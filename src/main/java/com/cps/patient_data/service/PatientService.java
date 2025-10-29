package com.cps.patient_data.service;

import com.cps.patient_data.entity.Patient;
import com.cps.patient_data.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientService {

    @Autowired
    private final PatientRepository patientRepository;

    public Optional<Patient> getPatientById(Long patient_id) {
        return patientRepository.findByPatientId(patient_id);
    }

    public Patient getPatientName(String name) {
        return patientRepository.findByName(name);
    }

    public List<Patient> getAllPatients(LocalDate date, String name) {
        return patientRepository.findAllByDateAndName(date, name);
    }

    public List<Patient> getPatientByName(String name) {
        return patientRepository.findAllByName(name);
    }

    public List<Patient> getPatientByDate(LocalDate date) {
        return patientRepository.findByDate(date);
    }

    public void postHeartRate(Patient patientData) {
        patientRepository.save(patientData);
    }
}
