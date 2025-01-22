package com.visa.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.visa.modals.Archive;

public interface ArchiveRepo extends JpaRepository<Archive, Long> {
	Archive findByMobileNumberAndVisaId(String molibeNumber , Long visaId);
	List<Archive> findByMobileNumber(String mobileNumber);
}
