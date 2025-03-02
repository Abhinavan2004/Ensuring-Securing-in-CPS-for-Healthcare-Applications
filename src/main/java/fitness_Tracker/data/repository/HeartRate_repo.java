package fitness_Tracker.data.repository;

import fitness_Tracker.data.entity.HeartRate_entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.List;

@Repository
public interface HeartRate_repo extends JpaRepository <HeartRate_entity, Integer> {
    List<HeartRate_entity> findByPatientId(int patientId);
    List<HeartRate_entity> findByStartTime(Date startTime);
    List<HeartRate_entity> findByEndTime(Date endTime);
    List<HeartRate_entity> findByHeartRate(int heartRate);
}
