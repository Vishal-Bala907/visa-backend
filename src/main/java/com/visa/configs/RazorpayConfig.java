package com.visa.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RazorpayConfig {
	@Value("${razorpay.key}")
	private String key;

	@Value("${razorpay.secret}")
	private String secret;

	    public String getKey() {
	        return key;
	    }

	    public String getSecret() {
	        return secret;
	    }
}
