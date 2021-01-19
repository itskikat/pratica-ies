package ies.g25.aLIVE.restcontroller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import ies.g25.aLIVE.model.Sensor;
import ies.g25.aLIVE.model.SugarLevel;
import ies.g25.aLIVE.repository.BloodPressureRepository;
import ies.g25.aLIVE.repository.BodyTemperatureRepository;
import ies.g25.aLIVE.repository.HeartRateRepository;
import ies.g25.aLIVE.repository.OxygenLevelRepository;
import ies.g25.aLIVE.repository.PatientRepository;
import ies.g25.aLIVE.repository.SensorRepository;
import ies.g25.aLIVE.repository.SugarLevelRepository;

@RestController
@RequestMapping("/api/sensors")
public class SensorRestController {

    @Autowired
    public SensorRepository sensorRepository;

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

    public SensorRestController (SensorRepository sensorRepository, PatientRepository patientRepository, HeartRateRepository heartRateRepository, SugarLevelRepository sugarLevelRepository,
            OxygenLevelRepository oxygenLevelRepository, BloodPressureRepository bloodPressureRepository, BodyTemperatureRepository bodyTemperatureRepository){

            this.sensorRepository=sensorRepository;
            this.bloodPressureRepository=bloodPressureRepository;
            this.bodyTemperatureRepository=bodyTemperatureRepository;
            this.heartRateRepository=heartRateRepository;
            this.sugarLevelRepository=sugarLevelRepository;
            this.oxygenLevelRepository=oxygenLevelRepository;
            this.patientRepository=patientRepository;
    }

    @GetMapping()
    @ResponseBody
    public List<Sensor> getAllSensors() {
        return sensorRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sensor> getSensortById(@PathVariable(value = "id") Long id) throws ResourceNotFoundException {
        Optional<Sensor> op=  sensorRepository.findById(id);
        if(op.isPresent()){
            Sensor s = op.get();
            return ResponseEntity.ok().body(s);
        }
        throw new ResourceNotFoundException("Sensor not found for this id: " + id);
    }

    @GetMapping("/ids")
    public List<Long> getNumberOfSensors(){
        return sensorRepository.getAllIds();
    }
    
    @PostMapping("/{id}/sugarlevel")
    public SugarLevel createSugarLevel(@PathVariable(value = "id") long id, @Valid @RequestBody SugarLevel sl) throws ResourceNotFoundException {
        Optional<Sensor> op = sensorRepository.findById(id);
        if(op.isPresent()){
            sl.setPatient(op.get().getPatient());
            return sugarLevelRepository.save(sl);
        }
        throw new ResourceNotFoundException("Sensor not found for this id: " + id);
    }

    @PostMapping("/{id}/oxygenlevel")
    public OxygenLevel createOxygenLevel(@PathVariable(value = "id") long id, @Valid @RequestBody OxygenLevel ol) throws ResourceNotFoundException {
        Optional<Sensor> op = sensorRepository.findById(id);
        if(op.isPresent()){
            ol.setPatient(op.get().getPatient());
            return oxygenLevelRepository.save(ol);
        }
        throw new ResourceNotFoundException("Sensor not found for this id: " + id);
    }

    @PostMapping("/{id}/bodytemperature")
    public BodyTemperature createBodyTemperature(@PathVariable(value = "id") long id, @Valid @RequestBody BodyTemperature bt) throws ResourceNotFoundException {
        Optional<Sensor> op = sensorRepository.findById(id);
        if(op.isPresent()){
            bt.setPatient(op.get().getPatient());
            return bodyTemperatureRepository.save(bt);
        }
        throw new ResourceNotFoundException("Sensor not found for this id: " + id);
    }

    @PostMapping("/{id}/bloodpressure")
    public BloodPressure createBloodPressure(@PathVariable(value = "id") long id, @Valid @RequestBody BloodPressure bp) throws ResourceNotFoundException {
        Optional<Sensor> op = sensorRepository.findById(id);
        if(op.isPresent()){
            bp.setPatient(op.get().getPatient());
            return bloodPressureRepository.save(bp);
        }
        throw new ResourceNotFoundException("Sensor not found for this id: " + id);
    }

    @PostMapping("/{id}/heartrate")
    public HeartRate createHeartrate(@PathVariable(value = "id") long id, @Valid @RequestBody HeartRate hr) throws ResourceNotFoundException {
        Optional<Sensor> op = sensorRepository.findById(id);
        if(op.isPresent()){
            hr.setPatient(op.get().getPatient());
            return heartRateRepository.save(hr);
        }
        throw new ResourceNotFoundException("Sensor not found for this id: " + id);
    }
    
}
