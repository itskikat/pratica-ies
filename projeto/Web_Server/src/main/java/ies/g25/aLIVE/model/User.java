package ies.g25.aLIVE.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.ToString;

@Entity
@Table(name="USER")
@Data
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="user_type", discriminatorType = DiscriminatorType.STRING)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "password")
    //@JsonIgnore
    //@ToString.Exclude
    private String password;

    @Column(name = "username",unique = true)
    private String username;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "fullname")
    private String fullname;

    @Column(name = "age")
    private int age;

    @Column(name = "gender")
    private String gender;

    @Lob
    @Column(name="picture",  columnDefinition="mediumblob")
    private byte[] image;

    public User(){

    }
    
    public User(String password, String username, String email, String fullname, int age, String gender) {
        this.password = password;
        this.username = username;
        this.email = email;
        this.fullname = fullname;
        this.age = age;
        this.gender = gender;
        this.image= Constants.NO_PHOTO;
    }

    public String getEmail(){
        return this.email;
    }

    public void setEmail(String email){
        this.email=email;
    } 

    public int getAge(){
        return this.age;
    }
    public void setAge(int age){
        this.age=age;
    } 

    public String getRole(){
        return this.getClass().getAnnotation(DiscriminatorValue.class).value(); 
    }
    public String getFullname(){
        return this.fullname;
    }
    
    public void setFullname(String fullname){
        this.fullname=fullname;
    } 

    public String getGender(){
        return this.gender;
    }
    
    public void setGender(String gender){
        this.gender=gender;
    } 

    public Long getId(){
        return this.id;
    }
    public String getUsername(){
        return this.username;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public void setImage(byte[] img){
        this.image = img;
    }

    public byte[] getImage(){
        return this.image;
    }
    
    public String getPassword(){
    	return this.password;
    }

    public void setPassword(String password) { this.password = password; }
}


