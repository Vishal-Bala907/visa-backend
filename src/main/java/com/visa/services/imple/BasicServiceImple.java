package com.visa.services.imple;

import java.util.List;

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

}
