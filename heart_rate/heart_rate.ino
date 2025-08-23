#include <Wire.h>
#include "MAX30105.h"
#include "heartRate.h"

MAX30105 particleSensor;

const byte RATE_SIZE = 4; // Number of samples to average
byte rates[RATE_SIZE]; // Array of heart rate values
byte rateSpot = 0;
long lastBeat = 0; // Time at which the last beat occurred

float beatsPerMinute;
int beatAvg;

void setup() {
  Serial.begin(115200);
  Wire.begin(21, 22); // SDA = 21, SCL = 22 for ESP32

  Serial.println("Initializing MAX30102...");

  if (!particleSensor.begin(Wire, I2C_SPEED_STANDARD)) {
    Serial.println("MAX30102 was not found. Please check wiring/power.");
    while (1);
  }

  Serial.println("MAX30102 initialized!");

  // Setup sensor with default configuration
  particleSensor.setup(); 
  particleSensor.setPulseAmplitudeRed(0x0A);   // Turn on Red LED
  particleSensor.setPulseAmplitudeGreen(0);    // Turn off Green LED
}

void loop() {
  long irValue = particleSensor.getIR(); // Read IR value

  if (irValue < 50000) {
    Serial.println("Place your finger on the sensor.");
    delay(500);
    return;
  }

  if (checkForBeat(irValue)) {
    long delta = millis() - lastBeat;
    lastBeat = millis();

    beatsPerMinute = 60 / (delta / 1000.0);

    if (beatsPerMinute < 255 && beatsPerMinute > 20) {
      rates[rateSpot++] = (byte)beatsPerMinute;
      rateSpot %= RATE_SIZE;

      beatAvg = 0;
      for (byte x = 0; x < RATE_SIZE; x++)
        beatAvg += rates[x];
      beatAvg /= RATE_SIZE;
    }

    Serial.print("❤️ BPM: ");
    Serial.print(beatsPerMinute);
    Serial.print(" | Avg BPM: ");
    Serial.println(beatAvg);
  }
}
