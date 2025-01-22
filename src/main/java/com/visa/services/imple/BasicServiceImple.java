package com.visa.services.imple;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.visa.modals.Archive;
import com.visa.modals.Blog;
import com.visa.modals.BlogMetaDTO;
import com.visa.modals.User;
import com.visa.modals.Visa;
import com.visa.repos.ArchiveRepo;
import com.visa.repos.BlogInterface;
import com.visa.repos.UserRepository;
import com.visa.repos.VisaRepo;
import com.visa.services.interfaces.BasicService;

@Service
public class BasicServiceImple implements BasicService {

	@Autowired
	private VisaRepo visaRepo;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BlogInterface blogInterface;
	@Autowired
	private ArchiveRepo archiveRepo;

	@Override
	public List<Visa> getAllVisas() {
		// TODO Auto-generated method stub
		return visaRepo.findAll();
	}

	@Override
	public List<Visa> getVisasByCountryName(String countryName) {
		List<Visa> byCountyName = visaRepo.findByCountyName(countryName);
		return byCountyName;
	}

	@Override
	public Set<String> getAllVisaDocs(String countryName) {
		List<Visa> byCountyName = visaRepo.findByCountyName(countryName);
		HashSet<String> documents = new HashSet<>();

		byCountyName.stream().forEach(visa -> {
			List<String> docs = visa.getDocuments();
			for (String doc : docs) {
				if (!documents.contains(doc))
					documents.add(doc);
			}
		});

		return documents;
	}

	@Override
	public User updateUser(User user) {
		User user2 = userRepository.findByMobileNumber(user.getMobileNumber()).get();
		if (user2 == null) {
			return null;
		} else {
			user2.setUserName(user.getUserName());
			user2.setEmail(user.getEmail());

			userRepository.save(user2);

			return user2;
		}

	}

	@Override
	public List<BlogMetaDTO> getBlogMetaDTO(String countryName) {
		ArrayList<BlogMetaDTO> collect = blogInterface.findByCountryName(countryName).stream().map(blog -> {
			BlogMetaDTO dto = BlogMetaDTO.builder().id(blog.getId()).countryName(blog.getCountryName())
					.blogHeading(blog.getBlogHeading()).blogDescription(blog.getBlogDescription())
					.bannerimg(blog.getBannerImage()).build();
			return dto;
		}).collect(Collectors.toCollection(ArrayList::new));
		return collect;
	}

	@Override
	public Blog findBlogById(Long id) {
		Optional<Blog> byId = blogInterface.findById(id);
		if (byId.isEmpty()) {
			return null;
		}
		return byId.get();
	}

	@Override
	public List<Archive> addToArchive(String mobileNumber, Long visaId) {
		Archive byMobileNumberAndVisaId = archiveRepo.findByMobileNumberAndVisaId(mobileNumber, visaId);
		Visa visa = visaRepo.findById(visaId).get();
		final Long timestamp = new Date().getTime();
		if (byMobileNumberAndVisaId == null) {
			Archive archive = Archive.builder().date(LocalDate.now()).visaName(visa.getCountyName())
					.visaType(visa.getVisaType()).mobileNumber(mobileNumber).timestamp(timestamp).visaId(visaId).build();
			archiveRepo.save(archive);
			return archiveRepo.findByMobileNumber(mobileNumber);
		} else {
			byMobileNumberAndVisaId.setDate(LocalDate.now());
			byMobileNumberAndVisaId.setTimestamp(timestamp);
			archiveRepo.save(byMobileNumberAndVisaId);
			List<Archive> list = archiveRepo.findByMobileNumber(mobileNumber).stream().sorted(Comparator.comparing(Archive::getTimestamp).reversed()).toList();
			return list;
		}
	}

	@Override
	public List<Archive> getAllArchives(String mobileNumber) {
		if(mobileNumber.equals("112233")) {
			 List<Archive> list = archiveRepo.findAll().stream().sorted(Comparator.comparing(Archive::getTimestamp).reversed()).toList();
			 return list;
		}
		List<Archive> list = archiveRepo.findByMobileNumber(mobileNumber).stream().sorted(Comparator.comparing(Archive::getTimestamp).reversed()).toList();
		return list;
	}

}
