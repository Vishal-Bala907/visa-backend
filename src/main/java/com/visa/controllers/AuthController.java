package com.visa.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.visa.modals.AuthenticationRequest;
import com.visa.modals.User;
import com.visa.repos.UserRepository;
import com.visa.services.imple.JwtService;
import com.visa.services.imple.UserDetailsServiceImplementation;

import jakarta.servlet.http.Cookie;
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
	public String addNewUser(@RequestBody User userInfo) {
		System.out.println(userInfo.getPassword());
		userInfo.setRole("ROLE_USER");
		String user = detailsServiceImplementation.addUser(userInfo);
		return user;
	}

	@PostMapping("/login")
	public String authenticateAndGetToken(@RequestBody AuthenticationRequest authRequest,
			HttpServletResponse response) {
		Authentication authentication = null;
		
		System.out.println(authRequest.getPassword());
		
		try {
		    authentication = authenticationManager.authenticate(
		        new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword())
		    );
		} catch (Exception e) {
		    e.printStackTrace();
		    throw e;
		}

		if (authentication.isAuthenticated()) {
			System.out.println("logged in");
			String role = repository.findByUserName(authRequest.getUserName()).get().getRole();
			String token = jwtService.generateToken(authRequest.getUserName(), role);

			// Set the token as an HTTP-only cookie
			Cookie cookie = new Cookie("authToken", token);
			cookie.setHttpOnly(false);
			cookie.setPath("/"); // Make the cookie available to the entire application
			response.addCookie(cookie);

			return token;
		} else {
//			System.out.println("login un-successfull");
			throw new UsernameNotFoundException("invalid user request !");
		}
	}

}