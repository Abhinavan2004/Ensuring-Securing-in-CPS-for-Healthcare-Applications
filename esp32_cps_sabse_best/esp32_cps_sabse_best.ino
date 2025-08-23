#include <WiFi.h>
#include <HTTPClient.h>
#include <Firebase_ESP_Client.h> // Ensure you have this library installed
#include <ArduinoJson.h>         // For parsing JSON from Firebase (if needed, or directly use FirebaseJson)

// --- Wi-Fi credentials ---
const char* ssid = "LAB-ONLY";
const char* password = "Labs@dtf";

// --- Spring Boot Backend Address (CRITICAL: REPLACE localhost with your PC's IP) ---
// Example: If your PC's IP is 192.168.1.10, use "http://192.168.1.10:8080/heartRate/data"
const char* serverAddress = "http://172.16.220.206:8080/heartRate/data";

// --- Firebase credentials (replace with your actual Firebase info) ---
#define API_KEY "AIzaSyCxOPT6_7uxl28h3bRHZORY8uf2kx5ua98" // Your Firebase Web API Key
#define DATABASE_URL "https://cps-firebase-1c4ef-default-rtdb.firebaseio.com" // Your Firebase Realtime Database URL

// Firebase objects
FirebaseData fbdo;
FirebaseAuth auth;
FirebaseConfig config;

// Define the Firebase path where heart rate data will be stored by the Kotlin app
// This MUST match the path used in your Kotlin app.
const String firebaseHeartRatePath = "/heartRateData/patient_3";

unsigned long sendDataPrevMillis = 0;
bool signupOK = false;

// Function to send data to Spring Boot backend via HTTP POST
void sendHeartRateToServer(int patientId, int heartRate) {
    HTTPClient http;
    http.begin(serverAddress);
    http.addHeader("Content-Type", "application/json");

    // Create JSON payload
    // Note: Your Spring Boot backend might expect startTime/endTime.
    // For simplicity, we'll send patientId and heartRate as per your project's data format.
    // If your backend requires startTime/endTime, you'll need to generate them here.
    String jsonPayload = "{\"patientId\":" + String(patientId) + ", \"heartRate\":" + String(heartRate) + "}";

    Serial.print("🔗 Sending POST request to: ");
    Serial.println(serverAddress);
    Serial.print("📦 Payload: ");
    Serial.println(jsonPayload);

    int httpResponseCode = http.POST(jsonPayload);

    if (httpResponseCode > 0) {
        Serial.print("✅ Response Code: ");
        Serial.println(httpResponseCode);
        Serial.println(http.getString());
    } else {
        Serial.print("❌ Error sending POST request: ");
        Serial.println(httpResponseCode);
        Serial.println(http.errorToString(httpResponseCode).c_str()); // Print detailed error
    }

    http.end();
}

// Callback function for Firebase Realtime Database stream
void streamCallback(FirebaseStream data) {
    Serial.printf("Stream Data Path: %s, Type: %s, Etag: %s\n", data.dataPath.c_str(), data.dataType.c_str(), data.ETag().c_str());

    // Check if the data is a JSON object (which our heart rate payload is)
    if (data.dataType() == "json") {
        FirebaseJson &json = data.jsonVariant;
        Serial.println("Received JSON from Firebase:");
        String jsonStr;
        json.toString(jsonStr, true); // Convert JSON to string for printing
        Serial.println(jsonStr);

        // Extract heartRate and patientId
        int patientId = json.get<int>("patientId");
        int heartRate = json.get<int>("heartRate");

        Serial.printf("Parsed Patient ID: %d, Heart Rate: %d\n", patientId, heartRate);

        // Send this data to your Spring Boot backend
        sendHeartRateToServer(patientId, heartRate);
    } else {
        Serial.printf("Received non-JSON data type: %s\n", data.dataType().c_str());
    }
}

// Callback function for Firebase stream status
void streamTimeoutCallback(bool timeout) {
    if (timeout) {
        Serial.println("Stream timeout, re-establishing...");
    }
    // You can add logic here to re-establish the stream if it times out
}

void setup() {
    Serial.begin(115200); // Changed to 115200 for better speed

    WiFi.begin(ssid, password);
    Serial.print("Connecting to WiFi");
    while (WiFi.status() != WL_CONNECTED) {
        delay(1000);
        Serial.print(".");
    }
    Serial.println("\nConnected to WiFi!");
    Serial.print("ESP32 IP Address: ");
    Serial.println(WiFi.localIP());

    // Firebase setup
    config.api_key = API_KEY;
    config.database_url = DATABASE_URL;

    // Optional: Set the size of the FirebaseJson object buffer
    // This helps with parsing larger JSON payloads.
    // config.json.setB().setJsonDataSize(1024);

    // Anonymous sign-in
    Serial.println("Attempting Firebase anonymous sign-in...");
    if (Firebase.signUp(&config, &auth, "", "")) { // Using signUp for anonymous, as per example
        Serial.println("✅ Firebase SignUp OK (Anonymous Auth)");
        signupOK = true;
    } else {
        Serial.printf("❌ Firebase SignUp failed: %s\n", config.signer.signupError.message.c_str());
        // If signup fails, try signInAnonymously directly if it's already signed up
        if (Firebase.auth.signInAnonymously(&config, &auth)) {
            Serial.println("✅ Firebase SignIn (Anonymous Auth) OK");
            signupOK = true;
        } else {
            Serial.printf("❌ Firebase SignIn failed: %s\n", config.signer.tokenError.message.c_str());
        }
    }

    Firebase.begin(&config, &auth);
    Firebase.reconnectWiFi(true); // Automatically reconnect WiFi if disconnected

    // Set up Firebase Realtime Database stream
    if (signupOK) {
        Serial.print("Setting up Firebase stream on path: ");
        Serial.println(firebaseHeartRatePath);
        if (Firebase.RTDB.beginStream(&fbdo, firebaseHeartRatePath)) {
            Firebase.RTDB.setStreamCallback(&streamCallback, &streamTimeoutCallback);
            Serial.println("✅ Firebase Stream initialized successfully.");
        } else {
            Serial.printf("❌ Failed to begin Firebase stream: %s\n", fbdo.errorReason().c_str());
        }
    }
}

void loop() {
    // Firebase.loop() must be called in loop() for stream processing and token refreshing
    if (Firebase.ready() && signupOK) {
        Firebase.RTDB.runStream(&fbdo);
    } else {
        // Handle cases where Firebase is not ready or signup failed
        Serial.println("Firebase not ready or signup failed. Retrying...");
        delay(1000); // Small delay to prevent tight loop
        // You might want to re-attempt Firebase.begin() or sign-in here
    }

    // Your existing periodic send (if still desired, but Firebase stream is better)
    // if (millis() - sendDataPrevMillis > 5000 && WiFi.status() == WL_CONNECTED) {
    //     sendDataPrevMillis = millis();
    //     // This part is now handled by the Firebase stream
    //     // sendHeartRateToServer(80); // Simulated HR data
    // }

    // Your existing command check (if still desired)
    // This part is separate from the heart rate stream
    if (Firebase.ready() && signupOK) {
        if (Firebase.RTDB.getString(&fbdo, "commands/patient_3")) {
            if (fbdo.dataType() == "string") {
                String command = fbdo.stringData();
                if (command.length() > 0) { // Only process if command is not empty
                    Serial.print("📥 Received Firebase Command: ");
                    Serial.println(command);

                    if (command == "start") {
                        Serial.println("💓 Starting heart rate tracking...");
                    } else if (command == "stop") {
                        Serial.println("🛑 Stopping heart rate tracking...");
                    }

                    // Clear command after reading
                    Firebase.RTDB.setString(&fbdo, "commands/patient_3", "");
                }
            }
        } else {
            // Serial.print("⚠ Firebase read failed for commands: "); // Suppress frequent error if path is empty
            // Serial.println(fbdo.errorReason());
        }
    }
    // Small delay to prevent watchdog timer resets, especially if no other blocking code
    delay(10);
}
