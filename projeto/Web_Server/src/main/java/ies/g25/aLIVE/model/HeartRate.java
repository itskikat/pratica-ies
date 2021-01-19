//data de recolha, id paciente, beats per minute
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
@Table(name="HEART_RATE")
public class HeartRate{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    // date of the mesure
    @Column(name="date")
    @CreationTimestamp
    private LocalDateTime date;

    // Hert Rate values
    @Column(name="heartRate",nullable = false)
    private int heartRate;

    //Patient Id
    @ManyToOne()
    @JsonIgnore
    @ToString.Exclude
    @JoinColumn(name = "patient")
    private Patient patient;

    public HeartRate(){}

    public HeartRate(int heart_rate, Patient patient){
        this.heartRate=heart_rate;
        this.patient=patient;
    }

    public void setHeartRate(int heart_rate){
        this.heartRate=heart_rate;
    }
    public int getHeartRate(){
        return this.heartRate;
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