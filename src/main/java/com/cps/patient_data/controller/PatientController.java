package com.cps.patient_data.controller;

import com.cps.patient_data.entity.Patient;
import com.cps.patient_data.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/patientData")
public class PatientController {

    @Autowired
    private PatientService patientService;

    private static final String ESP32_IP = "10.229.218.238";

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
    public String postPatientData(@RequestBody Patient patientData, HttpServletRequest request) {
        String clientIp = request.getRemoteAddr();

        if (!ESP32_IP.equals(clientIp)) {
            System.out.println("⚠️ Unauthorized data attempt from IP: " + clientIp);
            return "Access denied: Unauthorized IP (" + clientIp + "). Data not saved.";
        }

        System.out.println("✅ Data accepted from ESP32 IP: " + clientIp);
        patientService.postHeartRate(patientData);
        return "Data successfully saved from ESP32 IP: " + clientIp;
    }
}
