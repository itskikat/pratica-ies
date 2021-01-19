package ies.g25.aLIVE.restcontroller;

import java.beans.FeatureDescriptor;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
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
import ies.g25.aLIVE.model.Patient;
import ies.g25.aLIVE.model.Professional;
import ies.g25.aLIVE.model.User;
import ies.g25.aLIVE.repository.PatientRepository;
import ies.g25.aLIVE.repository.ProfessionalRepository;
import ies.g25.aLIVE.repository.UserRepository;
import springfox.documentation.spring.web.paths.Paths;

@RestController
@RequestMapping("/api/professionals")
public class ProfessionalRestController {
    // get users, get health info from a user

    @Autowired
    public PatientRepository patientRepository;

    @Autowired
    public PasswordEncoder passwordEncoder;

    @Autowired
    public ProfessionalRepository professionalRepository;

    @Autowired
    public UserRepository userRepository;

    public ProfessionalRestController(PatientRepository patientRepository, ProfessionalRepository professionalRepository, UserRepository userRepository) {
        this.patientRepository = patientRepository;
        this.professionalRepository=professionalRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    @ResponseBody
    public List<Professional> getAllProfessionals(HttpServletRequest request) {

        Principal principal = request.getUserPrincipal();

        if (principal.getName().equals("admin")){
            return professionalRepository.findAll();
        } throw new AccessDeniedException("Cannot access this resource");
    }

    @PostMapping
    public Professional createProfessional(@Valid @RequestBody Professional professional, HttpServletRequest request) throws UnprocessableEntityException, Exception{
        try{
            Optional<User> u1 = userRepository.findByEmail(professional.getEmail());
            Optional<User> u2 = userRepository.findByUsername(professional.getUsername());
            if(u2.isPresent()){
                throw new UnprocessableEntityException("Username already exists");
            }
            else if(u1.isPresent()){
                throw new UnprocessableEntityException("Email already exists");
            }
            professional.setPassword(passwordEncoder.encode(professional.getPassword()));
            return professionalRepository.save(professional);
        }
        catch (Exception e) {
            throw e;
        }
        
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('Professional')")
    public Professional replaceProfessional(@RequestBody Professional newProfessional, @PathVariable(value = "id") Long professionalId, HttpServletRequest request)
            throws ResourceNotFoundException, UnprocessableEntityException{

        Principal principal = request.getUserPrincipal();
        Optional<Professional> op1 = professionalRepository.findByUsername(principal.getName());
        Professional p = op1.get();

        Optional<Professional> op = professionalRepository.findById(professionalId);


        if (principal.getName().equals("admin") || p.getId()==professionalId){
            if (op.isPresent()) {
                Optional<User> u1 = userRepository.findByEmail(newProfessional.getEmail());
                Optional<User> u2 = userRepository.findByUsername(newProfessional.getUsername());
                if(u2.isPresent() && u2.get().getUsername()!=p.getUsername()){
                    throw new UnprocessableEntityException("Username already exists");
                }
                else if(u1.isPresent() && u1.get().getUsername()!=p.getUsername()){
                    throw new UnprocessableEntityException("Email already exists");
                }
                Professional professional = op.get();
                if(newProfessional.getPassword() != null)
                    newProfessional.setPassword(passwordEncoder.encode(newProfessional.getPassword()));

                newProfessional = (Professional) PersistenceUtils.partialUpdate(professional, newProfessional);
                
                return professionalRepository.save(newProfessional);
            }
            throw new ResourceNotFoundException("Professional not found for this id: " + professionalId);
        }
        throw new AccessDeniedException("Cannot access this resource");


    }

    @PostMapping("/{id}/picture")
    @PreAuthorize("hasRole('Professional')")
    public Professional updatePhoto(@PathVariable(value = "id") Long professionalId, @RequestParam("file") MultipartFile file, HttpServletRequest request)
            throws ResourceNotFoundException, IOException {

        Principal principal = request.getUserPrincipal();
        Optional<Professional> op1 = professionalRepository.findByUsername(principal.getName());
        Professional prof = op1.get();

        if (principal.getName().equals("admin") || prof.getId()==professionalId){
            Optional<Professional> op =  professionalRepository.findById(professionalId);
            if(op.isPresent()){
                Professional p = op.get();
                byte[] data = file.getBytes();
                p.setImage(data);
                professionalRepository.save(p);
                return p;
            }
            throw new ResourceNotFoundException("Professional not found for this id: " + professionalId);
        }
        throw new AccessDeniedException("Cannot access this resource");

    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('Professional')")
    public ResponseEntity<Professional> getProfessionalById(@PathVariable(value = "id") Long pID, HttpServletRequest request)
            throws ResourceNotFoundException, AccessDeniedException {
        Principal principal = request.getUserPrincipal();
        if (principal.getName().equals("admin")){
            Optional<Professional> op = professionalRepository.findById(pID);
            if (op.isPresent()) {
                return ResponseEntity.ok().body(op.get());
            }
            throw new ResourceNotFoundException("Professional not found for this id: " + pID);
        }
        Optional<Professional> op = professionalRepository.findByUsername(principal.getName());
        if (op.isPresent()) {
            Professional p = op.get();
            if(p.getId()==pID){
                return ResponseEntity.ok().body(p);
            }
            throw new AccessDeniedException("Cannot access this resource");
        }
        throw new AccessDeniedException("Cannot access this resource");
        
    }

    @GetMapping("/{id}/patients")
    @PreAuthorize("hasRole('Professional')")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> AllPatientsByProfessionalId(@PathVariable(value = "id") Long pId, @RequestParam(defaultValue = "0") 
        int page,@RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String name, @RequestParam(required = false) String state,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date start_date, @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date end_date, HttpServletRequest request)
            throws ResourceNotFoundException {

        Principal principal = request.getUserPrincipal();

        Optional<Professional> op = professionalRepository.findById(pId);

        List<Patient> patients;
        Pageable paging = PageRequest.of(page, size,Sort.by(Sort.Direction.DESC, "lastcheck"));
        Page<Patient> pt;

        if(op.isPresent()){
            Professional p = op.get();
            if(start_date!=null && end_date!=null){
                pt = patientRepository.findByProfessionalAndLastCheckDate(p, start_date,end_date, paging);
            }
            else if(name!=null){
                pt = patientRepository.findByProfessionalAndFullnameContaining(p, name, paging);
            }else if(state!=null){
                pt = patientRepository.findByProfessionalAndCurrentstate(p, state, paging);
            } else{
                pt = patientRepository.findByProfessional(p, paging);
            }
        }
        else{
            throw new ResourceNotFoundException("Professional not found for this id: " + pId);
        }
        patients = pt.getContent();
        Map<String, Object> response = new HashMap<>();
        response.put("data", patients);
        response.put("currentPage", pt.getNumber());
        response.put("totalItems", pt.getTotalElements());
        response.put("totalPages", pt.getTotalPages());


        Optional<Professional> op1 = professionalRepository.findByUsername(principal.getName());

        if (!principal.getName().equals("admin")){
            if (op1.isPresent()) {
                Professional p = op1.get();
                if(p.getId() == pId){
                    return new ResponseEntity<>(response, HttpStatus.OK);
                }
                throw new AccessDeniedException("Cannot access this resource");
            }
            throw new AccessDeniedException("Cannot access this resource");
        }
        else{
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

    }

    @PutMapping("/{id}/patients/{idpatient}")
    @PreAuthorize("hasRole('Professional')")
    @ResponseBody
    public Patient UpdateLastCheck(@PathVariable(value = "id") Long pId, @PathVariable(value = "idpatient") Long patId, HttpServletRequest request)
            throws ResourceNotFoundException {

        Principal principal = request.getUserPrincipal();

        Optional<Professional> op = professionalRepository.findById(pId);
        Optional<Professional> op1 = professionalRepository.findByUsername(principal.getName());
        Optional<Patient> opat = patientRepository.findById(patId);

        if (op.isPresent()) {
            if (opat.isPresent() & opat.get().getProfessional().getId() == pId) {
                Patient patient = opat.get();
                patient.setLastCheck(new Date());
                if (!principal.getName().equals("admin")) {
                    if (op1.isPresent()) {
                        Professional p = op1.get();
                        if (p.getId() == pId) {
                            return patientRepository.save(patient);
                        }
                        throw new AccessDeniedException("Cannot access this resource");
                    }
                    throw new AccessDeniedException("Cannot access this resource");
                } else {
                    return patientRepository.save(patient);
                }
            } else {
                throw new ResourceNotFoundException("Patient with id: " + patId + " not found for requested professional");
            }
        }

        throw new ResourceNotFoundException("Professional not found for this id: " + pId);
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
}
