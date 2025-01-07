package com.visa.services.interfaces;

import com.visa.modals.CountryName;

public interface CountryNameService {
	
	CountryName findByCountryName(String countryName);
	
}
