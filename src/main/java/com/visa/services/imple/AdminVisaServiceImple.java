package com.visa.services.imple;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.visa.modals.Blog;
import com.visa.modals.CountryName;
import com.visa.modals.EmbassyFeesStructure;
import com.visa.modals.ImageUpdateDTO;
import com.visa.modals.Visa;
import com.visa.modals.VisaRequestMain;
import com.visa.modals.VisaType;
import com.visa.repos.BlogInterface;
import com.visa.repos.CountryNameRepo;
import com.visa.repos.VisaRepo;
import com.visa.repos.VisaRequestMainRepo;
import com.visa.repos.VisaTypeInterface;
import com.visa.services.interfaces.AdminVisaService;

@Service
public class AdminVisaServiceImple implements AdminVisaService {

	@Autowired
	private VisaRepo visaRepo;
	@Autowired
	private FileService fileService;
	@Autowired
	private CountryNameServiceImple countryNameService;
	@Autowired
	private CountryNameRepo countryNameRepo;
	@Autowired
	private BlogInterface blogInterface;
	@Autowired
	private VisaRequestMainRepo visaRequestMainRepo;
	@Autowired
	private VisaTypeInterface vtInterface;
	@Autowired
	private ChartDataService chartDataService;
	@Autowired
	private DateService dateService;

	@Override
	public boolean addNewVisa(Visa visa, MultipartFile file) {

		visaRepo.save(visa);
		CountryName countryName = null;

		// save the country first
		String cName = visa.getCountyName();
		CountryName byCountryName = countryNameService.findByCountryName(cName);

		if (byCountryName != null) {
			countryName = byCountryName;
		} else {
			CountryName country = CountryName.builder().countryName(cName).build();
			countryName = countryNameRepo.save(country);
		}

		try {
			Visa updateOrSaveProfilePic = fileService.updateOrSaveProfilePic(visa, file);
			Visa VISA = visaRepo.save(updateOrSaveProfilePic);
			List<Visa> visas = countryName.getVisas();
			if (visas == null) {
				visas = new ArrayList<>();
			}
			visas.add(VISA);
			countryNameRepo.save(countryName);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return false;
	}

	@Override
	public List<Visa> updateVisa(Visa visa) {
		final Long visaId = visa.getId();
		if (visaId == null) {
			return null;
		}

		// get the original visa id
		Optional<Visa> byId = visaRepo.findById(visaId);
		Visa requestedVisa = byId.orElseThrow(() -> new NullPointerException("visa not found"));

		// update the requestedVisa values
//		System.out.println(visa.getCountyName());
//		System.out.println(visa);
		String countryName = "";
//		System.out.println(visa.getCountyName());
		if (visa.getCountyName() == null) {
			countryName = requestedVisa.getCountyName();
		} else {
			countryName = visa.getCountyName();
		}
		requestedVisa.setCountyName(countryName);
		requestedVisa.setDescription(visa.getDescription());
		requestedVisa.setDocuments(visa.getDocuments());
		requestedVisa.setServiceFee(visa.getServiceFee());
		requestedVisa.setStayDuration(visa.getStayDuration());
		requestedVisa.setTag(visa.getTag());
		requestedVisa.setVisaFee(visa.getVisaFee());
		requestedVisa.setVisaType(visa.getVisaType());
		requestedVisa.setVisaValidity(visa.getVisaValidity());
		requestedVisa.setWaitingTime(visa.getWaitingTime());

		String insurrance = visa.getInsaurance();
		if (insurrance == null) {
			insurrance = "";
		}

		requestedVisa.setInsaurance(visa.getInsaurance());
		visaRepo.save(requestedVisa);
		return visaRepo.findAll();
	}

	@Override
	public List<Visa> updateImage(ImageUpdateDTO imageUpdateDTO) {

		Optional<Visa> byId = visaRepo.findById(imageUpdateDTO.getVisaId());
		if (byId.isEmpty()) {
			return null;
		}
		// delete image
		Visa visa = byId.get();
		boolean handleBase64Image = fileService.handleBase64Image(visa, imageUpdateDTO);
		if (handleBase64Image) {
			return visaRepo.findAll();
		} else {
			return null;
		}
	}

	@Override
	public List<Visa> updateEmbassyFees(Long visaId, EmbassyFeesStructure embassyFeesStructure) {

		Optional<Visa> byId = visaRepo.findById(visaId);
		if (byId.isEmpty() || embassyFeesStructure == null) {
			return null;
		}

		Visa visa = byId.get();
		visa.setEmbassyFees(embassyFeesStructure);
		visaRepo.save(visa);

		return visaRepo.findAll();
	}

	@Override
	public List<Visa> deleteVisa(Long visaId) {
		if (visaId == null) {
			return null;
		}

		Optional<Visa> byId = visaRepo.findById(visaId);
		if (byId.isEmpty()) {
			return null;
		}

		// delete the image first
		Visa visa = byId.get();
		try {
			fileService.deleteImageByVisaId(visa.getBannerImage());
			// delete the visa obj
			visaRepo.delete(visa);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return visaRepo.findAll();
	}

	@Override
	public HashSet<String> uploadBlog(Blog blog, MultipartFile banner, MultipartFile img1, MultipartFile img2) {
		// add images first

		// Uploading the banner image
		String bannerImage = fileService.uploadBlogImage(banner);
		if (bannerImage != null) {
			blog.setBannerImage(bannerImage);
		} else {
			return null;
		}

		// Uploading the first image
		String img1Path = fileService.uploadBlogImage(img1);
		if (img1Path != null) {
			blog.setImg1(img1Path); // Set img1 correctly
		} else {
			return null;
		}

		// Uploading the second image
		String img2Path = fileService.uploadBlogImage(img2);
		if (img2Path != null) {
			blog.setImg2(img2Path); // Set img2 correctly
		} else {
			return null;
		}

		blogInterface.save(blog);
		HashSet<String> collect = blogInterface.findAll().stream().map(Blog::getCountryName)
				.collect(Collectors.toCollection(HashSet<String>::new));

		return collect;
	}

	@Override
	public String uploadImage(MultipartFile file) {
		String uploadImage = fileService.uploadImage(file);
		if (uploadImage == null) {
			return null;
		}
		return uploadImage;
	}

	@Override
	public Page<VisaRequestMain> getAllVisaHistory(int size, int page) {

		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("timestamp")));

		Page<VisaRequestMain> list = visaRequestMainRepo.findAll(pageable);
		return list;
	}

	@Override
	public Map<String, Map<String, Long>> getVisaNameAndQt() {

		Map<String, Map<String, Long>> map = new HashMap<>();
		final Long timestamp = new Date().getTime();

		List<VisaType> all = vtInterface.findAll();
		if (all.isEmpty()) {
			Map<String, Long> emap = new HashMap<>();
			emap.put("Empty", 0L);
			map.put("EMPTY", emap);
			return map;
		}

		List<VisaRequestMain> week = visaRequestMainRepo.findByTimeRange(dateService.getPrevWeekTimestamp(), timestamp);
		List<VisaRequestMain> month = visaRequestMainRepo.findByTimeRange(dateService.getPrevMonthTimestamp(),
				timestamp);
		List<VisaRequestMain> year = visaRequestMainRepo.findByTimeRange(dateService.getPrevYearTimestamp(), timestamp);
		
		System.out.println(week);

		Map<String, Long> dataAccordingToSevenDays = chartDataService.getDataAccordingToDays(week);
		Map<String, Long> dataAccordingTo30Days = chartDataService.getDataAccordingToDays(month);
		Map<String, Long> dataAccordingTo365Days = chartDataService.getDataAccordingToDays(year);

		map.put("week", dataAccordingToSevenDays);
		map.put("month", dataAccordingTo30Days);
		map.put("year", dataAccordingTo365Days);
		
		System.out.println(map);

		return map;
	}

	@Override
	public Map<String, Map<String, Long>> getVisaNameAndIncome() {
		Map<String, Map<String, Long>> map = new HashMap<>();
		final Long timestamp = new Date().getTime();

		List<VisaType> all = vtInterface.findAll();
		if (all.isEmpty()) {
			Map<String, Long> emap = new HashMap<>();
			emap.put("Empty", 0L);
			map.put("EMPTY", emap);
			return map;
		}

		List<VisaRequestMain> week = visaRequestMainRepo.findByTimeRange(dateService.getPrevWeekTimestamp(), timestamp);
		List<VisaRequestMain> month = visaRequestMainRepo.findByTimeRange(dateService.getPrevMonthTimestamp(),
				timestamp);
		List<VisaRequestMain> year = visaRequestMainRepo.findByTimeRange(dateService.getPrevYearTimestamp(), timestamp);

		Map<String, Long> dataAccordingToSevenDays = chartDataService.getIncomeAccordingToDays(week);
		Map<String, Long> dataAccordingTo30Days = chartDataService.getIncomeAccordingToDays(month);
		Map<String, Long> dataAccordingTo365Days = chartDataService.getIncomeAccordingToDays(year);

		map.put("week", dataAccordingToSevenDays);
		map.put("month", dataAccordingTo30Days);
		map.put("year", dataAccordingTo365Days);

		return map;
	}

	@Override
	public Map<String, Map<String, Long>> getDatabyDate(String date) {
		LocalDate localDate = LocalDate.parse(date);

		// Define the timezone for conversion
		ZoneId zoneId = ZoneId.systemDefault();

		// Get 12:00 AM timestamp
		long startOfDay = localDate.atStartOfDay(zoneId).toInstant().toEpochMilli();

		// Get 11:59 PM timestamp
		long endOfDay = localDate.atTime(23, 59, 59).atZone(zoneId).toInstant().toEpochMilli();

		// Fetch data within the time range
		List<VisaRequestMain> byTimeRange = visaRequestMainRepo.findByTimeRange(startOfDay, endOfDay);

		// Initialize the response map
		Map<String, Map<String, Long>> resultMap = new HashMap<>();

		// Handle the empty case
		if (byTimeRange.isEmpty()) {
			Map<String, Long> emptyMap = new HashMap<>();
			emptyMap.put("Empty", 0L);
			resultMap.put("QTY", emptyMap);
			resultMap.put("ICM", emptyMap);
			return resultMap;
		}

		// Calculate QTY and ICM data
		Map<String, Long> qty = chartDataService.getDataAccordingToDays(byTimeRange);
		Map<String, Long> icn = chartDataService.getIncomeAccordingToDays(byTimeRange);

		// Populate the result map
		resultMap.put("QTY", qty);
		resultMap.put("ICM", icn);

		return resultMap;
	}

	@Override
	public String markVisaCompleted(VisaRequestMain main) {
		
		long visaReqId = 0;
		try {
			visaReqId = main.getId();
			Optional<VisaRequestMain> byId = visaRequestMainRepo.findById(visaReqId);
			if(byId.isEmpty()) {
				return null;
			}
			VisaRequestMain visaRequestMain = byId.get(); 
			visaRequestMain.setCompletionStatus(true);
			visaRequestMainRepo.save(visaRequestMain);
		} catch (Exception e) {
			return null;
		}
		
		return "Done...";
	}



}
