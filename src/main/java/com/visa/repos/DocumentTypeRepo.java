package com.visa.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.visa.modals.DocumentType;

@Repository
public interface DocumentTypeRepo extends JpaRepository<DocumentType, Long> {
	Optional<DocumentType> findByDocumentName(String documentName);
}
