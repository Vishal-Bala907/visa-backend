package com.visa.services.interfaces;

import java.util.List;
import java.util.Set;

import com.visa.modals.User;
import com.visa.modals.Visa;

public interface BasicService {
	List<Visa> getAllVisas();
	List<Visa> getVisasByCountryName(String countryName);
	Set<String> getAllVisaDocs(String countryName);
	User updateUser(User user);
}
