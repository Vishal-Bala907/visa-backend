package com.visa.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.visa.modals.Visa;
import com.visa.services.imple.BasicServiceImple;

@RestController
@RequestMapping("/data")
public class BasicController {

	@Autowired
	private BasicServiceImple basicServiceImple;

	@GetMapping("/visas")
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	public ResponseEntity<List<Visa>> getAllVisas() {
		List<Visa> allVisas = basicServiceImple.getAllVisas();
		System.out.println(allVisas);
		return ResponseEntity.ok().body(allVisas);
	}

}
