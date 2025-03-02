package fitness_Tracker.data.service;


import fitness_Tracker.data.entity.HeartRate_entity;
import fitness_Tracker.data.repository.HeartRate_repo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class HeartRate_service {

    @Autowired
    private HeartRate_repo heartRateRepo;

    public List<HeartRate_entity> findAll(){
        return heartRateRepo.findAll();
    }

    public List<HeartRate_entity> getPatientId(int patientId){
        return heartRateRepo.findByPatientId(patientId);
    }

    public List<HeartRate_entity> getStartTime(Date startTime){
        return heartRateRepo.findByStartTime(startTime);
    }

    public List<HeartRate_entity> getEndTime(Date endTime){
        return heartRateRepo.findByEndTime(endTime);
    }

    public List<HeartRate_entity> getHeartRate(int heartRate){
        return heartRateRepo.findByHeartRate(heartRate);
    }

    public void postHeartRate(HeartRate_entity heartRateEntity){
        heartRateRepo.save(heartRateEntity);
    }

    public void postHeartRateData(HeartRate_entity heartRateEntity){
        heartRateRepo.save(heartRateEntity);
    }

    public void postHeartRateDataV2(int patientId, Date startTime, Date endTime, int heartRate){
        HeartRate_entity heartRateEntity = new HeartRate_entity();

        heartRateEntity.setPatientId(patientId);
        heartRateEntity.setStartTime(startTime);
        heartRateEntity.setEndTime(endTime);
        heartRateEntity.setHeartRate(heartRate);

        heartRateRepo.save(heartRateEntity);
    }
}
