package fitness_Tracker.data.controller;

import fitness_Tracker.data.entity.HeartRate_entity;
import fitness_Tracker.data.service.HeartRate_service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("HeartRate_controller")
public class HeartRate_controller {

    @Autowired
    private HeartRate_service heartRateService;

    @GetMapping("/getPatientId")
    public List<HeartRate_entity> getPatientId(int patientId){
        return heartRateService.getPatientId(patientId);
    }

    @GetMapping("/getStartTime")
    public List<HeartRate_entity> getStartTime(Date startTime){
        return heartRateService.getStartTime(startTime);
    }

    @GetMapping("/getEndTime")
    public List<HeartRate_entity> getEndTime(Date endTime){
        return heartRateService.getEndTime(endTime);
    }

    @GetMapping("/getHeartRate")
    public List<HeartRate_entity> getHeartRate(int heartRate){
        return heartRateService.getHeartRate(heartRate);
    }

    @PostMapping("/postHeartRateData")
    public String postHeartRateData(@RequestBody HeartRate_entity heartRateEntity){
        heartRateService.postHeartRateData(heartRateEntity);
        return "Heart Rate data is successfully posted";
    }

    @PostMapping("/postHeartRateDataV2")
    public String postHeartRateDataV2(
        @RequestParam("patientId") int patientId,
        @RequestParam("startTime") Date startTime,
        @RequestParam("endTime") Date endTime,
        @RequestParam("heartRate") int heartRate) {
        heartRateService.postHeartRateDataV2(patientId, startTime, endTime, heartRate);
        return "Heart Rate data successfully saved";
    }
}
