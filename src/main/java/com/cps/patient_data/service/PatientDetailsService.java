//package com.cps.patient_data.service;
//
//import com.cps.patient_data.entity.Patient;
//import com.cps.patient_data.entity.PatientDetails;
//import com.cps.patient_data.repository.PatientDetailsRepository;
//import com.cps.patient_data.repository.PatientRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//
//@Service
//@RequiredArgsConstructor
//public class PatientDetailsService {
//
//    @Autowired
//    private PatientDetailsRepository patientDetailsRepository;
//
//    @Autowired
//    private PatientRepository patientRepository;
//
//    public PatientDetails savePatientDetails(Long patient_id, PatientDetails details) {
//        Patient patient = patientRepository.findByPatientId(patient_id)
//            .orElseThrow(() -> new RuntimeException("Patient does not found with ID: "+patient_id));
//        details.setPatient(patient);
//        return patientDetailsRepository.save(details);
//    }
//
//    public Optional<PatientDetails> getDetailsByPatientId(Long patient_id) {
//        return patientDetailsRepository.findByPatientId(patient_id);
//    }
//
//    public PatientDetails getContactNo(String contactNo) {
//        return patientDetailsRepository.findContactNo(contactNo);
//    }
//
//    public void postPatientDetails(PatientDetails patientDetails) {
//        patientDetailsRepository.save(patientDetails);
//    }
//}
