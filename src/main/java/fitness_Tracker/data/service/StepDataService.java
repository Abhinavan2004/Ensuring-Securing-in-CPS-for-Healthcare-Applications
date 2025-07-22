//package fitness_Tracker.data.service;
//
//import fitness_Tracker.data.entity.StepData;
//import fitness_Tracker.data.repository.StepDataRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class StepDataService {
//
//    private final StepDataRepository stepDataRepository;
//
//    @Autowired
//    public StepDataService(StepDataRepository stepDataRepository) {
//        this.stepDataRepository = stepDataRepository;
//    }
//
//    public List<StepData> getAllSteps() {
//        return stepDataRepository.findAll();
//    }
//
//    public Optional<StepData> getStepById(Long id) {
//        return stepDataRepository.findById(id);
//    }
//
//    public List<StepData> getStepsByPatientId(String patientId) {
//        return stepDataRepository.findByPatientId(patientId);
//    }
//
//    public StepData saveStepData(StepData stepData) {
//        return stepDataRepository.save(stepData);
//    }
//
//    public void deleteStepData(Long id) {
//        stepDataRepository.deleteById(id);
//    }
//}
