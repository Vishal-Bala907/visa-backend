package com.visa.services.imple;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.visa.modals.User;
import com.visa.repos.UserRepository;

@Service
public class UserDetailsServiceImplementation implements UserDetailsService {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
	private UserRepository repository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	    Optional<User> byUserName = repository.findByUserName(username);

	    // Throw exception if user is not found
	    User user = byUserName.orElseThrow(() -> 
	        new UsernameNotFoundException("User not found with username: " + username)
	    );

	    // Return UserDetails object
	    System.out.println(user);
	    return new UserDetailsImplementation(user);
	}

	public String addUser(User userInfo) {
		userInfo.setPassword(bCryptPasswordEncoder.encode(userInfo.getPassword()));
		userInfo.setRole("ROLE_USER");
		repository.save(userInfo);
		return "Saved successfull";
	}

}