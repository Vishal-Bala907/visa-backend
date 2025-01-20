package com.visa.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.visa.modals.Blog;

@Repository
public interface BlogInterface extends JpaRepository<Blog, Long> {
	List<Blog> findByCountryName(String countryName);
}
