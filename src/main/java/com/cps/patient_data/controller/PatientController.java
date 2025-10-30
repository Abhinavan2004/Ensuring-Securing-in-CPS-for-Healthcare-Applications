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

    // Secure values loaded from environment variables
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

    @PostMapping("/postPatientData")
    public String postPatientData(
            @RequestBody Patient patientData,
            HttpServletRequest request,
            @RequestHeader(value = "x-nonce", required = false) String requestNonce,
            @RequestHeader(value = "X-Device-MAC", required = false) String deviceMacHeader) {

        String remoteIp = request.getRemoteAddr();

        // 1️⃣ Validate Nonce
        if (requestNonce == null || !requestNonce.equals(validNonce)) {
            System.out.println("🚫 Potential attack detected: Invalid nonce from IP " + remoteIp);
            return "Potential attack detected: Invalid or missing nonce.";
        }

        // 2️⃣ Validate Device MAC
        if (deviceMacHeader == null || !deviceMacHeader.trim().equalsIgnoreCase(allowedDeviceMac.trim())) {
            System.out.println("🚫 Potential attack detected: Invalid or missing MAC from IP " + remoteIp);
            return "Potential attack detected: Invalid or missing device MAC.";
        }

        // 3️⃣ If both checks pass → save data
        System.out.println("✅ Verified request from device " + deviceMacHeader + " (IP: " + remoteIp + ")");
        patientService.postHeartRate(patientData);
        return "Data successfully saved from verified device.";
    }
}
