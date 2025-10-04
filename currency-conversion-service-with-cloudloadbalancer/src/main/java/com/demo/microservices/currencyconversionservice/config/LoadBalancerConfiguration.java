package com.demo.microservices.currencyconversionservice.config;

import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.core.RandomLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
//Best practice to add load balancer here
@LoadBalancerClient(
	    name = "currency-exchange-service",
	    configuration = LoadBalancerConfiguration.class
	)
public class LoadBalancerConfiguration {

    @Bean
    public ReactorServiceInstanceLoadBalancer randomLoadBalancer(
            Environment environment,
            LoadBalancerClientFactory loadBalancerClientFactory) {
        String name = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
        return new RandomLoadBalancer(
                loadBalancerClientFactory.getLazyProvider(name, ServiceInstanceListSupplier.class),
                name
        );
        
        // For static servers (if not using Eureka)
//        return ServiceInstanceListSuppliers.from("currency-exchange-service",
//                "http://localhost:8000",
//                "http://localhost:8001");
    }
}