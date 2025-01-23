package com.visa.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.visa.modals.VisaRequestMain;

@Repository
public interface VisaRequestMainRepo extends JpaRepository<VisaRequestMain, Long> {
	List<VisaRequestMain> findByMobileNumber(String mobileNumber);
}
