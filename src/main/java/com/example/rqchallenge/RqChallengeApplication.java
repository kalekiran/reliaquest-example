package com.example.rqchallenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class RqChallengeApplication {

    public static void main(String[] args) {
        SpringApplication.run(RqChallengeApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
    	SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
    	factory.setConnectTimeout(3000);
    	factory.setReadTimeout(3000);
    	return new RestTemplate(factory);
    }

}
