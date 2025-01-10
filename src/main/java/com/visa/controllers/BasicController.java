package com.visa.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.visa.modals.User;
import com.visa.modals.Visa;
import com.visa.repos.UserRepository;
import com.visa.services.imple.BasicServiceImple;
import com.visa.services.imple.JwtService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/data")
public class BasicController {

	@Autowired
	private BasicServiceImple basicServiceImple;
	@Autowired
	private JwtService jwtService;
	@Autowired
	private UserRepository repository;

	@GetMapping("/visas")
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	public ResponseEntity<List<Visa>> getAllVisas() {
		List<Visa> allVisas = basicServiceImple.getAllVisas();
		System.out.println(allVisas);
		return ResponseEntity.ok().body(allVisas);
	}

	@GetMapping("/profile")
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	public ResponseEntity<User> getProfile(HttpServletRequest request) {
	    String authHeader = request.getHeader("Authorization");

	    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	    }

	    // Extract the token
	    String token = authHeader.substring(7); // Remove "Bearer " prefix
	    System.out.println("Token: " + token);

	    // Extract the mobile number
	    String mobileNumber = jwtService.extractMobileNumber(token);
	    User user = repository.findByMobileNumber(mobileNumber).get();

	    // Fetch the user's profile (replace with actual logic)
	    // User user = userService.findByMobileNumber(mobileNumber);
	    // return ResponseEntity.ok(user);

	    return ResponseEntity.ok(user); // Placeholder
	}

}
