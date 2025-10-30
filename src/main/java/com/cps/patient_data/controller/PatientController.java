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

    @GetMapping("getPatients/byName")
    public Patient getPatientByName(@RequestParam String name) {
        return patientService.getPatientName(name);
    }

    @GetMapping("getAllPatients")
    public List<Patient> getAllPatients(@RequestParam LocalDate date, @RequestParam String name) {
        return patientService.getAllPatients(date, name);
    }

    @PostMapping("/postPatientData")
    public String postPatientData(
            @RequestBody Patient patientData,
            HttpServletRequest request,
            @RequestHeader(value = "x-nonce", required = false) String requestNonce) {

        String clientIp = request.getRemoteAddr();

        if (requestNonce == null || !requestNonce.equals(validNonce)) {
            System.out.println("🚫 Unauthorized request: Invalid or missing nonce from IP " + clientIp);
            return "Access denied: Invalid nonce. Data not saved.";
        }

        System.out.println("✅ Data accepted from ESP32. IP: " + clientIp + " | Nonce verified.");
        patientService.postHeartRate(patientData);
        return "Data successfully saved from verified ESP32.";
    }
}
