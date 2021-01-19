package ies.g25.aLIVE.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.CreationTimestamp;

import lombok.ToString;

@Entity
@Table(name="OXYGEN_LEVEL")
public class OxygenLevel{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    // date of the mesure
    @Column(name="date")
    @CreationTimestamp
    private LocalDateTime date;

    // Oxygen level percentage values
    @Column(name="oxygen_level",nullable = false)
    private double oxygen_level;

    //Patient Id
    @ManyToOne()
    @JsonIgnore
    @ToString.Exclude
    @JoinColumn(name = "patient_id")
    private Patient patient;

    public OxygenLevel(){}

    public OxygenLevel(double oxygen_level, Patient patient){
        this.oxygen_level=oxygen_level;
        this.patient=patient;
    }

    public void setOxygenLevel(double oxygen_level){
        this.oxygen_level=oxygen_level;
    }
    public double getOxygenLevel(){
        return this.oxygen_level;
    }

    public LocalDateTime getDate(){
        return this.date;
    }
    public void setDate(LocalDateTime date){
        this.date=date;
    }
    public Patient getPatient(){
        return this.patient;
    }
    public void setPatient(Patient patient){
        this.patient=patient;
    }
}