package com.visa.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.visa.modals.UserVisa;

@Repository
public interface UserVisaDetailsRepo extends JpaRepository<UserVisa, Long> {

}
