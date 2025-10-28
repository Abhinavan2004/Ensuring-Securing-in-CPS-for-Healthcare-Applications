package com.cps.patient_data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Entity
@Getter
@Setter
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patient_id")
    private Long patientId;

    private String name;

    @CreationTimestamp
    private LocalDateTime date;

    @Column(name = "heart_rate")
    private Double heartRate;

    @Transient
    private static final List<String> NAMES = List.of(
            "Devansh",
            "Abhinav",
            "Atharva",
            "Om",
            "Parth"
    );

    @PrePersist
    public void assignRandomName() {
        if (this.name == null) {
            Random random = new Random();
            this.name = NAMES.get(random.nextInt(NAMES.size()));
        }
    }
}
