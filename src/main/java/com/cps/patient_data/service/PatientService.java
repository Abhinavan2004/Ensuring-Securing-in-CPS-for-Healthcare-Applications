package com.cps.patient_data.service;

import com.cps.patient_data.entity.Patient;
import com.cps.patient_data.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    /**
     * Save incoming heart rate reading only if >= 20 seconds have passed since last accepted reading
     * for the same patientId. If a reading arrives earlier, reject with 429 Too Many Requests.
     *
     * Expects patientData.getPatientId() to be set (preferred). If patientId is null, this still saves
     * (you can change behavior to require patientId if needed).
     */
    public void postHeartRate(Patient patientData) {
        if (patientData == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "patient data is required");
        }

        Long patientId = patientData.getPatientId();
        LocalDateTime now = LocalDateTime.now();
        final long REQUIRED_SECONDS = 20L;

        if (patientId != null) {
            Optional<Patient> lastOpt = patientRepository.findTopByPatientIdOrderByDateDesc(patientId);
            if (lastOpt.isPresent()) {
                Patient last = lastOpt.get();
                LocalDateTime lastTime = last.getDate();
                if (lastTime != null) {
                    long secondsSince = Duration.between(lastTime, now).getSeconds();
                    // ACCEPT only if elapsed time is exactly 20 seconds
                    if (secondsSince != REQUIRED_SECONDS) {
                        throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS,
                                "Reading rejected: must be exactly " + REQUIRED_SECONDS +
                                        " seconds apart. (Elapsed: " + secondsSince + "s)");
                    }
                }
            }
        }

        // Accept and save
        patientData.setDate(now);
        patientRepository.save(patientData);
    }

}
