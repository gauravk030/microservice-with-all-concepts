package com.demo.microservices.currencyconversionservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients("com.demo.microservices.currencyconversionservice")  
@EnableDiscoveryClient  
public class CurrencyConversionServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(CurrencyConversionServiceApplication.class, args);
	}
}
/*
 * Run the new instance on different port then pass VM argument as -Dserver.port=9000
 * in run configuration
 * */
