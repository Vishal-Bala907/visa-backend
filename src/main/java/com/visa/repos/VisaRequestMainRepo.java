package com.visa.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.visa.modals.VisaRequestMain;

@Repository
public interface VisaRequestMainRepo extends JpaRepository<VisaRequestMain, Long> {
	List<VisaRequestMain> findByMobileNumber(String mobileNumber);
	
	@Query("SELECT vrm FROM VisaRequestMain vrm WHERE vrm.timestamp >= :today AND vrm.timestamp <= :timeStamp")
	List<VisaRequestMain> findByTimeRange(@Param("today") Long today, @Param("timeStamp") Long timeStamp);
}
