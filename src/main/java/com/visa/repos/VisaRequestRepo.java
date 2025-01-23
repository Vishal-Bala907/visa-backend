package com.visa.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.visa.modals.VisaRequest;

@Repository
public interface VisaRequestRepo extends JpaRepository<VisaRequest, Long> {

}
