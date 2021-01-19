package ies.g25.aLIVE.restcontroller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ies.g25.aLIVE.exception.ResourceNotFoundException;
import ies.g25.aLIVE.model.BloodPressure;
import ies.g25.aLIVE.model.BodyTemperature;
import ies.g25.aLIVE.model.HeartRate;
import ies.g25.aLIVE.model.OxygenLevel;
import ies.g25.aLIVE.model.Patient;
import ies.g25.aLIVE.model.SugarLevel;
import ies.g25.aLIVE.repository.BloodPressureRepository;
import ies.g25.aLIVE.repository.BodyTemperatureRepository;
import ies.g25.aLIVE.repository.HeartRateRepository;
import ies.g25.aLIVE.repository.OxygenLevelRepository;
import ies.g25.aLIVE.repository.PatientRepository;
import ies.g25.aLIVE.repository.SugarLevelRepository;

@RestController                                             
@RequestMapping("/api/data") 
public class HealthDataRestController{
    //get users, get health info from a user
    @Autowired
    public PatientRepository patientRepository;

    @Autowired
    public HeartRateRepository heartRateRepository;

    @Autowired
    public SugarLevelRepository sugarLevelRepository;

    @Autowired
    public OxygenLevelRepository oxygenLevelRepository;

    @Autowired
    public BloodPressureRepository bloodPressureRepository;

    @Autowired
    public BodyTemperatureRepository bodyTemperatureRepository;

    public HealthDataRestController(PatientRepository patientRepository, HeartRateRepository heartRateRepository,
      SugarLevelRepository sugarLevelRepository, OxygenLevelRepository oxygenLevelRepository, BloodPressureRepository bloodPressureRepository, BodyTemperatureRepository bodyTemperatureRepository) {
        this.patientRepository=patientRepository;
        this.heartRateRepository=heartRateRepository;
        this.sugarLevelRepository=sugarLevelRepository;
        this.oxygenLevelRepository=oxygenLevelRepository;
        this.bloodPressureRepository=bloodPressureRepository;
        this.bodyTemperatureRepository= bodyTemperatureRepository;
    }

    @GetMapping("/sugarlevel")
    @ResponseBody
    public List<SugarLevel> getAllSugarLevels() {
        return sugarLevelRepository.findAll();
    }


    @PostMapping("/sugarlevel/{pid}")
    public SugarLevel createSugarLevel(@PathVariable(value = "pid") long pid, @Valid @RequestBody SugarLevel sl) throws ResourceNotFoundException {
        Optional<Patient> op=patientRepository.findById(pid);
        if(op.isPresent()){
            sl.setPatient(op.get());
            return sugarLevelRepository.save(sl);
        }
        throw new ResourceNotFoundException("Patient not found for this id: " + pid);
    }

    @GetMapping("/oxygenlevel")
    @ResponseBody
    public List<OxygenLevel> getAllOxygenLevels() {
        return oxygenLevelRepository.findAll();
    }


    @PostMapping("/oxygenlevel/{pid}")
    public OxygenLevel createOxygenLevel(@PathVariable(value = "pid") long pid, @Valid @RequestBody OxygenLevel ol) throws ResourceNotFoundException {
        Optional<Patient> op=patientRepository.findById(pid);
        if(op.isPresent()){
            ol.setPatient(op.get());
            return oxygenLevelRepository.save(ol);
        }
        throw new ResourceNotFoundException("Patient not found for this id: " + pid);
    }

    @GetMapping("/heartrate")
    @ResponseBody
    public List<HeartRate> getAllHeartRates() {
        return heartRateRepository.findAll();
    }


    @PostMapping("/heartrate/{pid}")
    public HeartRate createHeartRate( @PathVariable(value = "pid") long pid, @Valid @RequestBody HeartRate hr) throws ResourceNotFoundException {
        Optional<Patient> op=patientRepository.findById(pid);
        if(op.isPresent()){
            hr.setPatient(op.get());
            return heartRateRepository.save(hr);
        }
        throw new ResourceNotFoundException("Patient not found for this id: " + pid);
    }

    @GetMapping("/bloodpressure")
    @ResponseBody
    public List<BloodPressure> getAllBloodPressures() {
        return bloodPressureRepository.findAll();
    }

    @PostMapping("/bloodpressure/{pid}")
    public BloodPressure createBloodPressure(@Valid @RequestBody BloodPressure bp, @PathVariable(value = "pid") long pid) throws ResourceNotFoundException{
        Optional<Patient> op=patientRepository.findById(pid);
        if(op.isPresent()){
            bp.setPatient(op.get());
            return bloodPressureRepository.save(bp);
        }       
        throw new ResourceNotFoundException("Patient not found for this id: " + pid);
    }


    @GetMapping("/bodytemperature")
    @ResponseBody
    public List<BodyTemperature> getAllBodyTemperature() {
        return bodyTemperatureRepository.findAll();
    }



    @PostMapping("/bodytemperature/{pid}")
    public BodyTemperature createBloodPressure(@Valid @RequestBody BodyTemperature bt, @PathVariable(value = "pid") long pid)  throws ResourceNotFoundException{
        Optional<Patient> op=patientRepository.findById(pid);
        if(op.isPresent()){
            bt.setPatient(op.get());
            return bodyTemperatureRepository.save(bt);
        }
        throw new ResourceNotFoundException("Patient not found for this id: " + pid);
    }
    

}