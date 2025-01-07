package com.visa.services.imple;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.visa.modals.CountryName;
import com.visa.repos.CountryNameRepo;
import com.visa.services.interfaces.CountryNameService;

@Service
public class CountryNameServiceImple implements CountryNameService {

	@Autowired
	private CountryNameRepo countryNameRepo;
	
	@Override
	public CountryName findByCountryName(String countryName) {
		Optional<CountryName> byCountryName = countryNameRepo.findByCountryName(countryName);
		return byCountryName.orElse(null);
	}

}
