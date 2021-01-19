package ies.g25.aLIVE.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


@Entity
@Data
@DiscriminatorValue("Patient")
public class Patient extends User {

    @Column(name = "lastcheck")
    @Temporal(TemporalType.DATE)
    private Date lastcheck;

    @Column(name = "currentstate")
    private String currentstate;

    @Column(name = "med_conditions")
    private ArrayList<String> med_conditions = new ArrayList<>();

    @Column(name = "medication")
    private ArrayList<String> medication = new ArrayList<>(); //dic medicamento:quantidade?

    @Column(name = "weight")
    private double weight;

    @Column(name = "height")
    private double height;



    //Assigned professional
    @ManyToOne
    @JoinColumn(name = "professional_id")
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Professional professional;

    @OneToMany(mappedBy = "patient")
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<BloodPressure> bloodPressure;

    @OneToMany(mappedBy = "patient")
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<SugarLevel> sugarLevel;

    @OneToMany(mappedBy = "patient")
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<OxygenLevel> oxygenLevel;

    @OneToMany(mappedBy = "patient")
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<HeartRate> heartRate;

    @OneToMany(mappedBy = "patient")
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<BodyTemperature> bodyTemp;

    @OneToMany(mappedBy = "patient")
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Sensor> sensors;
    
    public Patient(){

    }

    public Patient(String password, String username, String email, String fullname, 
    int age, String gender, Date lastcheck, String currentstate, double height, double weight) {
        super(password, username, email, fullname, age, gender);
        this.lastcheck = lastcheck;
        this.currentstate = currentstate;
        this.height = height;
        this.weight = weight;
    }

    public void addCondition(String cond){
        this.med_conditions.add(cond);
    }

    public void removeCondition(String cond){
        this.med_conditions.remove(cond);
    }

    public void addMedication(String med){
        this.medication.add(med);
    }

    public void removeMedication(String med){
        this.medication.remove(med);
    }

    public String getCurrentstate(){
        return this.currentstate;
    }

    public void setCurrentstate(String currentstate){
        this.currentstate=currentstate;
    }

    public Professional getProfessional(){
        return professional;
    }

    public void setProfessional(Professional professional){
        this.professional=professional;
    }

    public String getUsername(){
        return super.getUsername();
    }

    public String getPassword(){
        return super.getPassword();
    }

    public void setUsername(String username){
        super.setUsername(username);
    }

    public String getFullname(){
        return super.getFullname();
    }

    public void setFullname(String fullname){
        super.setFullname(fullname);
    }

    public int getAge(){
        return super.getAge();
    }

    public void setAge(int age){
        super.setAge(age);
    }

    public String getEmail(){
        return super.getEmail();
    }

    public void setEmail(String email){
        super.setEmail(email);
    }

    public double getWeight(){
        return this.weight;
    }

    public void setWeight(double weight){
        this.weight=weight;
    }

    public double getHeight(){
        return this.height;
    }

    public void setHeight(double height){
        this.height=height;
    }

    public Date getLastCheck(){
        return this.lastcheck;
    }

    public void setLastCheck(Date lastcheck){
        this.lastcheck=lastcheck;
    }

}