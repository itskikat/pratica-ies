package ies.g25.aLIVE.model;

import java.time.LocalDate;
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
@Table(name="BOODY_TEMP")
public class BodyTemperature{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    // date of the mesure
    @Column(name="date")
    @CreationTimestamp
    private LocalDateTime date;

    // Blood Temperature values
    @Column(name = "bodytemp")
    private double bodyTemp;

    //Patient Id
    @ManyToOne()
    @JsonIgnore
    @ToString.Exclude
    @JoinColumn(name = "patient_id")
    private Patient patient;

    public BodyTemperature(){}

    public BodyTemperature(double bodytemp){
        this.bodyTemp=bodytemp;
    }

    public void setbodyTemp(double bodytemp){
        this.bodyTemp=bodytemp;
    }
    public double getbodyTemp(){
        return this.bodyTemp;
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
    public void setPatient(Patient p){
        this.patient=p;
    }
}