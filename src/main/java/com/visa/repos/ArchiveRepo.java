package com.visa.repos;

//import java.awt.print.Pageable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.visa.modals.Archive;

public interface ArchiveRepo extends JpaRepository<Archive, Long> {
	Archive findByMobileNumberAndVisaId(String molibeNumber, Long visaId);

	Page<Archive> findByMobileNumber(String mobileNumber, Pageable pageable);
	List<Archive> findByMobileNumber(String mobileNumber);

	Page<Archive> findAll(Pageable pageable);
}
