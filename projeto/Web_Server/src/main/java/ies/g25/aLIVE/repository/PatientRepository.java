package ies.g25.aLIVE.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ies.g25.aLIVE.model.User;
import ies.g25.aLIVE.model.Patient;
import ies.g25.aLIVE.model.Professional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;


@Repository
public interface PatientRepository extends JpaRepository<Patient, Long>{

    @Query("select p from Patient p where p.lastcheck > :start_date and p.lastcheck<:end_date and p.professional=:pID")
    Page<Patient> findByProfessionalAndLastCheckDate(@Param("pID") Professional pID,@Param("start_date") Date start_date, @Param("end_date") Date end_date, Pageable pageable);

    Page<Patient> findByProfessional(Professional professional, Pageable pageable);

    Page<Patient> findByProfessionalAndCurrentstate(Professional professional, String currentstate, Pageable pageable);

    Page<Patient> findByProfessionalAndFullnameContaining(Professional professional, String name, Pageable pageable);

    Optional<Patient> findByUsername(String username);
}
