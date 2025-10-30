package com.cps.patient_data.controller;

import com.cps.patient_data.entity.Patient;
import com.cps.patient_data.service.PatientService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/patientData")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @Value("${app.security.nonce}")
    private String validNonce;

    @Value("${app.device.mac}")
    private String allowedDeviceMac;

    @GetMapping("/getPatient/Id")
    public Optional<Patient> getPatientById(@RequestParam Long patient_id) {
        return patientService.getPatientById(patient_id);
    }

    @GetMapping("/getPatients/name")
    public List<Patient> getPatientsByName(@RequestParam String name) {
        return patientService.getPatientByName(name);
    }

    @GetMapping("/getPatients/date")
    public List<Patient> getPatientsByDate(@RequestParam LocalDate date) {
        return patientService.getPatientByDate(date);
    }

    @GetMapping("/getPatients/byName")
    public Patient getPatientByName(@RequestParam String name) {
        return patientService.getPatientName(name);
    }

    @GetMapping("/getAllPatients")
    public List<Patient> getAllPatients(@RequestParam LocalDate date, @RequestParam String name) {
        return patientService.getAllPatients(date, name);
    }

    // ==== Custom cipher function (must match ESP32) ====
    private String computeCustomCipher(String nonce, String mac) {
        String normalizedMac = mac.replace(":", "").toLowerCase();
        String combined = nonce + "|" + normalizedMac;
        StringBuilder cipher = new StringBuilder();

        for (int i = 0; i < combined.length(); i++) {
            int b = combined.charAt(i);
            int offset = (i * 31) & 0xFF;
            int transformed = ((b + offset) & 0xFF) ^ 0xA5;
            cipher.append(String.format("%02x", transformed));
        }

        return cipher.toString();
    }

    @PostMapping("/postPatientData")
    public String postPatientData(
            @RequestBody Patient patientData,
            HttpServletRequest request,
            @RequestHeader(value = "X-Device-Cipher", required = false) String deviceCipher) {

        String remoteIp = request.getRemoteAddr();

        if (deviceCipher == null) {
            System.out.println("🚫 Missing cipher from IP " + remoteIp);
            return "Unauthorized: Missing device cipher.";
        }

        String expectedCipher = computeCustomCipher(validNonce, allowedDeviceMac);

        if (!deviceCipher.trim().equalsIgnoreCase(expectedCipher.trim())) {
            System.out.println("🚫 Invalid cipher from IP " + remoteIp);
            return "Unauthorized: Invalid device cipher.";
        }

        System.out.println("✅ Verified cipher from IP: " + remoteIp);
        patientService.postHeartRate(patientData);
        return "Data successfully saved from verified device.";
    }
}
