//data de recolha, id paciente, mg/dL value
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
@Table(name="SUGAR_LEVEL")
public class SugarLevel{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    // date of the mesure
    @Column(name="date")
    @CreationTimestamp
    private LocalDateTime date;

    // Sugar level values
    @Column(name="sugar_level")
    private double sugar_level;

    //Patient Id
    @ManyToOne()
    @JsonIgnore
    @ToString.Exclude
    @JoinColumn(name = "patient_id")
    private Patient patient;

    public SugarLevel(){}

    public SugarLevel(double sugar_level, Patient patient){
        this.sugar_level=sugar_level;
        this.patient=patient;
    }

    public void setSugarLevel(double sugar_level){
        this.sugar_level=sugar_level;
    }
    public double getSugarLevel(){
        return this.sugar_level;
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