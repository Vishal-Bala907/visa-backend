package com.visa.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.visa.services.imple.JwtAuthFilter;
import com.visa.services.imple.UserDetailsServiceImplementation;


@Configuration
@EnableMethodSecurity
public class SecurityConfig {
	@Autowired
	private JwtAuthFilter jwtAuthFilter;
	@Autowired
	private PasswordEncoder encoder;
	@Autowired
	private UserDetailsServiceImplementation userDetailsServiceImplementation;
	@Autowired
	private CustomAccessDeniedHandler customAccessDeniedHandler;
	@Autowired
	private CustomeAuthenticationEntryPoint authenticationEntryPoint;
	
	@Bean
	SecurityFilterChain chain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.authorizeHttpRequests(auth -> 
		auth.requestMatchers("/api/v1/auth/**")
				.permitAll()
				.requestMatchers(HttpMethod.OPTIONS, "/admin/visa/**").permitAll()
				.anyRequest().authenticated())
		  .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		  .exceptionHandling((exceptions)-> exceptions
				  .accessDeniedHandler(customAccessDeniedHandler)
						.authenticationEntryPoint(
								authenticationEntryPoint)
				  )
          .authenticationProvider(authenticationProvider())
          .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
          ;
		httpSecurity.cors(cors->cors.disable());
		httpSecurity.csrf(csrf -> csrf.disable());

		return httpSecurity.build();
	}
	
	@Bean
	AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsServiceImplementation);
		authenticationProvider.setPasswordEncoder(encoder);
		return authenticationProvider;
	}
	
	
}