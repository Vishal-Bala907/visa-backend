package com.visa.services.imple;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.visa.modals.CountryName;
import com.visa.modals.Visa;
import com.visa.repos.CountryNameRepo;
import com.visa.repos.VisaRepo;
import com.visa.services.interfaces.AdminVisaService;

@Service
public class AdminVisaServiceImple implements AdminVisaService {

	@Autowired
	private VisaRepo visaRepo;
	@Autowired
	private FileService fileService;
	@Autowired
	private CountryNameServiceImple countryNameService;
	@Autowired
	private CountryNameRepo countryNameRepo;
	
	@Override
	public boolean addNewVisa(Visa visa, MultipartFile file) {
		
		System.out.println(visa);
		
		visaRepo.save(visa);
		CountryName countryName = null;
		
		// save the country first
		String cName = visa.getCountyName();
		CountryName byCountryName = countryNameService.findByCountryName(cName);
		
		if(byCountryName != null) {
			countryName = byCountryName;
		} else {
			CountryName country = CountryName.builder().countryName(cName).build();
			countryName = countryNameRepo.save(country);
		}
		
		try {
			Visa updateOrSaveProfilePic = fileService.updateOrSaveProfilePic(visa, file);
			Visa VISA = visaRepo.save(updateOrSaveProfilePic);
			List<Visa> visas = countryName.getVisas();
			if(visas == null) {
				visas = new ArrayList<>();
			}
			visas.add(VISA);
			countryNameRepo.save(countryName);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return false;
	}

}
