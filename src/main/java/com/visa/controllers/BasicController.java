package com.visa.controllers;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	public ResponseEntity<List<Visa>> getAllVisas() {
		List<Visa> allVisas = basicServiceImple.getAllVisas();
		System.out.println(allVisas);
		return ResponseEntity.ok().body(allVisas);
	}
	
	@GetMapping("/country/name/{countryName}")
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	public ResponseEntity<List<Visa>> getAllCountrySpecifivVisas(@PathVariable String countryName) {
		List<Visa> visasByCountryName = basicServiceImple.getVisasByCountryName(countryName);
		return new ResponseEntity<List<Visa>>(visasByCountryName , HttpStatus.OK);
	}

	@GetMapping("/profile")
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	public ResponseEntity<User> getProfile(HttpServletRequest request) {
	    String authHeader = request.getHeader("Authorization");

	    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	    }

	    String token = authHeader.substring(7); // Remove "Bearer " prefix
	    System.out.println("Token: " + token);

	    String mobileNumber = jwtService.extractMobileNumber(token);
	    User user = repository.findByMobileNumber(mobileNumber).get();

	    return ResponseEntity.ok(user); // Placeholder
	}
	
	@GetMapping("/doc/checklist/{countryName}")
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	public ResponseEntity<Set<String>> getDocCheckList(@PathVariable String countryName) {
		Set<String> allVisaDocs = basicServiceImple.getAllVisaDocs(countryName);
		return new ResponseEntity<Set<String>>(allVisaDocs , HttpStatus.OK);
	}
	@GetMapping("/update/profile")
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	public ResponseEntity<User> updateProfile(@RequestBody User user) {
		 User updateUser = basicServiceImple.updateUser(user);
		if(updateUser == null) {
			return new ResponseEntity<User>(user , HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<User>(updateUser , HttpStatus.OK);
	}

}
