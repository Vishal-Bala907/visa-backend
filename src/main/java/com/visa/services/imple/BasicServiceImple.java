package com.visa.services.imple;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.visa.modals.Visa;
import com.visa.repos.VisaRepo;
import com.visa.services.interfaces.BasicService;

@Service
public class BasicServiceImple implements BasicService {

	@Autowired
	private VisaRepo visaRepo;

	@Override
	public List<Visa> getAllVisas() {
		// TODO Auto-generated method stub
		return visaRepo.findAll();
	}

	@Override
	public List<Visa> getVisasByCountryName(String countryName) {
		List<Visa> byCountyName = visaRepo.findByCountyName(countryName);
		return byCountyName;
	}

	@Override
	public Set<String> getAllVisaDocs(String countryName) {
		List<Visa> byCountyName = visaRepo.findByCountyName(countryName);
		HashSet<String> documents = new HashSet<>();
		
		byCountyName.stream().forEach(visa->{
			List<String> docs = visa.getDocuments();
			for(String doc : docs) {
				if(!documents.contains(doc))
					documents.add(doc);
			}
		});
		
		return documents;
	}

}
