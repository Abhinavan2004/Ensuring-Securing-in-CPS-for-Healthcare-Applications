#include <WiFi.h>
#include <FirebaseESP32.h>


// WiFi credentials
#define WIFI_SSID "STUDENTS"
#define WIFI_PASSWORD "Students@rcoem"

// Firebase project credentials
#define API_KEY "AIzaSyCxOPT6_7uxl28h3bRHZORY8uf2kx5ua98"
#define DATABASE_URL "https://cps-firebase-1c4ef-default-rtdb.firebaseio.com"  

// Firebase objects
FirebaseData fbdo;
FirebaseAuth auth;
FirebaseConfig config;

unsigned long lastFetchTime = 0;
int heartRate = 0;

void setup() {
  Serial.begin(115200);
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);

  Serial.print("Connecting to Wi-Fi");
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(300);
  }
  Serial.println(" Connected!");

  config.api_key = API_KEY;
  config.database_url = DATABASE_URL;
  Firebase.begin(&config, &auth);
  Firebase.reconnectWiFi(true);
}

void loop() {
  if (Firebase.ready() && millis() - lastFetchTime > 3000) {
    lastFetchTime = millis();

    // Fetch action
    if (Firebase.getString(fbdo, "commands/patient_3/action")) {
      String action = fbdo.stringData();
      Serial.print("Action: ");
      Serial.println(action);
    } else {
      Serial.print("Failed to get action: ");
      Serial.println(fbdo.errorReason());
    }

    // Fetch timestamp
    if (Firebase.getInt(fbdo, "commands/patient_3/timestamp")) {
      long timestamp = fbdo.intData();
      Serial.print("Timestamp: ");
      Serial.println(timestamp);
    } else {
      Serial.print("Failed to get timestamp: ");
      Serial.println(fbdo.errorReason());
    }
  }
}