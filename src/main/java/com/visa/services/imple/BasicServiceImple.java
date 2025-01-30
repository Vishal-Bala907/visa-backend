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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.visa.modals.Archive;
import com.visa.modals.Blog;
import com.visa.modals.BlogMetaDTO;
import com.visa.modals.User;
import com.visa.modals.Visa;
import com.visa.modals.VisaRequestMain;
import com.visa.repos.ArchiveRepo;
import com.visa.repos.BlogInterface;
import com.visa.repos.UserRepository;
import com.visa.repos.VisaRepo;
import com.visa.repos.VisaRequestMainRepo;
import com.visa.services.interfaces.BasicService;

import jakarta.transaction.Transactional;

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
	@Autowired
	private VisaRequestMainRepo visaRequestMainRepo;
//	@Autowired
//	private PaymentRepo paymentRepo;

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

	@Transactional
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
					.visaType(visa.getVisaType()).mobileNumber(mobileNumber).timestamp(timestamp).visaId(visaId)
					.build();
			archiveRepo.save(archive);
			return archiveRepo.findByMobileNumber(mobileNumber);
		} else {
			byMobileNumberAndVisaId.setDate(LocalDate.now());
			byMobileNumberAndVisaId.setTimestamp(timestamp);
			archiveRepo.save(byMobileNumberAndVisaId);
			List<Archive> list = archiveRepo.findByMobileNumber(mobileNumber).stream()
					.sorted(Comparator.comparing(Archive::getTimestamp).reversed()).toList();
			return list;
		}
	}

	@Override
	public Page<Archive> getAllArchives(String mobileNumber, int size, int page) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("timestamp")));

		if ("112233".equals(mobileNumber)) {
			return archiveRepo.findAll(pageable); // The sorting will be handled by the database query
		} else {
			return archiveRepo.findByMobileNumber(mobileNumber, pageable);
		}
	}

//	@Override
//	public List<Archive> getAllArchives(String mobileNumber) {
//		if (mobileNumber.equals("112233")) {
//			List<Archive> list = archiveRepo.findAll().stream()
//					.sorted(Comparator.comparing(Archive::getTimestamp).reversed()).toList();
//			return list;
//		}
//		List<Archive> list = archiveRepo.findByMobileNumber(mobileNumber).stream()
//				.sorted(Comparator.comparing(Archive::getTimestamp).reversed()).toList();
//		return list;
//	}

	@Override
	public String submitVisaApplication(String number, VisaRequestMain main, Long visaId) {
		final Long timestamp = new Date().getTime();
		main.setMobileNumber(number);
		main.setTimestamp(timestamp);
		main.setPaymentStatus(false);
		main.setCompletionStatus(false);
		main.setPaymentId("0");

		// fetch the visa
		Optional<Visa> byId = visaRepo.findById(visaId);
		if (byId.isEmpty()) {
			return null;
		}
		main.setVisa(byId.get());

		if (main.getAppointmentDetails() == null) {
			main.setAppointmentDetails("N/R");
		}
		try {
			System.out.println("heelo");
			VisaRequestMain save = visaRequestMainRepo.save(main);
			return save.getId().toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public Page<VisaRequestMain> getVisaHistory(String number, int size, int page) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("timestamp")));
		Page<VisaRequestMain> byMobileNumber = visaRequestMainRepo.findByMobileNumber(number, pageable);
		return byMobileNumber;
	}

}
