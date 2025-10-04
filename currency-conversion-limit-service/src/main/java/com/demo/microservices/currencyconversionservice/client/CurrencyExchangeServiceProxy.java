package com.demo.microservices.currencyconversionservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.demo.microservices.currencyconversionservice.dto.CurrencyConversionBean;

//Create proxy interface
//enabling feign
//@FeignClient(name="currency-exchange-service", url="localhost:8000") 
//@FeignClient(name="currency-exchange-service")
//enabling ribbon  
//@RibbonClient(name="currency-exchange-service")  
@FeignClient(name="currency-exchange-service") 
//enabling Ribbon
//@RibbonClient(name="currency-exchange-service")
public interface CurrencyExchangeServiceProxy {
	@GetMapping("/currency-exchange/from/{from}/to/{to}")
	public CurrencyConversionBean convertCurrencyFeign(@PathVariable("from") String from, @PathVariable("to") String to);
}
