package com.visa.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.visa.modals.VisaType;

@Repository
public interface VisaTypeInterface extends JpaRepository<VisaType, Long> {
		Optional<VisaType> findByVisaType(String visaType);
}
