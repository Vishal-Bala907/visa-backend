package com.visa.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.visa.modals.Request;

@Repository
public interface RequestRepo extends JpaRepository<Request, Long> {

}
