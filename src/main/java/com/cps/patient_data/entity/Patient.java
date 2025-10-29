package com.cps.patient_data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patient_id")
    private Long patientId;

    private String name = "John Doe";

    @CreationTimestamp
    private LocalDateTime date;

    @Column(name = "heart_rate")
    private Double heartRate;
}
