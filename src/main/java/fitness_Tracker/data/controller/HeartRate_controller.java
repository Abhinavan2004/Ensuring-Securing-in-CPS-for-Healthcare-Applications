package fitness_Tracker.data.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fitness_Tracker.data.entity.HeartRate_entity;
import fitness_Tracker.data.service.HeartRate_service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeParseException;
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
     * 1) AWS IoT Core validatin by echoing back the "challenge" token
     * 2) Normal heart rate data ingestion
     */
    @PostMapping("/data")
    public ResponseEntity<String> postHeartRateData(@RequestBody String rawBody) {
        System.out.println(">>> Received rawBody: " + rawBody);
        try {
            JsonNode json = OBJECT_MAPPER.readTree(rawBody);

            // 1) AWS IoT handshake
            if (json.has("challenge") && json.get("challenge").isTextual()) {
                return ResponseEntity.ok(json.get("challenge").asText());
            }

            // 2) Normal heart rate ingestion
            // -- Extract primitive fields
            int patientId   = json.get("patientId").asInt();
            int heartRate   = json.get("heartRate").asInt();
            String startIso = json.get("startTime").asText();
            String endIso   = json.get("endTime").asText();

            Instant startInst = Instant.parse(startIso);
            Instant endInst   = Instant.parse(endIso);
            Date startDate    = Date.from(startInst);
            Date endDate      = Date.from(endInst);

            // -- Build your entity
            HeartRate_entity hr = new HeartRate_entity();
            hr.setPatientId(patientId);
            hr.setHeartRate(heartRate);
            hr.setStartTime(startDate);
            hr.setEndTime(endDate);

            // -- Persist
            heartRateService.postHeartRateData(hr);
            return ResponseEntity.ok("Heart Rate data successfully posted");

        } catch (DateTimeParseException dtpe) {
            return ResponseEntity
                    .badRequest()
                    .body("Invalid timestamp format: " + dtpe.getParsedString());
        } catch (IOException ioe) {
            return ResponseEntity
                    .badRequest()
                    .body("Malformed JSON: " + ioe.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();  // log the full stacktrace
            return ResponseEntity
                    .status(500)
                    .body("Server Error: " + ex.getMessage());
        }
    }
}