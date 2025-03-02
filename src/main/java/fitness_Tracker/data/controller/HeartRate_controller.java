package fitness_Tracker.data.controller;

import fitness_Tracker.data.entity.HeartRate_entity;
import fitness_Tracker.data.service.HeartRate_service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/heartRate")
public class HeartRate_controller {

    @Autowired
    private HeartRate_service heartRateService;

    @GetMapping("/getPatientId")
    public List<HeartRate_entity> getPatientId(@RequestParam int patientId){
        return heartRateService.getPatientId(patientId);
    }

    @GetMapping("/getStartTime")
    public List<HeartRate_entity> getStartTime(@RequestParam Date startTime){
        return heartRateService.getStartTime(startTime);
    }

    @GetMapping("/getEndTime")
    public List<HeartRate_entity> getEndTime(@RequestParam Date endTime){
        return heartRateService.getEndTime(endTime);
    }

    @GetMapping("/getHeartRate")
    public List<HeartRate_entity> getHeartRate(@RequestParam int heartRate){
        return heartRateService.getHeartRate(heartRate);
    }

    @PostMapping("/postHeartRateDataV2")
    public String postHeartRate(@RequestBody HeartRate_entity heartRateEntity) {
        heartRateService.postHeartRateData(heartRateEntity);
        return "Heart Rate data successfully saved";
    }

    @PostMapping("/data")
    public String postHeartRateData(@RequestBody HeartRate_entity heartRateEntity){
        heartRateService.postHeartRateData(heartRateEntity);
        return "Heart Rate data successfully posted";
    }
}
