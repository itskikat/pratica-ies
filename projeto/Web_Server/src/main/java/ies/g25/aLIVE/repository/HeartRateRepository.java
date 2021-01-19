package ies.g25.aLIVE.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ies.g25.aLIVE.model.HeartRate;
import ies.g25.aLIVE.model.Patient;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

@Repository
public interface HeartRateRepository extends JpaRepository<HeartRate, Long>{

    @Query("select h from HeartRate h where h.date > :start_date and h.date<:end_date and patient.id=:patientID")
    Page<HeartRate> findByPatientandDate(@Param("start_date") LocalDateTime start_date, @Param("end_date") LocalDateTime end_date, @Param("patientID") Long id, Pageable pageable);

    Page<HeartRate> findByPatient(Patient p, Pageable pageable);

    HeartRate findFirstByPatient(Patient p, Sort sort);
}