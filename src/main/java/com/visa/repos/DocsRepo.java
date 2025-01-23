package com.visa.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.visa.modals.Docs;

@Repository
public interface DocsRepo extends JpaRepository<Docs, Long> {

}
