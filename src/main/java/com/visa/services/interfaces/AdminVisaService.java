package com.visa.services.interfaces;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.visa.modals.ImageUpdateDTO;
import com.visa.modals.Visa;

public interface AdminVisaService {
	
	public boolean addNewVisa(Visa visa, MultipartFile file);
	public List<Visa> updateVisa(Visa visa);
	public List<Visa> updateImage(ImageUpdateDTO imageUpdateDTO);
	
}
