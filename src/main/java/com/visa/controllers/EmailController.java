package com.visa.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.visa.modals.EmailDetails;
import com.visa.services.interfaces.EmailService;

@RestController
@RequestMapping("/mail")
public class EmailController {

	@Autowired
	private EmailService emailService;

	// Sending a simple Email
	@PostMapping("/sendMail")
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	public ResponseEntity<String> sendMail(@RequestBody EmailDetails details) {
		String status = emailService.sendSimpleMail(details);
		if (status == null) {
			return new ResponseEntity<String>("Unable to send mail", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>("Email sent successfull", HttpStatus.OK);
	}
}
