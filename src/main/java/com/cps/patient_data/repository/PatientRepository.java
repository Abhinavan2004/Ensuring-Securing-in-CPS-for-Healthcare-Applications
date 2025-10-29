package com.cps.patient_data.repository;

import com.cps.patient_data.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    Optional<Patient> findByPatientId(Long patient_id);
    Patient findByName(String name);

    List<Patient> findAllByName(String name);
    List<Patient> findByDate(LocalDate date);
    List<Patient> findAllByDateAndName(LocalDate date, String name);

    Optional<Patient> findTopByPatientIdOrderByDateDesc(Long patientId);
}
