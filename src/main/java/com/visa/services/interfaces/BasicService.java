package com.visa.services.interfaces;

import java.util.List;
import java.util.Set;

import com.visa.modals.Archive;
import com.visa.modals.Blog;
import com.visa.modals.BlogMetaDTO;
import com.visa.modals.User;
import com.visa.modals.Visa;
import com.visa.modals.VisaRequestMain;

public interface BasicService {
	List<Visa> getAllVisas();
	List<Visa> getVisasByCountryName(String countryName);
	Set<String> getAllVisaDocs(String countryName);
	User updateUser(User user);
	List<BlogMetaDTO> getBlogMetaDTO(String countryName);
	Blog findBlogById(Long id);
	List<Archive> addToArchive(String mobileNumber , Long visaId);
	List<Archive> getAllArchives(String mobileNumber);
	String submitVisaApplication(String number, VisaRequestMain main);
}
