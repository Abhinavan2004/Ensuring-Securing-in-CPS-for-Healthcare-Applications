package com.cps.patient_data.service;

import com.cps.patient_data.entity.Patient;
import com.cps.patient_data.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
@RequiredArgsConstructor
public class PatientService {

    @Autowired
    private final PatientRepository patientRepository;

    // Per-patient locks to avoid race conditions within this JVM
    private final ConcurrentMap<Long, Object> patientLocks = new ConcurrentHashMap<>();

    public Optional<Patient> getPatientById(Long patient_id) {
        return patientRepository.findByPatientId(patient_id);
    }

    public Patient getPatientName(String name) {
        return patientRepository.findByName(name);
    }

    public List<Patient> getAllPatients(java.time.LocalDate date, String name) {
        return patientRepository.findAllByDateAndName(date, name);
    }

    public List<Patient> getPatientByName(String name) {
        return patientRepository.findAllByName(name);
    }

    public List<Patient> getPatientByDate(java.time.LocalDate date) {
        return patientRepository.findByDate(date);
    }

    /**
     * Save incoming heart rate reading only if it obeys the 20-second rule.
     * This method synchronizes per patientId to prevent concurrent races in this JVM.
     *
     * NOTE: This prevents duplicates caused by concurrent requests in the SAME application instance.
     * For multi-instance deployments, see DB-based or distributed lock approaches below.
     */
    @Transactional
    public void postHeartRate(Patient patientData) {
        if (patientData == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "patient data is required");
        }

        Long patientId = patientData.getPatientId();
        if (patientId == null) {
            // If you require patientId, reject. Current behavior: reject if null.
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "patientId is required");
        }

        // get per-patient lock object (reused)
        Object lock = patientLocks.computeIfAbsent(patientId, id -> new Object());

        // ensure only one thread handles check+save for this patient at a time
        synchronized (lock) {
            LocalDateTime now = LocalDateTime.now();
            final long REQUIRED_SECONDS = 20L;

            // Get the most recent saved reading (if any)
            Optional<Patient> lastOpt = patientRepository.findTopByPatientIdOrderByDateDesc(patientId);

            if (lastOpt.isPresent()) {
                Patient last = lastOpt.get();
                LocalDateTime lastTime = last.getDate();
                if (lastTime != null) {
                    long secondsSince = Duration.between(lastTime, now).getSeconds();

                    // strict equality check: accept only if exactly 20 seconds elapsed
                    // If you want >= 20 seconds, change condition to secondsSince < REQUIRED_SECONDS
                    if (secondsSince < 19 || secondsSince > 21) {
                        // Reject and DO NOT save
                        throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS,
                                "Reading rejected: must be exactly " + REQUIRED_SECONDS +
                                        " seconds apart. (Elapsed: " + secondsSince + "s)");
                    }
                }
            } else {
                // No previous reading — decide whether to accept first reading.
                // We accept the first reading here. If you want to require an initial seed or registration, reject instead.
            }

            // Accept and save (server time used)
            patientData.setDate(now);
            patientRepository.save(patientData);
            // transaction will commit when method exits
        }
    }
}
