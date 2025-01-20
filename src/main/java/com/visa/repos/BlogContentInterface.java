package com.visa.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.visa.modals.BlogContent;

public interface BlogContentInterface extends JpaRepository<BlogContent, Long> {

}
