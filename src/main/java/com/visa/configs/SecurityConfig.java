package com.visa.configs;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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
    @Value("${frontend.base.url}")
    private String CORS;
	
	@Bean
	SecurityFilterChain chain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.authorizeHttpRequests(auth -> 
		auth.requestMatchers("/api/v1/auth/**","/visa/**","/images/**")
				.permitAll()
				.requestMatchers(HttpMethod.OPTIONS, "/admin/visa/**", "/data/**","/payment/**").permitAll()
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
		httpSecurity .cors(cors -> cors.configurationSource(corsConfigurationSource()));
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
	
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
	    CorsConfiguration configuration = new CorsConfiguration();
	    configuration.setAllowedOrigins(List.of(CORS)); // Uses frontend.base.url
	    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
	    configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
	    configuration.setAllowCredentials(true);

	    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    source.registerCorsConfiguration("/**", configuration);
	    return (CorsConfigurationSource) source;
	}
	
	
}