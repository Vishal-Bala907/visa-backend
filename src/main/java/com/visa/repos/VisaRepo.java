package com.visa.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.visa.modals.Visa;

@Repository
public interface VisaRepo extends JpaRepository<Visa, Long> {

}
