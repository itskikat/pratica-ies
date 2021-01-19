package ies.g25.aLIVE.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ies.g25.aLIVE.model.BloodPressure;
import ies.g25.aLIVE.model.Patient;



@Repository
public interface BloodPressureRepository extends JpaRepository<BloodPressure, Long>{

    @Query("select b from BloodPressure b where b.date > :start_date and b.date<:end_date and patient.id=:patientID")
    Page<BloodPressure> findByPatientandDate(@Param("start_date") LocalDateTime start_date, @Param("end_date") LocalDateTime end_date, @Param("patientID") Long id, Pageable pageable);

    Page<BloodPressure> findByPatient(Patient p, Pageable pageable);

    BloodPressure findFirstByPatient(Patient p, Sort sort);
}
