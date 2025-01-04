package com.visa.services.imple;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.visa.modals.User;
import com.visa.repos.UserRepository;

@Service
public class UserDetailsServiceImplementation implements UserDetailsService {

//	@Autowired
//	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private UserRepository repository;

	@Override
	public UserDetails loadUserByUsername(String mobileNumber) throws UsernameNotFoundException {
	    // This method should be used for traditional username login. You can ignore or rename it.
	    Optional<User> byUserName =repository.findByMobileNumber(mobileNumber);
	    	
	    System.out.println(byUserName.get());
	    User user = byUserName.orElseThrow(() -> 
	        new UsernameNotFoundException("User not found with username: " + mobileNumber)
	    );

	    return new UserDetailsImplementation(user);
	}

	// New method for loading user by mobile number
//	public UserDetails loadUserByMobileNumber(String mobileNumber) throws UsernameNotFoundException {
//	    Optional<User> byMobileNumber = repository.findByMobileNumber(mobileNumber);
//
//	    User user = byMobileNumber.orElseThrow(() -> 
//	        new UsernameNotFoundException("User not found with mobile number: " + mobileNumber)
//	    );
//
//	    return new UserDetailsImplementation(user);
//	}

	// Add user - This method will still require password handling for sign-up
	public ResponseEntity<String> addUser(User userInfo) {
//		userInfo.setPassword(bCryptPasswordEncoder.encode(userInfo.getPassword())); // Encrypt the password
		userInfo.setRole("ROLE_USER"); // Assign default role
		try {
			repository.save(userInfo); 
			return new ResponseEntity<String>("Sign-up Successfull",HttpStatus.CREATED);
		} catch (Exception e) {
//			e.printStackTrace();

			return new ResponseEntity<String>(e.getCause().toString(),HttpStatus.CONFLICT);
		}
	}
}
