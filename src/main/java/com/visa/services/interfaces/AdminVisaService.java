package com.visa.services.interfaces;

import org.springframework.web.multipart.MultipartFile;

import com.visa.modals.Visa;

public interface AdminVisaService {
	
	public boolean addNewVisa(Visa visa, MultipartFile file);
	
}
