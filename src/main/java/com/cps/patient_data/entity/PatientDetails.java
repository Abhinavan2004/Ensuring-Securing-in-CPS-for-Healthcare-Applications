//package com.cps.patient_data.entity;
//
//import com.cps.patient_data.type.BloodGroupType;
//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.Setter;
//
//@Entity
//@Getter
//@Setter
//@Table(name = "patient_details")
//public class PatientDetails {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id")
//    private Long id;
//
//    private int age;
//
//    @Enumerated
//    private BloodGroupType bloodGroup;
//
//    private String contactNo;
//    private String address;
//
//    @OneToOne
//    @JoinColumn(name = "patient_id", referencedColumnName = "patient_id")
//    private Patient patient;
//}
