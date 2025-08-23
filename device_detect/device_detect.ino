#include <Wire.h>

void setup() {
  Wire.begin(21, 22);  // SDA = GPIO21, SCL = GPIO22
  Serial.begin(115200);
  delay(1000);
  Serial.println("🔍 Scanning I2C devices...");
}

void loop() {
  byte error, address;
  int nDevices = 0;

  for (address = 1; address < 127; address++ ) {
    Wire.beginTransmission(address);
    error = Wire.endTransmission();

    if (error == 0) {
      Serial.print("✅ I2C device found at address 0x");
      if (address < 16) Serial.print("0");
      Serial.println(address, HEX);
      nDevices++;
    }
  }

  if (nDevices == 0)
    Serial.println("❌ No I2C devices found.");
  else
    Serial.println("✔️ Done.");

  delay(3000);
}
