package ies.g25.aLIVE;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import ies.g25.aLIVE.rabbitmq.SensorReceiver;

@SpringBootApplication()
@EnableRabbit
@EnableWebSecurity
public class ALiveApplication {

	public static void main(String[] args) {
		SpringApplication.run(ALiveApplication.class, args);
	}
    
	@Bean
    public Queue queueHB() {
        return new Queue("heart_beat");
    }

    @Bean
    public Queue queueBT() {
        return new Queue("body_temp");
    }

    @Bean
    public Queue queueSL() {
        return new Queue("sugar_level");
    }

    @Bean
    public Queue queueOL() {
        return new Queue("oxygen_level");
    }

    @Bean
    public Queue queueBP() {
        return new Queue("blood_pressure");
    }
	
	@Bean
    public DirectExchange exchange() {
        return new DirectExchange("logs");
    }

    @Bean
    public Binding bindingHB() {
        return BindingBuilder.bind(queueHB()).to(exchange()).with("heart_beat");
    }

    @Bean
    public Binding bindingBT() {
        return BindingBuilder.bind(queueBT()).to(exchange()).with("body_temp");
    }

    @Bean
    public Binding bindingBP() {
        return BindingBuilder.bind(queueBP()).to(exchange()).with("blood_pressure");
    }
    @Bean
    public Binding bindingSL() {
        return BindingBuilder.bind(queueSL()).to(exchange()).with("sugar_level");
    }
    @Bean
    public Binding bindingOL() {
        return BindingBuilder.bind(queueOL()).to(exchange()).with("oxygen_level");
    }

    @Bean
    public SensorReceiver receiver() {
        return new SensorReceiver();
    }

}
