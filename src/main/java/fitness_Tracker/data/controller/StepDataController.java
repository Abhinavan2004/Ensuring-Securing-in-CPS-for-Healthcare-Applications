//package fitness_Tracker.data.controller;
//
//import fitness_Tracker.data.entity.StepData;
//import fitness_Tracker.data.service.StepDataService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/api/steps")
//public class StepDataController {
//
//    private final StepDataService stepDataService;
//
//    @Autowired
//    public StepDataController(StepDataService stepDataService) {
//        this.stepDataService = stepDataService;
//    }
//
//    @GetMapping
//    public List<StepData> getAllSteps() {
//        return stepDataService.getAllSteps();
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<StepData> getStepById(@PathVariable Long id) {
//        Optional<StepData> stepData = stepDataService.getStepById(id);
//        return stepData.map(ResponseEntity::ok)
//                .orElseGet(() -> ResponseEntity.notFound().build());
//    }
//
//    @GetMapping("/patient/{patientId}")
//    public List<StepData> getStepsByPatientId(@PathVariable String patientId) {
//        return stepDataService.getStepsByPatientId(patientId);
//    }
//
//    @PostMapping
//    public ResponseEntity<StepData> saveStepData(@RequestBody StepData stepData) {
//        StepData savedData = stepDataService.saveStepData(stepData);
//        return ResponseEntity.ok(savedData);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteStepData(@PathVariable Long id) {
//        if (stepDataService.getStepById(id).isPresent()) {
//            stepDataService.deleteStepData(id);
//            return ResponseEntity.noContent().build();
//        }
//        return ResponseEntity.notFound().build();
//    }
//}
