package ies.g25.aLIVE.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ies.g25.aLIVE.model.Sensor;

public interface SensorRepository extends JpaRepository<Sensor, Long> {
    @Query("select id from Sensor")
    List<Long> getAllIds();
}
