package com.sample.gatewayserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


//@EnableEurekaClient
@SpringBootApplication
@EnableDiscoveryClient  
//@ComponentScan("com.sample.gatewayserver")
public class GatewayserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayserverApplication.class, args);
	}
}
