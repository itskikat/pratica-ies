package ies.g25.aLIVE.rabbitmq;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;

import ies.g25.aLIVE.exception.ResourceNotFoundException;
import ies.g25.aLIVE.model.BloodPressure;
import ies.g25.aLIVE.model.BodyTemperature;
import ies.g25.aLIVE.model.HeartRate;
import ies.g25.aLIVE.model.OxygenLevel;
import ies.g25.aLIVE.model.Patient;
import ies.g25.aLIVE.model.Sensor;
import ies.g25.aLIVE.model.SugarLevel;
import ies.g25.aLIVE.repository.BloodPressureRepository;
import ies.g25.aLIVE.repository.BodyTemperatureRepository;
import ies.g25.aLIVE.repository.HeartRateRepository;
import ies.g25.aLIVE.repository.OxygenLevelRepository;
import ies.g25.aLIVE.repository.PatientRepository;
import ies.g25.aLIVE.repository.ProfessionalRepository;
import ies.g25.aLIVE.repository.SensorRepository;
import ies.g25.aLIVE.repository.SugarLevelRepository;
import ies.g25.aLIVE.repository.UserRepository;
import ies.g25.aLIVE.restcontroller.PatientRestController;
import ies.g25.aLIVE.restcontroller.SensorRestController;
import ies.g25.aLIVE.websocket.WarningController;

public class SensorReceiver {

    @Autowired
    public SensorRepository sensorRepository;

    @Autowired
    public PatientRepository patientRepository;

    @Autowired
    public HeartRateRepository heartRateRepository;

    @Autowired
    public SugarLevelRepository sugarLevelRepository;

    @Autowired
    public OxygenLevelRepository oxygenLevelRepository;

    @Autowired
    public BloodPressureRepository bloodPressureRepository;

    @Autowired
    public BodyTemperatureRepository bodyTemperatureRepository;

    @Autowired
    public ProfessionalRepository professionalRepository;

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public SensorRestController controller = new SensorRestController(sensorRepository, patientRepository,
            heartRateRepository, sugarLevelRepository, oxygenLevelRepository, bloodPressureRepository, bodyTemperatureRepository);

    @Autowired
    public PatientRestController PatientController = new PatientRestController(patientRepository, heartRateRepository,
            sugarLevelRepository, oxygenLevelRepository, bloodPressureRepository, bodyTemperatureRepository, professionalRepository, userRepository, sensorRepository );

    @Autowired
    public WarningController warningController = new WarningController();

    @RabbitListener(queues = "heart_beat")
    public void receive1(String in) throws InterruptedException, ResourceNotFoundException {
        receive(in, 1);
    }

    @RabbitListener(queues = "sugar_level")
    public void receive2(String in) throws InterruptedException, ResourceNotFoundException {
        receive(in, 2);
    }

    @RabbitListener(queues = "blood_pressure")
    public void receive3(String in) throws InterruptedException, ResourceNotFoundException {
        receive(in, 3);
    }

    @RabbitListener(queues = "body_temp")
    public void receive4(String in) throws InterruptedException, ResourceNotFoundException {
        receive(in, 4);
    }

    @RabbitListener(queues = "oxygen_level")
    public void receive5(String in) throws InterruptedException, ResourceNotFoundException {
        receive(in, 5);
    }

    public void receive(String in, int receiver) throws InterruptedException, ResourceNotFoundException {
        JSONObject jo = new JSONObject(in);
        Integer id = (Integer) jo.get("id");

        
        switch (receiver) {
            case 1:
                Integer val = (Integer) jo.get("heartbeat");
                HeartRate hr = new HeartRate();
                hr.setHeartRate(val);
                controller.createHeartrate(Long.valueOf(id), hr);
                break;

            case 2:
                BigDecimal vals = (BigDecimal) jo.get("sugar");
                SugarLevel sl = new SugarLevel();
                sl.setSugarLevel(vals.doubleValue());
                controller.createSugarLevel(Long.valueOf(id), sl);
                break;

            case 3:
                BigDecimal high = (BigDecimal) jo.get("systolic");
                BigDecimal low = (BigDecimal) jo.get("diastolic");
                BloodPressure bp = new BloodPressure();
                bp.setLow_value(low.doubleValue());
                bp.setHigh_value(high.doubleValue());
                controller.createBloodPressure(Long.valueOf(id), bp);
                break;

            case 4:
                BigDecimal body_temp = (BigDecimal) jo.get("temperature");
                BodyTemperature bt = new BodyTemperature();
                bt.setbodyTemp(body_temp.doubleValue());
                controller.createBodyTemperature(Long.valueOf(id), bt);
                break;

            case 5:
                BigDecimal valo = (BigDecimal) jo.get("oxygen");
                OxygenLevel ol = new OxygenLevel();
                ol.setOxygenLevel(valo.doubleValue());
                controller.createOxygenLevel(Long.valueOf(id), ol);
                break;

            default:
                break;
        }

        Optional<Sensor> op = sensorRepository.findById(Long.valueOf(id));
        if (op.isPresent()) {
            updatePatientStatus(op.get().getPatient());
        }

    }

    public void updatePatientStatus(Patient p) throws ResourceNotFoundException {
        int risk = 0;

        List<Object> list = PatientController.getLatestValues(p.getId());

        for (Object o : list) {
            if (o == null) {
                continue;
            }

            String cl = o.getClass().toString().split(" ")[1].split("\\.")[4];


            switch (cl) {
                case "BloodPressure":
                    BloodPressure bp = (BloodPressure) o;
                    double h = bp.getHigh_value();
                    double l = bp.getLow_value();

                    if (h < 69 || l < 39) {
                        risk += 15;
                    } else if (h < 89 || l < 59) {
                        risk += 4;
                    } else if (h < 99 || l < 64) {
                        risk += 1;
                    } else if (h < 120 && l < 80) {
                        risk += 0;
                    } else if (h < 129 && l < 84) {
                        risk += 1;
                    } else if (h < 139 && l < 89) {
                        risk += 2;
                    } else if (h < 159 && l < 99) {
                        risk += 4;
                    } else if (h < 179 && l < 109) {
                        risk += 5;
                    } else {
                        risk += 15;
                    }

                    break;

                case "BodyTemperature":
                    BodyTemperature bt = (BodyTemperature) o;
                    double temp = bt.getbodyTemp();

                    if (temp < 35) {
                        risk += 15;
                    } else if (temp < 37.5) {
                        risk += 0;
                    } else if (temp < 38.3) {
                        risk += 2;
                    } else {
                        risk += 15;
                    }
                    break;

                case "HeartRate":
                    HeartRate hr = (HeartRate) o;
                    int rate = hr.getHeartRate();

                    if (rate < 75) {
                        risk += 0;
                    } else if (rate < 85) {
                        risk += 1;
                    } else if (rate < 100) {
                        risk += 2;
                    } else if (rate < 120) {
                        risk += 3;
                    } else {
                        risk += 15;
                    }
                    break;

                case "SugarLevel":
                    SugarLevel sl = (SugarLevel) o;
                    double level = sl.getSugarLevel();

                    if (level < 3) {
                        risk += 15;
                    } else if (level < 4) {
                        risk += 2;
                    } else if (level < 5.5) {
                        risk += 0;
                    } else if (level < 7) {
                        risk += 1;
                    } else if (level < 8) {
                        risk += 2;
                    } else if (level < 9) {
                        risk += 4;
                    } else {
                        risk += 15;
                    }
                    break;

                case "OxygenLevel":
                    OxygenLevel ol = (OxygenLevel) o;
                    double olevel = ol.getOxygenLevel();

                    if (olevel < 89) {
                        risk += 15;
                    } else if (olevel < 95) {
                        risk += 4;
                    } else {
                        risk += 0;
                    }
                    break;

                default:
                    break;
            }
        }

        String status = "";
        if (risk < 2) {
            status = "healthy";
        } else if (risk < 7) {
            status = "normal";
        } else if (risk < 15) {
            status = "unhealthy";
        } else {
            status="in-danger";

            if(p.getCurrentstate() != null && !p.getCurrentstate().equals("in-danger")){


                    try {
                        warningController.send(String.valueOf(p.getId())+":"+String.valueOf(p.getProfessional().getId()));
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    
                    

            }
        }

        p.setCurrentstate(status);
        patientRepository.save(p);
        
    }


}
