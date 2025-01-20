package com.visa.services.imple;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.visa.modals.CountryName;
import com.visa.modals.EmbassyFeesStructure;
import com.visa.modals.ImageUpdateDTO;
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

		visaRepo.save(visa);
		CountryName countryName = null;

		// save the country first
		String cName = visa.getCountyName();
		CountryName byCountryName = countryNameService.findByCountryName(cName);

		if (byCountryName != null) {
			countryName = byCountryName;
		} else {
			CountryName country = CountryName.builder().countryName(cName).build();
			countryName = countryNameRepo.save(country);
		}

		try {
			Visa updateOrSaveProfilePic = fileService.updateOrSaveProfilePic(visa, file);
			Visa VISA = visaRepo.save(updateOrSaveProfilePic);
			List<Visa> visas = countryName.getVisas();
			if (visas == null) {
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

	@Override
	public List<Visa> updateVisa(Visa visa) {
		final Long visaId = visa.getId();
		if (visaId == null) {
			return null;
		}

		// get the original visa id
		Optional<Visa> byId = visaRepo.findById(visaId);
		Visa requestedVisa = byId.orElseThrow(() -> new NullPointerException("visa not found"));

		// update the requestedVisa values
//		System.out.println(visa.getCountyName());
//		System.out.println(visa);
		String countryName = "";
//		System.out.println(visa.getCountyName());
		if (visa.getCountyName() == null) {
			countryName = requestedVisa.getCountyName();
		} else {
			countryName = visa.getCountyName();
		}
		requestedVisa.setCountyName(countryName);
		requestedVisa.setDescription(visa.getDescription());
		requestedVisa.setDocuments(visa.getDocuments());
		requestedVisa.setServiceFee(visa.getServiceFee());
		requestedVisa.setStayDuration(visa.getStayDuration());
		requestedVisa.setTag(visa.getTag());
		requestedVisa.setVisaFee(visa.getVisaFee());
		requestedVisa.setVisaType(visa.getVisaType());
		requestedVisa.setVisaValidity(visa.getVisaValidity());
		requestedVisa.setWaitingTime(visa.getWaitingTime());

		String insurrance = visa.getInsaurance();
		if (insurrance == null) {
			insurrance = "";
		}

		requestedVisa.setInsaurance(visa.getInsaurance());
		visaRepo.save(requestedVisa);
		return visaRepo.findAll();
	}

	@Override
	public List<Visa> updateImage(ImageUpdateDTO imageUpdateDTO) {

		Optional<Visa> byId = visaRepo.findById(imageUpdateDTO.getVisaId());
		if (byId.isEmpty()) {
			return null;
		}
		// delete image
		Visa visa = byId.get();
		boolean handleBase64Image = fileService.handleBase64Image(visa, imageUpdateDTO);
		if (handleBase64Image) {
			return visaRepo.findAll();
		} else {
			return null;
		}
	}

	@Override
	public List<Visa> updateEmbassyFees(Long visaId, EmbassyFeesStructure embassyFeesStructure) {

		 Optional<Visa> byId = visaRepo.findById(visaId);
		if (byId.isEmpty() || embassyFeesStructure == null) {
			return null;
		}

		Visa visa = byId.get();
		visa.setEmbassyFees(embassyFeesStructure);
		visaRepo.save(visa);

		return visaRepo.findAll();
	}

	@Override
	public List<Visa> deleteVisa(Long visaId) {
		if (visaId == null) {
			return null;
		}

		Optional<Visa> byId = visaRepo.findById(visaId);
		if (byId.isEmpty()) {
			return null;
		}

		// delete the image first
		Visa visa = byId.get();
		try {
			fileService.deleteImageByVisaId(visa.getBannerImage());
			// delete the visa obj
			visaRepo.delete(visa);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return visaRepo.findAll();
	}

}
