package ies.g25.aLIVE.repository;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ies.g25.aLIVE.model.OxygenLevel;
import ies.g25.aLIVE.model.Patient;

@Repository
public interface OxygenLevelRepository extends JpaRepository<OxygenLevel, Long>{

    @Query("select s from OxygenLevel s where s.date > :start_date and s.date<:end_date and patient.id=:patientID")
    Page<OxygenLevel> findByPatientandDate(@Param("start_date") LocalDateTime start_date, @Param("end_date") LocalDateTime end_date, @Param("patientID") Long id, Pageable pageable);
    
    Page<OxygenLevel> findByPatient(Patient p, Pageable pageable);

    OxygenLevel findFirstByPatient(Patient p, Sort sort);
}