//package com.cps.patient_data.controller;
//
//import com.cps.patient_data.entity.PatientDetails;
//import com.cps.patient_data.service.PatientDetailsService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/patientDetails")
////public class PatientDetailsController {
////
////    @Autowired
////    private PatientDetailsService patientDetailsService;
////
////    @GetMapping("/{patientId}")
////    public ResponseEntity<PatientDetails> getDetails(@PathVariable Long patient_id) {
////        return patientDetailsService.getDetailsByPatientId(patient_id)
////                .map(ResponseEntity::ok)
////                .orElse(ResponseEntity.notFound().build());
////    }
////
////    @PostMapping("/{patientId}")
////    public ResponseEntity<PatientDetails> postDetails(@PathVariable Long patient_id, @RequestBody PatientDetails details) {
////        PatientDetails saveDetails = patientDetailsService.savePatientDetails(patient_id, details);
////        return ResponseEntity.ok(saveDetails);
////    }
////}
