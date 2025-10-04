package com.demo.microservices.currencyconversionservice.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.demo.microservices.currencyconversionservice.client.CurrencyExchangeServiceProxy;
import com.demo.microservices.currencyconversionservice.dto.CurrencyConversionBean;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;

@RestController
//@RequestMapping("/test")
public class CurrencyConversionController {
	
	@Autowired  
	private CurrencyExchangeServiceProxy client; 
	
	@GetMapping("/currency-converter/from/{from}/to/{to}/quantity/{quantity}") // where {from} and {to} represents the // column//returns a bean back
	public CurrencyConversionBean convertCurrency(@PathVariable String from, @PathVariable String to,
			@PathVariable BigDecimal quantity) {
		
		/* Call Api using RestTemplate*/
		//setting variables to currency exchange service  
		Map<String, String>uriVariables = new HashMap<>();  
		uriVariables.put("from", from);  
		uriVariables.put("to", to);  
		//calling the currency-exchange-service  
		ResponseEntity<CurrencyConversionBean>responseEntity = new RestTemplate().getForEntity("http://localhost:8000/currency-exchange/from/{from}/to/{to}", CurrencyConversionBean.class, uriVariables);  
		CurrencyConversionBean response = responseEntity.getBody();
			
		//creating a new response bean and getting the response back and taking it into Bean  
		return new CurrencyConversionBean(response.getId(), from,to,response.getConversionMultiple(), quantity,quantity.multiply(response.getConversionMultiple()),response.getPort());
	}	
	
	@CircuitBreaker(name = "currencyExchangeService", fallbackMethod = "fallbackConvertCurrency")
	@GetMapping("/currency-converter-feign/from/{from}/to/{to}/quantity/{quantity}") // where {from} and {to} represents the // column//returns a bean back
	public CurrencyConversionBean convertCurrencyFeign(@PathVariable String from, @PathVariable String to,
			@PathVariable BigDecimal quantity) {
		
		//Call api using feign
		CurrencyConversionBean response = client.convertCurrencyFeign(from, to);
		
		//creating a new response bean and getting the response back and taking it into Bean  
		return new CurrencyConversionBean(response.getId(), from,to,response.getConversionMultiple(), quantity,quantity.multiply(response.getConversionMultiple()),response.getPort());
	}	
	
	public CurrencyConversionBean fallbackConvertCurrency(String from,String to,
			BigDecimal quantity, Throwable throwable) {
		BigDecimal conversionMultiple = BigDecimal.valueOf(1);
		BigDecimal quantity1 = BigDecimal.valueOf(1);
		BigDecimal totalCalculatedAmount = BigDecimal.valueOf(80);
        return new CurrencyConversionBean(10l,"01","80",conversionMultiple,quantity1,totalCalculatedAmount,8001);
    }
	

}
