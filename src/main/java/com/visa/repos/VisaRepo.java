package com.visa.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.visa.modals.Visa;

@Repository
public interface VisaRepo extends JpaRepository<Visa, Long> {
	List<Visa> findByCountyName(String countryName);
}
