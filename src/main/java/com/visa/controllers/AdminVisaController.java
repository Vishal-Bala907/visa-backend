package com.visa.controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.visa.modals.Blog;
import com.visa.modals.DocumentType;
import com.visa.modals.EmbassyFeesStructure;
import com.visa.modals.ImageUpdateDTO;
import com.visa.modals.Visa;
import com.visa.modals.VisaType;
import com.visa.repos.DocumentTypeRepo;
import com.visa.repos.VisaTypeInterface;
import com.visa.services.imple.AdminVisaServiceImple;

@RestController
@RequestMapping("/admin/visa")
public class AdminVisaController {

	@Autowired
	private VisaTypeInterface visaTypeInterface;
	@Autowired
	private DocumentTypeRepo typeRepo;
	@Autowired
	private AdminVisaServiceImple adminVisaServiceImple;

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
		List<String> visaTypes = visaTypeInterface.findAll().stream().map(VisaType::getVisaType)
				.collect(Collectors.toList());
		return new ResponseEntity<List<String>>(visaTypes, HttpStatus.CREATED);
	}

	@GetMapping("/docs")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<String>> getDocumentType() {
		ArrayList<String> collect = typeRepo.findAll().stream().map(DocumentType::getDocumentName)
				.collect(Collectors.toCollection(ArrayList::new));
		return new ResponseEntity<List<String>>(collect, HttpStatus.CREATED);
	}

	@PostMapping("/add-doc-type")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<String>> addDocumentType(@RequestBody DocumentType docType) {
		Optional<DocumentType> type = typeRepo.findByDocumentName(docType.getDocumentName());

		if (type.isPresent()) {
			return new ResponseEntity<List<String>>(List.of(), HttpStatus.CONFLICT);
		}

		typeRepo.save(docType);
		List<String> docs = typeRepo.findAll().stream().map(DocumentType::getDocumentName).collect(Collectors.toList());

		return new ResponseEntity<List<String>>(docs, HttpStatus.CREATED);
	}

	@PostMapping("/add-visa")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> addNewVisa(@RequestParam String countryName, @RequestParam String visaType,
			@RequestParam String selectedTags, @RequestParam Long visaFee, @RequestParam Long serviceFee,
			@RequestParam Long waitingTime, @RequestParam Long stayDuration, @RequestParam Long visaValidity,
			@RequestParam String insuranceDetails, @RequestParam String description,
			@RequestPart("embassyFees") EmbassyFeesStructure embassyFees, @RequestParam List<String> requiredDocuments,
			@RequestParam MultipartFile bannerImage) {

		System.out.println(embassyFees);

		Visa visa = Visa.builder().countyName(countryName).visaType(visaType).serviceFee(serviceFee).bannerImage(null)
				.description(description).documents(requiredDocuments).insaurance(insuranceDetails).tag(selectedTags)
				.visaFee(visaFee).embassyFees(embassyFees).stayDuration(stayDuration).visaValidity(visaValidity)
				.waitingTime(waitingTime).build();

		adminVisaServiceImple.addNewVisa(visa, bannerImage);

		return new ResponseEntity<String>("OK", HttpStatus.CREATED);
	}

	@PostMapping("/add-blog")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<HashSet<String>> addNewBlog(@RequestParam String countryName, @RequestParam String blogHeading,
			@RequestParam String blogDescription, @RequestParam String blogContent,
			@RequestParam MultipartFile bannerImage, @RequestParam MultipartFile img1,
			@RequestParam MultipartFile img2) {

		Blog blog = Blog.builder().countryName(countryName.toLowerCase()).blogHeading(blogHeading).blogDescription(blogDescription)
				.blogContent(blogContent).build();
		HashSet<String> uploadBlog = adminVisaServiceImple.uploadBlog(blog, bannerImage, img1, img2);

		if (uploadBlog == null) {
			HashSet<String> set = new HashSet<>();
			set.add("empty");
			return new ResponseEntity<HashSet<String>>(set, HttpStatus.CREATED);
		}
		return new ResponseEntity<HashSet<String>>(uploadBlog, HttpStatus.CREATED);
	}

	@PutMapping("/update/visa")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<List<Visa>> updateProfile(@RequestBody Visa visa) {
		try {
			List<Visa> updateVisa = adminVisaServiceImple.updateVisa(visa);
			return new ResponseEntity<List<Visa>>(updateVisa, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<List<Visa>>(List.of(visa), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/update/image")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<List<Visa>> updateImage(@RequestBody ImageUpdateDTO imageUpdate) {

		List<Visa> updateImage = adminVisaServiceImple.updateImage(imageUpdate);
		if (updateImage == null) {
			return new ResponseEntity<List<Visa>>(List.of(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<Visa>>(updateImage, HttpStatus.OK);

	}

	@PutMapping("/update/fees/{visaId}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<List<Visa>> updateFess(@PathVariable Long visaId,
			@RequestBody EmbassyFeesStructure embassyFeesStructure) {

		List<Visa> updateEmbassyFees = adminVisaServiceImple.updateEmbassyFees(visaId, embassyFeesStructure);
		if (updateEmbassyFees == null) {
			return new ResponseEntity<List<Visa>>(List.of(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<List<Visa>>(updateEmbassyFees, HttpStatus.OK);

	}

	@DeleteMapping("/update/delete/{visaId}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<List<Visa>> updateFess(@PathVariable Long visaId) {

		List<Visa> updateEmbassyFees = adminVisaServiceImple.deleteVisa(visaId);
		if (updateEmbassyFees == null) {
			return new ResponseEntity<List<Visa>>(List.of(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<List<Visa>>(updateEmbassyFees, HttpStatus.OK);

	}
	
	

}
