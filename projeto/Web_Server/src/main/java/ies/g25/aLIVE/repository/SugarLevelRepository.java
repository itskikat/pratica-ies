package ies.g25.aLIVE.repository;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ies.g25.aLIVE.model.Patient;
import ies.g25.aLIVE.model.SugarLevel;

@Repository
public interface SugarLevelRepository extends JpaRepository<SugarLevel, Long>{

    @Query("select s from SugarLevel s where s.date > :start_date and s.date<:end_date and patient.id=:patientID")
    Page<SugarLevel> findByPatientandDate(@Param("start_date") LocalDateTime start_date, @Param("end_date") LocalDateTime end_date, @Param("patientID") Long id, Pageable pageable);
    
    Page<SugarLevel> findByPatient(Patient p, Pageable pageable);

    SugarLevel findFirstByPatient(Patient p, Sort sort);
}