package com.demo.microservices.currencyexchangeservice.controller;

import java.math.BigDecimal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import com.demo.microservices.currencyexchangeservice.entity.ExchangeValue;

@SpringBootApplication
@RestController
public class CurrencyExchangeController {
	@Autowired
	private Environment environment;
	private static final Logger logger = LoggerFactory.getLogger(CurrencyExchangeController.class);

	@GetMapping("/currency-exchange/from/{from}/to/{to}") // where {from} and {to} are path variable
	public ExchangeValue retrieveExchangeValue(@PathVariable String from, @PathVariable String to) // from map to USD
																									// and to map to INR
	{
		ExchangeValue exchangeValue = new ExchangeValue(1000L, from, to, BigDecimal.valueOf(65));
		//setting the port
		exchangeValue.setPort(Integer.parseInt(environment.getProperty("local.server.port")));
		logger.info("load exchange details sucessfully");
		return exchangeValue;
	}
}
