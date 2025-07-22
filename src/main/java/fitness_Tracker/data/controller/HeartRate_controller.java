package fitness_Tracker.data.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fitness_Tracker.data.entity.HeartRate_entity;
import fitness_Tracker.data.service.HeartRate_service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/heartRate")
public class HeartRate_controller {

    @Autowired
    private HeartRate_service heartRateService;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @GetMapping("/getPatientIdForHR")
    public List<HeartRate_entity> getPatientId(@RequestParam int patientId) {
        return heartRateService.getPatientId(patientId);
    }

    @GetMapping("/getStartTime")
    public List<HeartRate_entity> getStartTime(@RequestParam Date startTime) {
        return heartRateService.getStartTime(startTime);
    }

    @GetMapping("/getEndTime")
    public List<HeartRate_entity> getEndTime(@RequestParam Date endTime) {
        return heartRateService.getEndTime(endTime);
    }

    @GetMapping("/getHeartRate")
    public List<HeartRate_entity> getHeartRate(@RequestParam int heartRate) {
        return heartRateService.getHeartRate(heartRate);
    }

    @PostMapping("/postHeartRateDataV2")
    public ResponseEntity<String> postHeartRate(@RequestBody HeartRate_entity heartRateEntity) {
        heartRateService.postHeartRateData(heartRateEntity);
        return ResponseEntity.ok("Heart Rate data successfully saved");
    }

    /**
     * Handles both:
     * 1) AWS IoT Core validation by echoing back the "challenge" token
     * 2) Normal heart rate data ingestion
     */
    @PostMapping("/data")
    public ResponseEntity<String> postHeartRateData(@RequestBody String rawBody) {
        try {
            JsonNode json = OBJECT_MAPPER.readTree(rawBody);
            // 1. AWS IoT confirmation handshake
            if (json.has("challenge") && json.get("challenge").isTextual()) {
                String token = json.get("challenge").asText();
                return ResponseEntity.ok(token);
            }

            // 2. Normal heart rate data
            HeartRate_entity hr = OBJECT_MAPPER.treeToValue(json, HeartRate_entity.class);
            heartRateService.postHeartRateData(hr);
            return ResponseEntity.ok("Heart Rate data successfully posted");

        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Invalid JSON: " + e.getMessage());
        }
    }
}
