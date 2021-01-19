package ies.g25.aLIVE;

import java.util.Date;
import java.util.Optional;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Timestamp;

import ies.g25.aLIVE.model.BloodPressure;
import ies.g25.aLIVE.model.BodyTemperature;
import ies.g25.aLIVE.model.HeartRate;
import ies.g25.aLIVE.model.OxygenLevel;
import ies.g25.aLIVE.model.Patient;
import ies.g25.aLIVE.model.Professional;
import ies.g25.aLIVE.model.Sensor;
import ies.g25.aLIVE.model.SugarLevel;
import ies.g25.aLIVE.model.User;
import ies.g25.aLIVE.repository.BloodPressureRepository;
import ies.g25.aLIVE.repository.BodyTemperatureRepository;
import ies.g25.aLIVE.repository.HeartRateRepository;
import ies.g25.aLIVE.repository.OxygenLevelRepository;
import ies.g25.aLIVE.repository.PatientRepository;
import ies.g25.aLIVE.repository.ProfessionalRepository;
import ies.g25.aLIVE.repository.SensorRepository;
import ies.g25.aLIVE.repository.SugarLevelRepository;
import ies.g25.aLIVE.repository.UserRepository;

import java.time.*;
import java.time.temporal.ChronoUnit;

@Configuration
class LoadDatabase {

  private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

  @Bean
  CommandLineRunner initDatabase(PatientRepository Prep, SensorRepository Srep, ProfessionalRepository Pro,
      PasswordEncoder passwordEncoder, BloodPressureRepository bloodPressureRepository,
      BodyTemperatureRepository bodyTemperatureRepository, HeartRateRepository heartRateRepository,
      OxygenLevelRepository oxygenLevelRepository, SugarLevelRepository sugarLevelRepository, UserRepository userRepository) {

    return args -> {

      //Optional<Patient> op = Prep.findById(Long.valueOf(23));
      //Optional<Professional> op1 = Pro.findById(Long.valueOf(24));
      //Patient p = op.get();
      //Professional prof = op1.get();
      //p.setProfessional(prof);
      //Prep.save(p);
      /*
      
      Professional p = new Professional(passwordEncoder.encode("pass"),"carlitos","cls@jmail.com","Carlitos Sousa",34,"Male","Hospital de Braga","Cardiologia","professional"); 
      Professional p2 = new Professional(passwordEncoder.encode("passita"),"anocas","anaaaa@jmail.com","Anocas Beatriz",44,"Female","Hospital de Aveiro","Medicina Interna","professional");
       
      Patient d = new Patient(passwordEncoder.encode("pass1"), "manel123","manel@jmail.com", "Manel Ribeiro",62,"Male", new Date(System.currentTimeMillis()-72*60*60*1000), "normal",1.75,67);
       
      d.addCondition("diabetes"); d.addCondition("obesity");
      d.addCondition("asthma"); d.setProfessional(p);
       
       
      Patient d1 = new Patient(passwordEncoder.encode("pass2"), "silvia69","silv@dmail.com", "Silvia Matos",74,"Female", new Date(System.currentTimeMillis()-24*60*60*1000), "in-danger", 1.66,70);
       
      d1.setProfessional(p); d1.addCondition("obesity");
       
      Patient d2 = new Patient(passwordEncoder.encode("pass3"), "ze321","ze@dmail.com", "Zé Matos", 75,"Male", new Date(), "normal",1.69,80);
       
      d2.setProfessional(p);

      Patient d3 = new Patient(passwordEncoder.encode("pass"), "ze123", "ze@jmail.com", "ze Ribeiro",89,"Male", new Date(System.currentTimeMillis()-48*60*60*1000), "normal",1.83,77);

      d3.setProfessional(p);
      d3.addCondition("diabetes");

      Patient d4 = new Patient(passwordEncoder.encode("pass"), "litas", "litas@jmail.com", "Litas",52,"Male", new Date(System.currentTimeMillis()-67*60*60*1000), "normal",1.8,100);

      d4.addCondition("diabetes");
      d4.addCondition("obesity");
      d4.setProfessional(p);
      

      Patient d5 = new Patient(passwordEncoder.encode("pass"), "gg", "gg@dmail.com", "Gonçalo Gonçalves",74,"Female", new Date(System.currentTimeMillis()-190*60*60*1000), "healthy", 1.69,90);

      d5.setProfessional(p);
      d5.addCondition("obesity");
      d5.addCondition("asthma");

      Patient d6 = new Patient(passwordEncoder.encode("pass"), "antonio123", "antonio@dmail.com", "Antonio Matos", 101,"Male", new Date(), "unhealthy",1.50,53);

      d6.setProfessional(p);

      Patient d7 = new Patient(passwordEncoder.encode("pass"), "lucia12", "lulu@jmail.com", "Lucia Ribeiro",31,"Female", new Date(System.currentTimeMillis()-78*60*60*1000), "normal",1.23,50);

      d7.setProfessional(p);


      Patient d8 = new Patient(passwordEncoder.encode("pass"), "georgina", "georgina@jmail.com", "Gigi Ribeiro",62,"Female", new Date(System.currentTimeMillis()-500*60*60*1000), "healthy",1.75,62);

      d8.addCondition("diabetes");
      d8.addCondition("obesity");
      d8.addCondition("asthma");
      d8.setProfessional(p);
      

      Patient d9 = new Patient(passwordEncoder.encode("pass"), "joaquim12", "joca@dmail.com", "Joaquim Matos",95,"Male", new Date(System.currentTimeMillis()-22*60*60*1000), "in-danger", 1.78,60);

      d9.setProfessional(p);
      d9.addCondition("asthma");

      Patient d10 = new Patient(passwordEncoder.encode("pass"), "rita", "ritinha@dmail.com", "Rita Matos", 75,"Female", new Date(), "normal",2.10,110);

      d10.setProfessional(p);

      Patient d11 = new Patient(passwordEncoder.encode("pass"), "gregorio", "grego@jmail.com", "Gregorio Ribeiro",99,"Male", new Date(System.currentTimeMillis()-88*60*60*1000), "unhealthy",1.83,77);

      d11.setProfessional(p);
      d11.addCondition("diabetes");

      Patient d12 = new Patient(passwordEncoder.encode("pass"), "manel65", "manel65@jmail.com", "Manel Lopes Ribeiro",69,"Male", new Date(System.currentTimeMillis()-89*60*60*1000), "normal",1.75,63);

      d12.addCondition("diabetes");
      d12.addCondition("asthma");
      d12.setProfessional(p);
      

      Patient d13 = new Patient(passwordEncoder.encode("pass"), "sandrax", "sandrax@dmail.com", "Sandrx Matos",73,"Female", new Date(System.currentTimeMillis()-37*60*60*1000), "healthy", 1.66,76);

      d13.setProfessional(p);
      d13.addCondition("obesity");

      Patient d14 = new Patient(passwordEncoder.encode("pass"), "jony", "jony@dmail.com", "João Matos", 57,"Male", new Date(), "healthy",1.89,80);

      d14.setProfessional(p);

      Patient d15 = new Patient(passwordEncoder.encode("pass"), "vitorino", "vito@jmail.com", "Vito Ribeiro",86,"Male", new Date(System.currentTimeMillis()-300*60*60*1000), "normal",1.56,77);

      d15.setProfessional(p2);
      d15.addCondition("diabetes");
      d15.addCondition("obesity");

      Patient d16 = new Patient(passwordEncoder.encode("pass"), "anita", "anita@jmail.com", "Ana Sá Ribeiro",57,"Female", new Date(System.currentTimeMillis()-210*60*60*1000), "normal",1.75,67);


      d16.addCondition("asthma");
      d16.setProfessional(p2);
      

      Patient d17 = new Patient(passwordEncoder.encode("pass"), "mimosa", "mumu@dmail.com", "Mimosa Matos",44,"Female", new Date(System.currentTimeMillis()-21*60*60*1000), "in-danger", 1.63,80);

      d17.setProfessional(p2);
      d17.addCondition("obesity");

      Patient d18 = new Patient(passwordEncoder.encode("pass"), "zira", "zira@dmail.com", "Alzira Matos", 70,"Female", new Date(), "unhealthy",1.59,45);

      d18.setProfessional(p2);

      Patient d19 = new Patient(passwordEncoder.encode("pass"), "manel70", "ohmanel@jmail.com", "Manel Silva",110,"Male", new Date(System.currentTimeMillis()-20*60*60*1000), "normal",1.70,70);

      d19.setProfessional(p2);
       
       
      log.info("Preloading " + Pro.save(p));
      log.info("Preloading " + Pro.save(p2));
      log.info("Preloading " + Prep.save(d));
      log.info("Preloading " + Prep.save(d1));    
      log.info("Preloading " + Prep.save(d2));
      log.info("Preloading " + Prep.save(d3));
      log.info("Preloading " + Prep.save(d4));
      log.info("Preloading " + Prep.save(d5));    
      log.info("Preloading " + Prep.save(d6));
      log.info("Preloading " + Prep.save(d7));
      log.info("Preloading " + Prep.save(d8));
      log.info("Preloading " + Prep.save(d9));    
      log.info("Preloading " + Prep.save(d10));
      log.info("Preloading " + Prep.save(d11));
      log.info("Preloading " + Prep.save(d12));
      log.info("Preloading " + Prep.save(d13));    
      log.info("Preloading " + Prep.save(d14));
      log.info("Preloading " + Prep.save(d15));
      log.info("Preloading " + Prep.save(d16));
      log.info("Preloading " + Prep.save(d17));    
      log.info("Preloading " + Prep.save(d18));
      log.info("Preloading " + Prep.save(d19));


      Sensor s1 =new Sensor(d); 
      Sensor s2 = new Sensor(d1);
      Sensor s3 = new Sensor(d2); 
      Sensor s4 = new Sensor(d3); 
      Sensor s5 =new Sensor(d4);
      Sensor s6 = new Sensor(d5); 
      Sensor s7 = new Sensor(d6);
      Sensor s8 = new Sensor(d7);
      Sensor s9 =new Sensor(d8);
      Sensor s10 = new Sensor(d9);
      Sensor s11 = new Sensor(d10); 
      Sensor s12 = new Sensor(d11); 
      Sensor s13 =new Sensor(d12);
      Sensor s14 = new Sensor(d13); 
      Sensor s15 = new Sensor(d14); 
      Sensor s16 = new Sensor(d15); 
      Sensor s17 =new Sensor(d16); 
      Sensor s18 = new Sensor(d17);
      Sensor s19 = new Sensor(d18); 
      Sensor s20 = new Sensor(d19);
      
      log.info("Preloading " + Srep.save(s1)); 
      log.info("Preloading " + Srep.save(s2)); 
      log.info("Preloading " + Srep.save(s3));
      log.info("Preloading " + Srep.save(s4)); 
      log.info("Preloading " + Srep.save(s5)); 
      log.info("Preloading " + Srep.save(s6));
      log.info("Preloading " + Srep.save(s7)); 
      log.info("Preloading " + Srep.save(s8)); 
      log.info("Preloading " + Srep.save(s9));
      log.info("Preloading " + Srep.save(s10)); 
      log.info("Preloading " + Srep.save(s11)); 
      log.info("Preloading " + Srep.save(s12));
      log.info("Preloading " + Srep.save(s13)); 
      log.info("Preloading " + Srep.save(s14)); 
      log.info("Preloading " + Srep.save(s15));
      log.info("Preloading " + Srep.save(s16));
      log.info("Preloading " + Srep.save(s17)); 
      log.info("Preloading " + Srep.save(s18));
      log.info("Preloading " + Srep.save(s19)); 
      log.info("Preloading " + Srep.save(s20));

      Timestamp ts = Timestamp.valueOf("2021-01-19 9:30:00");
      LocalDateTime ldt = ts.toLocalDateTime();
      for (Long i = Long.valueOf(3); i < 23; i++) {
        Optional<Patient> op = Prep.findById(i);
        Patient pat = op.get();
        Random r = new Random();

        // blood pressure
        for (int j = 0; j < 40; j++) {
          double systolic_mu = 130 + r.nextGaussian() * 20;
          double diastolic_mu = 85 + r.nextGaussian() * 20;
          LocalDateTime value = ldt.minus(j , ChronoUnit.DAYS);
          BloodPressure bp = new BloodPressure(systolic_mu, diastolic_mu);
          bp.setDate(value);
          System.out.println(value);
          bp.setPatient(pat);
          bloodPressureRepository.save(bp);
        }
      
        // sugar level
        for (int j = 0; j < 40; j++) {
          double sugar_level = 5 + r.nextGaussian() * 1;
          LocalDateTime value = ldt.minus(j , ChronoUnit.DAYS);
          SugarLevel sl = new SugarLevel(sugar_level, pat);
          sl.setDate(value);
          sugarLevelRepository.save(sl);
          System.out.println("SL");
        }
        // body temperature
        for (int j = 0; j < 40; j++) {
          double bt = 37 + r.nextGaussian() * 1;
          LocalDateTime value = ldt.minus(j , ChronoUnit.DAYS);
          BodyTemperature btmp = new BodyTemperature(bt);
          btmp.setDate(value);
          btmp.setPatient(pat);
          bodyTemperatureRepository.save(btmp);
          System.out.println("BT");
        }

        // Heart Rate
        for (int j = 0; j < 20; j++) {
          for (int k = 0; k < 24; k++) {
            int hb = (int) (80 + r.nextGaussian() * 10);
            LocalDateTime value = ldt.minus(j , ChronoUnit.DAYS);
            value = value.minus(k , ChronoUnit.HOURS);
            HeartRate hr = new HeartRate(hb, pat);
            hr.setDate(value);
            heartRateRepository.save(hr);
          }
          System.out.println("HR");
        }

        // Oxigen
        for (int j = 0; j < 20; j++) {
          for (int k = 0; k < 24; k++) {
            double ox = 100 - Math.abs(1 + r.nextGaussian() * 5);
            LocalDateTime value = ldt.minus(j , ChronoUnit.DAYS);
            value = value.minus(k , ChronoUnit.HOURS);
            OxygenLevel ol = new OxygenLevel(ox, pat);
            ol.setDate(value);
            oxygenLevelRepository.save(ol);
          }
          System.out.println("O");
        }
      }
      */
    };
    
  }
  

}

