package com.visa.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.visa.modals.AuthenticationRequest;
import com.visa.modals.TokenDTO;
import com.visa.modals.User;
import com.visa.repos.UserRepository;
import com.visa.services.imple.JwtService;
import com.visa.services.imple.OtpServiceImple;
import com.visa.services.imple.UserDetailsServiceImplementation;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtService jwtService;
	@Autowired
	private UserDetailsServiceImplementation detailsServiceImplementation;
	@Autowired
	private UserRepository repository;
	@Autowired
	private OtpServiceImple otpService;

	@GetMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public String admin() {
		return "you are admin ";
	}

	@GetMapping("/user")
	@PreAuthorize("hasRole('USER')")
	public String user() {
		return "you are user ";
	}

	@PostMapping("/sign-up")
	public ResponseEntity<String> addNewUser(@RequestBody User userInfo) {
//		System.out.println(userInfo.getPassword());

		userInfo.setRole("ROLE_USER");
		ResponseEntity<String> response = detailsServiceImplementation.addUser(userInfo);
		return response;
	}

	@PostMapping("/get-otp")
	public ResponseEntity<Map<String, String>> getOtp(@RequestParam String number) {
		otpService.generateOtp(number);
		return null;
	}

	@PostMapping("/login")
	public ResponseEntity<TokenDTO> authenticateAndGetToken(@RequestBody AuthenticationRequest authRequest,
			HttpServletResponse response) {
		String token = jwtService.generateToken(authRequest.getMobileNumber());
		User user = repository.findByMobileNumber(authRequest.getMobileNumber()).get();
		if (user == null) {
			TokenDTO tokenDTO = TokenDTO.builder().email("").userName("").number("").token("").build();
			return new ResponseEntity<TokenDTO>(tokenDTO, HttpStatus.NOT_FOUND);
		}

		TokenDTO tokenDTO = TokenDTO.builder().email(user.getEmail()).userName(user.getUserName())
				.number(user.getMobileNumber()).token(token).role(user.getRole()).build();

		return new ResponseEntity<TokenDTO>(tokenDTO, HttpStatus.OK);
	}

}