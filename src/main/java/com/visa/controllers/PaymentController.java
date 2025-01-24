package com.visa.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.visa.modals.PaymentDetails;
import com.visa.services.imple.PaymentService;

@RestController
@RequestMapping("/payment")
public class PaymentController {
	@Autowired
	private PaymentService paymentService;

	@GetMapping("/create-order/{amount}")
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	public String createOrder(@PathVariable Long amount) {
//		int amt = (int) amount;
		try {
			return paymentService.createOrder(amount);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return "Error: " + e.getMessage();
		}

	}

	@PostMapping("/make-order")
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	public ResponseEntity<String> createOrder(@RequestBody PaymentDetails details) {
//		int amt = (int) amount;
		System.out.println(details);
		String handlePayment = paymentService.handlePayment(details);
		return new ResponseEntity<String>(handlePayment, HttpStatus.OK);
	}

}
