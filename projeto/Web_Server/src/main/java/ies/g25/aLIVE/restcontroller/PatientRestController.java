package ies.g25.aLIVE.restcontroller;

import java.beans.FeatureDescriptor;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ies.g25.aLIVE.exception.ResourceNotFoundException;
import ies.g25.aLIVE.exception.UnprocessableEntityException;
import ies.g25.aLIVE.model.BloodPressure;
import ies.g25.aLIVE.model.BodyTemperature;
import ies.g25.aLIVE.model.HeartRate;
import ies.g25.aLIVE.model.OxygenLevel;
import ies.g25.aLIVE.model.Patient;
import ies.g25.aLIVE.model.PatientContext;
import ies.g25.aLIVE.model.Professional;
import ies.g25.aLIVE.model.SugarLevel;
import ies.g25.aLIVE.model.User;
import ies.g25.aLIVE.model.Sensor;
import ies.g25.aLIVE.repository.BloodPressureRepository;
import ies.g25.aLIVE.repository.BodyTemperatureRepository;
import ies.g25.aLIVE.repository.HeartRateRepository;
import ies.g25.aLIVE.repository.OxygenLevelRepository;
import ies.g25.aLIVE.repository.PatientRepository;
import ies.g25.aLIVE.repository.ProfessionalRepository;
import ies.g25.aLIVE.repository.SensorRepository;
import ies.g25.aLIVE.repository.SugarLevelRepository;
import ies.g25.aLIVE.repository.UserRepository;

@RestController
@RequestMapping("/api/patients")
public class PatientRestController {
    // get users, get health info from a user

    @Autowired
    public PatientRepository patientRepository;

    public HeartRateRepository heartRateRepository;
    public SugarLevelRepository sugarLevelRepository;
    public BloodPressureRepository bloodPressureRepository;
    public BodyTemperatureRepository bodyTemperatureRepository;
    public ProfessionalRepository professionalRepository;
    public OxygenLevelRepository oxygenLevelRepository;
    public UserRepository userRepository;
    public SensorRepository sensorRepository;

    @Autowired
    public PasswordEncoder passwordEncoder;


    public PatientRestController(PatientRepository patientRepository, HeartRateRepository heartRateRepository,
            SugarLevelRepository sugarLevelRepository, OxygenLevelRepository oxygenLevelRepository, 
            BloodPressureRepository bloodPressureRepository, BodyTemperatureRepository bodyTemperatureRepository, 
            ProfessionalRepository professionalRepository, UserRepository userRepository, SensorRepository sensorRepository) {
        this.patientRepository = patientRepository;
        this.heartRateRepository = heartRateRepository;
        this.sugarLevelRepository = sugarLevelRepository;
        this.oxygenLevelRepository = oxygenLevelRepository;
        this.bloodPressureRepository = bloodPressureRepository;
        this.bodyTemperatureRepository = bodyTemperatureRepository;
        this.professionalRepository = professionalRepository;
        this.userRepository= userRepository;
        this.sensorRepository = sensorRepository;
    }

    @GetMapping(produces="application/json")
    @ResponseBody
    public List<Patient> getAllPatients(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();

        if (principal.getName().equals("admin")){
            return patientRepository.findAll();
        } throw new AccessDeniedException("Cannot access this resource");

    }


    @PostMapping(produces="application/json", consumes="application/json")
    public Patient createPatient(@Valid @RequestBody PatientContext patient)throws ResourceNotFoundException,Exception, UnprocessableEntityException {
        Optional<Professional> p = professionalRepository.findByEmail(patient.getPemail());
        if(p.isPresent()){
            try{
                Patient pat = patient.getPatient();
                System.out.println(pat.getUsername());
                pat.setProfessional(p.get());
                Optional<User> u1 = userRepository.findByEmail(pat.getEmail());
                Optional<User> u2 = userRepository.findByUsername(pat.getUsername());
                if(u2.isPresent()){
                    throw new UnprocessableEntityException("Username already exists");
                }
                else if(u1.isPresent()){
                    throw new UnprocessableEntityException("Email already exists");
                }
                pat.setPassword(passwordEncoder.encode(pat.getPassword()));
                
                patientRepository.save(pat);

                Sensor s1 =new Sensor(pat);
                sensorRepository.save(s1);
                return pat;
            }
            catch (Exception e) {
                throw new Exception(e.getMessage() + " entrou");
            }
            
        }
        throw new ResourceNotFoundException("Professional not found for this email");
    }

    @PutMapping(value="/{id}",produces="application/json", consumes="application/json")
    @PreAuthorize("hasRole('Patient')")
    public Patient replacePatient(@RequestBody Patient newPatient, @PathVariable(value = "id") Long patientId, HttpServletRequest request)
            throws ResourceNotFoundException,UnprocessableEntityException{

        Principal principal = request.getUserPrincipal();
        Optional<Patient> op1 = patientRepository.findByUsername(principal.getName());
        Optional<Professional> op2 = professionalRepository.findByUsername(principal.getName());

        if(op1.isPresent()){
            Patient p = op1.get();
            if (principal.getName().equals("admin") || p.getId()==patientId) {
                Optional<User> u1 = userRepository.findByEmail(newPatient.getEmail());
                Optional<User> u2 = userRepository.findByUsername(newPatient.getUsername());
                if(u2.isPresent() && u2.get().getUsername()!=p.getUsername()){
                    throw new UnprocessableEntityException("Username already exists");
                }
                else if(u1.isPresent() && u1.get().getUsername()!=p.getUsername()){
                    throw new UnprocessableEntityException("Email already exists");
                }
                Optional<Patient> op = patientRepository.findById(patientId);
                if (op.isPresent()) {
                    Patient patient = op.get();

                    if(newPatient.getPassword() != null)
                        newPatient.setPassword(passwordEncoder.encode(newPatient.getPassword()));
                    
                    newPatient = (Patient) PersistenceUtils.partialUpdate(patient, newPatient);
                    
                    return patientRepository.save(newPatient);
                }
                throw new ResourceNotFoundException("Patient not found for this id: " + patientId);
    
            } throw new AccessDeniedException("Cannot access this resource");
        }
        else{
            Professional p = op2.get();
            Optional<Patient> op = patientRepository.findById(patientId);
            if (op.isPresent()) {
                Patient patient = op.get();

                newPatient = (Patient) PersistenceUtils.partialUpdate(patient, newPatient);
                
            }
            else{
                throw new ResourceNotFoundException("Patient not found for this id: " + patientId);
            }
            if(newPatient.getProfessional()==p){
                
                return patientRepository.save(newPatient);
            }
            throw new AccessDeniedException("Cannot access this resource");
        }
        

        

    }



    @GetMapping(value="/{id}", produces = "application/json")
    public ResponseEntity<Patient> getPatientById(@PathVariable(value = "id") Long patientId, HttpServletRequest request)
            throws ResourceNotFoundException {

        Boolean checkPermission = checkUserPermissions(request, patientId);

        if (!checkPermission){
            throw new AccessDeniedException("Cannot access this resource");
        }

        Optional<Patient> op = patientRepository.findById(patientId);
        if (op.isPresent()) {
            Patient patient = op.get();
            return ResponseEntity.ok().body(patient);
        }
        throw new ResourceNotFoundException("Patient not found for this id: " + patientId);

    }

    @PostMapping(value="/{id}/picture", produces="application/json")
    @PreAuthorize("hasRole('Patient')")
    public Patient updatePhoto(@PathVariable(value = "id") Long patientId, @RequestParam("file") MultipartFile file, HttpServletRequest request)
            throws ResourceNotFoundException, IOException {

        Principal principal = request.getUserPrincipal();
        Optional<Patient> op1 = patientRepository.findByUsername(principal.getName());
        Patient p = op1.get();

        if (principal.getName().equals("admin") || p.getId()==patientId) {
            Optional<Patient> op=  patientRepository.findById(patientId);
            if(op.isPresent()){
                Patient patient = op.get();
                byte[] data = file.getBytes();
                patient.setImage(data);
                patientRepository.save(patient);
                return patient;
            }
            throw new ResourceNotFoundException("Patient not found for this id: " + patientId);

        } throw new AccessDeniedException("Cannot access this resource");
    }
    
    @GetMapping("{id}/bloodpressure")
    public ResponseEntity<Map<String, Object>> getBloodPressureByIdAndDate(@PathVariable(value = "id") Long patientId,
        @RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "10") int size,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start_date, @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end_date      
         , HttpServletRequest request) throws ResourceNotFoundException {

        Boolean checkPermission = checkUserPermissions(request, patientId);

        if (!checkPermission){
            throw new AccessDeniedException("Cannot access this resource");
        }

        List<BloodPressure> bloodPressures;
        Pageable paging = PageRequest.of(page, size,Sort.by(Sort.Direction.DESC, "date"));
        Page<BloodPressure> bp;

        try {
            if (start_date != null &&  end_date!=null){
                bp= bloodPressureRepository.findByPatientandDate(start_date,end_date, patientId, paging);
            }
            else{
                Optional<Patient> op = patientRepository.findById(patientId);
                if (op.isPresent()) {
                    Patient p = op.get();
                    bp = bloodPressureRepository.findByPatient(p, paging);
                }
                else{
                    throw new ResourceNotFoundException("Patient not found for this id: " + patientId);
                }
            }
            bloodPressures = bp.getContent();
            Map<String, Object> response = new HashMap<>();
            response.put("data", bloodPressures);
            response.put("currentPage", bp.getNumber());
            response.put("totalItems", bp.getTotalElements());
            response.put("totalPages", bp.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);
            

        } catch (Exception e) {
            throw new ResourceNotFoundException(e.getMessage()+e.getLocalizedMessage());
        } 

    }

    @GetMapping("{id}/heartrate")
    public ResponseEntity<Map<String, Object>> getHeartRateByIdAndDate(@PathVariable(value = "id") Long patientId,
        @RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "10") int size,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start_date, @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end_date      
        , HttpServletRequest request ) throws ResourceNotFoundException {

        Boolean checkPermission = checkUserPermissions(request, patientId);

        if (!checkPermission){
            throw new AccessDeniedException("Cannot access this resource");
        }

        List<HeartRate> heartRates;
        Pageable paging = PageRequest.of(page, size,Sort.by(Sort.Direction.DESC, "date"));
        Page<HeartRate> hr;

        try {
            if (start_date != null &&  end_date!=null){
                hr= heartRateRepository.findByPatientandDate(start_date,end_date, patientId, paging);
            }
            else{
                Optional<Patient> op = patientRepository.findById(patientId);
                if (op.isPresent()) {
                    Patient p = op.get();
                    hr = heartRateRepository.findByPatient(p, paging);
                }
                else{
                    throw new ResourceNotFoundException("Patient not found for this id: " + patientId);
                }
            }
            heartRates = hr.getContent();
            Map<String, Object> response = new HashMap<>();
            response.put("data", heartRates);
            response.put("currentPage", hr.getNumber());
            response.put("totalItems", hr.getTotalElements());
            response.put("totalPages", hr.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);
            

        } catch (Exception e) {
            throw new ResourceNotFoundException(e.getLocalizedMessage());
        } 

    }

    @GetMapping("{id}/sugarlevel")
    public ResponseEntity<Map<String, Object>> getSugarLevelByIdAndDate(@PathVariable(value = "id") Long patientId,
        @RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "10") int size,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start_date, @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end_date      
        , HttpServletRequest request) throws ResourceNotFoundException {

        Boolean checkPermission = checkUserPermissions(request, patientId);

        if (!checkPermission){
            throw new AccessDeniedException("Cannot access this resource");
        }

        List<SugarLevel> sugarlevels;
        Pageable paging = PageRequest.of(page, size,Sort.by(Sort.Direction.DESC, "date"));
        Page<SugarLevel> sl;

        try {
            if (start_date != null &&  end_date!=null){
                sl= sugarLevelRepository.findByPatientandDate(start_date,end_date, patientId, paging);
            }
            else{
                Optional<Patient> op = patientRepository.findById(patientId);
                if (op.isPresent()) {
                    Patient p = op.get();
                    sl = sugarLevelRepository.findByPatient(p, paging);
                }
                else{
                    throw new ResourceNotFoundException("Patient not found for this id: " + patientId);
                }
            }
            sugarlevels = sl.getContent();
            Map<String, Object> response = new HashMap<>();
            response.put("data", sugarlevels);
            response.put("currentPage", sl.getNumber());
            response.put("totalItems", sl.getTotalElements());
            response.put("totalPages", sl.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);
            

        } catch (Exception e) {
            throw new ResourceNotFoundException(e.getMessage()+e.getLocalizedMessage());
        }
    }
    
    @GetMapping("{id}/oxygenlevel")
    public ResponseEntity<Map<String, Object>> getOxygenLevelByIdAndDate(@PathVariable(value = "id") Long patientId,
        @RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "10") int size,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start_date, @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end_date      
        , HttpServletRequest request) throws ResourceNotFoundException {

        Boolean checkPermission = checkUserPermissions(request, patientId);

        if (!checkPermission){
            throw new AccessDeniedException("Cannot access this resource");
        }

        List<OxygenLevel> oxygenlevels;
        Pageable paging = PageRequest.of(page, size,Sort.by(Sort.Direction.DESC, "date"));
        Page<OxygenLevel> ol;

        try {
            if (start_date != null &&  end_date!=null){
                ol = oxygenLevelRepository.findByPatientandDate(start_date,end_date, patientId, paging);
            }
            else{
                Optional<Patient> op = patientRepository.findById(patientId);
                if (op.isPresent()) {
                    Patient p = op.get();
                    ol = oxygenLevelRepository.findByPatient(p, paging);
                }
                else{
                    throw new ResourceNotFoundException("Patient not found for this id: " + patientId);
                }
            }
            oxygenlevels = ol.getContent();
            Map<String, Object> response = new HashMap<>();
            response.put("data", oxygenlevels);
            response.put("currentPage", ol.getNumber());
            response.put("totalItems", ol.getTotalElements());
            response.put("totalPages", ol.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);
            

        } catch (Exception e) {
            throw new ResourceNotFoundException(e.getMessage()+e.getLocalizedMessage());
        }
    }


    @GetMapping("{id}/bodytemperature")
    public ResponseEntity<Map<String, Object>> getBodyTemperatureByIdAndDate(@PathVariable(value = "id") Long patientId,
        @RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "10") int size,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start_date, @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end_date      
        , HttpServletRequest request) throws ResourceNotFoundException {

        Boolean checkPermission = checkUserPermissions(request, patientId);

        if (!checkPermission){
            throw new AccessDeniedException("Cannot access this resource");
        }

        List<BodyTemperature> bodyTemperatures;
        Pageable paging = PageRequest.of(page, size,Sort.by(Sort.Direction.DESC, "date"));
        Page<BodyTemperature> bt;

        try {
            if (start_date != null &&  end_date!=null){
                bt= bodyTemperatureRepository.findByPatientandDate(start_date,end_date, patientId, paging);
            }
            else{
                Optional<Patient> op = patientRepository.findById(patientId);
                if (op.isPresent()) {
                    Patient p = op.get();
                    bt = bodyTemperatureRepository.findByPatient(p, paging);
                }
                else{
                    throw new ResourceNotFoundException("Patient not found for this id: " + patientId);
                }
            }
            bodyTemperatures = bt.getContent();
            Map<String, Object> response = new HashMap<>();
            response.put("data", bodyTemperatures);
            response.put("currentPage", bt.getNumber());
            response.put("totalItems", bt.getTotalElements());
            response.put("totalPages", bt.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);
            

        } catch (Exception e) {
            throw new ResourceNotFoundException(e.getMessage()+e.getLocalizedMessage());
        }
    }


    @GetMapping("/{pid}/latest")
    @ResponseBody
    public List<Object> getLatestValues(@PathVariable(value = "pid") long pid) throws ResourceNotFoundException {
        Sort sort = Sort.by("date").ascending().descending();
        Optional<Patient> op=patientRepository.findById(pid);
        if(op.isPresent()){
            List<Object> list = new ArrayList<Object>();
            Patient p = op.get();
            list.add(bloodPressureRepository.findFirstByPatient(p, sort));
            list.add(bodyTemperatureRepository.findFirstByPatient(p, sort));
            list.add(heartRateRepository.findFirstByPatient(p, sort));
            list.add(sugarLevelRepository.findFirstByPatient(p, sort));
            list.add(oxygenLevelRepository.findFirstByPatient(p, sort));

             return list;
        }
        throw new ResourceNotFoundException("Patient not found for this id: " + pid);
    }


    public static class PersistenceUtils {

        public static Object partialUpdate(Object dbObject, Object partialUpdateObject){
            String[] ignoredProperties = getNullPropertyNames(partialUpdateObject);
            BeanUtils.copyProperties(partialUpdateObject, dbObject, ignoredProperties);
            return dbObject;
        }
    
        private static String[] getNullPropertyNames(Object object) {
            final BeanWrapper wrappedSource = new BeanWrapperImpl(object);
            return Stream.of(wrappedSource.getPropertyDescriptors())
                    .map(FeatureDescriptor::getName)
                    .filter(propertyName -> wrappedSource.getPropertyValue(propertyName) == null)
                    .toArray(String[]::new);
        }
    
    
    }


    public Boolean  checkUserPermissions(HttpServletRequest request, Long patientId){

        Boolean flag = true;

        // get request user
        Principal principal = request.getUserPrincipal();
        // try to get professional or patient from user
        Optional<Patient> op1_pat = patientRepository.findByUsername(principal.getName());
        Optional<Professional> op1_prof = professionalRepository.findByUsername(principal.getName());

        // if user is a patient
        if (op1_pat.isPresent()){
            Patient patient = op1_pat.get();
            if (patient.getId()!=patientId) {
                flag = false;
            }
        }
        // if user is a professional
        else if (op1_prof.isPresent()){
            Optional<Patient> op = patientRepository.findById(patientId);
            Patient patient = op.get();
            Professional prof = patient.getProfessional();
            if (op1_prof.get().getId() != prof.getId()) {
                flag = false;
            }

        }
        // if is not either, check if is admin
        else if (!principal.getName().equals("admin")) {
            flag = false;
        }

        return flag;
    }

}