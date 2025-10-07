package com.sample.gatewayserver.filters;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

@Component
public class FilterUtility {

	private static final Logger logger = LoggerFactory.getLogger(FilterUtility.class);
	public static final String CORRELATION_ID = "currancy-correlation-id";
	
	public String getCorrelationId(HttpHeaders requestHeaders) {
		if (requestHeaders.get(CORRELATION_ID) != null) {
			List<String> requestHeaderList = requestHeaders.get(CORRELATION_ID);
			return requestHeaderList.stream().findFirst().get();
		} else {
			logger.warn("Request id is not present for current request");
			return null;
		}
	}

	public ServerWebExchange setRequestHeader(ServerWebExchange exchange, String name, String value) {
		return exchange.mutate().request(exchange.getRequest().mutate().header(name, value).build()).build();
	}

	public ServerWebExchange setCorrelationId(ServerWebExchange exchange, String correlationId) {
		return this.setRequestHeader(exchange, CORRELATION_ID, correlationId);
	}

}
