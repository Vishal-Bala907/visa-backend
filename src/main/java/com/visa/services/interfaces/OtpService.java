package com.visa.services.interfaces;

public interface OtpService {
	public String generateOtp(String mobileNumber);

	public boolean validateOtp(String mobileNumber, String otp);

	public void clearOtp(String mobileNumber);
}
