package com.visa.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.visa.modals.DocumentType;
import com.visa.modals.VisaType;
import com.visa.repos.DocumentTypeRepo;
import com.visa.repos.VisaTypeInterface;

@RestController
@RequestMapping("/admin/visa")
public class AdminVisaController {

	@Autowired
	private VisaTypeInterface visaTypeInterface;
	@Autowired
	private DocumentTypeRepo typeRepo;

	@PostMapping("/add-type")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<String>> addVisaType(@RequestBody VisaType visaType) {
		Optional<VisaType> byVisaType = visaTypeInterface.findByVisaType(visaType.getVisaType());

		if (byVisaType.isPresent()) {
			return new ResponseEntity<List<String>>(List.of(), HttpStatus.CONFLICT);
		}

		visaTypeInterface.save(visaType);
		List<String> visaTypes = visaTypeInterface.findAll().stream().map(VisaType::getVisaType)
				.collect(Collectors.toList());

		return new ResponseEntity<List<String>>(visaTypes, HttpStatus.CREATED);
	}
	
	
	@GetMapping("/visa-types")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<String>> fetchAllVisaType() {
		List<String> visaTypes = visaTypeInterface.findAll().stream().map(VisaType::getVisaType).collect(Collectors.toList());		
		return new ResponseEntity<List<String>>(visaTypes, HttpStatus.CREATED);
	}
	
	@PostMapping("/add-doc-type")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<String>> addDocumentType(@RequestBody DocumentType docType) {
		Optional<DocumentType> type = typeRepo.findByDocumentName(docType.getDocumentName());

		if (type.isPresent()) {
			return new ResponseEntity<List<String>>(List.of(), HttpStatus.CONFLICT);
		}

		typeRepo.save(docType);
		List<String> docs = typeRepo.findAll().stream().map(DocumentType::getDocumentName)
				.collect(Collectors.toList());

		return new ResponseEntity<List<String>>(docs, HttpStatus.CREATED);
	}
	
	
}
