package com.visa.services.interfaces;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.visa.modals.EmbassyFeesStructure;
import com.visa.modals.ImageUpdateDTO;
import com.visa.modals.Visa;

public interface AdminVisaService {
	
	public boolean addNewVisa(Visa visa, MultipartFile file);
	public List<Visa> updateVisa(Visa visa);
	public List<Visa> updateImage(ImageUpdateDTO imageUpdateDTO);
	public List<Visa> updateEmbassyFees(Long visaId , EmbassyFeesStructure embassyFeesStructure);
	public List<Visa> deleteVisa(Long visaId);
	
}
