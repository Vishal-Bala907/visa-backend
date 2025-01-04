package com.visa.services.imple;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.visa.services.interfaces.OtpService;

@Service
public class OtpServiceImple implements OtpService {

	private final Map<String, String> otpStore = new HashMap<>();

	@Override
	public String generateOtp(String mobileNumber) {
		String otp = String.valueOf(new Random().nextInt(900000) + 100000); // 6-digit OTP
		otpStore.put(mobileNumber, otp);
		return otp;
	}

	@Override
	public boolean validateOtp(String mobileNumber, String otp) {
		 return otp.equals(otpStore.get(mobileNumber));
	}

	@Override
	public void clearOtp(String mobileNumber) {
		otpStore.remove(mobileNumber);
	}

}
