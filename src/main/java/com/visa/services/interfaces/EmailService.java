package com.visa.services.interfaces;

import com.visa.modals.EmailDetails;

public interface EmailService {
	String sendSimpleMail(EmailDetails details);
}
