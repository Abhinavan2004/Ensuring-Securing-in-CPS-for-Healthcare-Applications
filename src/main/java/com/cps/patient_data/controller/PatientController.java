package com.cps.patient_data.controller;

import com.cps.patient_data.entity.Patient;
import com.cps.patient_data.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/patientData")
public class PatientController {

    @Autowired
    private PatientService patientService;

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
    public void postPatientData(@RequestBody Patient patientData) {
        patientService.postHeartRate(patientData);
    }
}
