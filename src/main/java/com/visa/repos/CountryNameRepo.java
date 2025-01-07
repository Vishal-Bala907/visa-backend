package com.visa.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.visa.modals.CountryName;

@Repository
public interface CountryNameRepo extends JpaRepository<CountryName, Long> {
	
	Optional<CountryName> findByCountryName(String countryName);

}
