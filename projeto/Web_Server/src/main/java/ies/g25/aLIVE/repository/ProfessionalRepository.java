package ies.g25.aLIVE.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ies.g25.aLIVE.model.Professional;


@Repository
public interface ProfessionalRepository extends JpaRepository<Professional, Long>{
    Optional<Professional>  findByEmail(String email);

    Optional<Professional> findByUsername(String username);
}