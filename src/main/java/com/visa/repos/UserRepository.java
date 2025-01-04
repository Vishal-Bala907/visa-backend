package com.visa.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.visa.modals.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	
//	@Query("SELECT u FROM User u WHERE u.username = :username")
	Optional<User> findByUserName(String username);
	@Query("SELECT u FROM User u WHERE u.mobileNumber = :mobileNumber")
	Optional<User> findByMobileNumber(String mobileNumber);
}
