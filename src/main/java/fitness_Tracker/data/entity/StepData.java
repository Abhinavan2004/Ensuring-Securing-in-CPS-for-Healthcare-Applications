//package fitness_Tracker.data.entity;
//
//import jakarta.persistence.*;
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "steps")
//public class StepData {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(name = "patient_id", nullable = false)
//    private String patientId;
//
//    @Column(name = "step_count", nullable = false)
//    private int stepCount;
//
//    @Column(name = "timestamp", nullable = false)
//    private LocalDateTime timestamp;
//
//    public StepData() {
//    }
//
//    public StepData(String patientId, int stepCount, LocalDateTime timestamp) {
//        this.patientId = patientId;
//        this.stepCount = stepCount;
//        this.timestamp = timestamp;
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getPatientId() {
//        return patientId;
//    }
//
//    public void setPatientId(String patientId) {
//        this.patientId = patientId;
//    }
//
//    public int getStepCount() {
//        return stepCount;
//    }
//
//    public void setStepCount(int stepCount) {
//        this.stepCount = stepCount;
//    }
//
//    public LocalDateTime getTimestamp() {
//        return timestamp;
//    }
//
//    public void setTimestamp(LocalDateTime timestamp) {
//        this.timestamp = timestamp;
//    }
//}
