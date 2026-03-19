package com.fcolucasvieira.smartdelivery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class SmartdeliveryApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartdeliveryApplication.class, args);
	}

}
