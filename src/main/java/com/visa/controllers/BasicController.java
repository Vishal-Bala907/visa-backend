package com.visa.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.visa.modals.Archive;
import com.visa.modals.Blog;
import com.visa.modals.BlogMetaDTO;
import com.visa.modals.User;
import com.visa.modals.Visa;
import com.visa.repos.BlogInterface;
import com.visa.repos.UserRepository;
import com.visa.services.imple.AdminVisaServiceImple;
import com.visa.services.imple.BasicServiceImple;
import com.visa.services.imple.JwtService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/data")
public class BasicController {

	@Autowired
	private BasicServiceImple basicServiceImple;
	@Autowired
	private JwtService jwtService;
	@Autowired
	private UserRepository repository;
	@Autowired
	BlogInterface blogInterface;
	@Autowired
	AdminVisaServiceImple adminVisaServiceImple;

	@GetMapping("/visas")
	public ResponseEntity<List<Visa>> getAllVisas() {
		List<Visa> allVisas = basicServiceImple.getAllVisas();
		System.out.println(allVisas);
		return ResponseEntity.ok().body(allVisas);
	}

	@GetMapping("/blogs/country-names")
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	public ResponseEntity<HashSet<String>> getAllCountryBlog() {
		HashSet<String> collect = blogInterface.findAll().stream().map(Blog::getCountryName)
				.collect(Collectors.toCollection(HashSet<String>::new));
		return ResponseEntity.ok().body(collect);
	}

	@GetMapping("/country/name/{countryName}")
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	public ResponseEntity<List<Visa>> getAllCountrySpecifivVisas(@PathVariable String countryName) {
		List<Visa> visasByCountryName = basicServiceImple.getVisasByCountryName(countryName);
		return new ResponseEntity<List<Visa>>(visasByCountryName, HttpStatus.OK);
	}

	@GetMapping("/profile")
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	public ResponseEntity<User> getProfile(HttpServletRequest request) {
		String authHeader = request.getHeader("Authorization");

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		String token = authHeader.substring(7); // Remove "Bearer " prefix
//		System.out.println("Token: " + token);

		String mobileNumber = jwtService.extractMobileNumber(token);
		User user = repository.findByMobileNumber(mobileNumber).get();

		return ResponseEntity.ok(user); // Placeholder
	}

	@GetMapping("/doc/checklist/{countryName}")
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	public ResponseEntity<Set<String>> getDocCheckList(@PathVariable String countryName) {
		Set<String> allVisaDocs = basicServiceImple.getAllVisaDocs(countryName);
		return new ResponseEntity<Set<String>>(allVisaDocs, HttpStatus.OK);
	}

	@PutMapping("/update/profile")
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	public ResponseEntity<User> updateProfile(@RequestBody User user) {
		User updateUser = basicServiceImple.updateUser(user);
		if (updateUser == null) {
			return new ResponseEntity<User>(user, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<User>(updateUser, HttpStatus.OK);
	}

	@GetMapping("/blog-meta/{countryName}")
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	public ResponseEntity<List<BlogMetaDTO>> fetchBlogDetailsFirst(@PathVariable String countryName) {
//		System.out.println(countryName);
		List<BlogMetaDTO> blogMetaDTO = basicServiceImple.getBlogMetaDTO(countryName);
		if (blogMetaDTO == null || blogMetaDTO.size() == 0) {
			return new ResponseEntity<List<BlogMetaDTO>>(List.of(), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<List<BlogMetaDTO>>(blogMetaDTO, HttpStatus.OK);

	}

	@GetMapping("/blog-data/{id}")
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	public ResponseEntity<Blog> findBlog(@PathVariable Long id) {
		Blog blogById = basicServiceImple.findBlogById(id);
		if (blogById == null) {
			return new ResponseEntity<Blog>(new Blog(), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Blog>(blogById, HttpStatus.OK);

	}

	@GetMapping("/add-arch/{mobile}/{visaid}")
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	public ResponseEntity<List<Archive>> findBlog(@PathVariable String mobile, @PathVariable Long visaid) {
		List<Archive> toArchive = basicServiceImple.addToArchive(mobile, visaid);
		/*
		 * if (blogById == null) { return new ResponseEntity<Blog>(new Blog(),
		 * HttpStatus.NOT_FOUND); }
		 */
		return new ResponseEntity<List<Archive>>(toArchive, HttpStatus.OK);

	}
	@GetMapping("/get-arch/{mobile}")
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	public ResponseEntity<List<Archive>> findArchives(@PathVariable String mobile) {
		List<Archive> toArchive = basicServiceImple.getAllArchives(mobile);
		/*
		 * if (blogById == null) { return new ResponseEntity<Blog>(new Blog(),
		 * HttpStatus.NOT_FOUND); }
		 */
		return new ResponseEntity<List<Archive>>(toArchive, HttpStatus.OK);
		
	}

	@PostMapping("/upload/img")
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	public ResponseEntity<String> uploadImage(@RequestParam MultipartFile file) {
		String uploadImage = adminVisaServiceImple.uploadImage(file);
		if (uploadImage == null) {
			return new ResponseEntity<String>("image upload failed", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<String>(uploadImage, HttpStatus.OK);
	}
}
