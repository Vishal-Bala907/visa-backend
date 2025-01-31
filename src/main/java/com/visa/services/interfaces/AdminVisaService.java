package com.visa.services.interfaces;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.visa.modals.Blog;
import com.visa.modals.EmbassyFeesStructure;
import com.visa.modals.ImageUpdateDTO;
import com.visa.modals.Visa;
import com.visa.modals.VisaRequestMain;

public interface AdminVisaService {
	
	public boolean addNewVisa(Visa visa, MultipartFile file);
	public List<Visa> updateVisa(Visa visa);
	public List<Visa> updateImage(ImageUpdateDTO imageUpdateDTO);
	public List<Visa> updateEmbassyFees(Long visaId , EmbassyFeesStructure embassyFeesStructure);
	public List<Visa> deleteVisa(Long visaId);
	public HashSet<String> uploadBlog(Blog blog , MultipartFile banner , MultipartFile img1, MultipartFile img2);
	public String uploadImage(MultipartFile file);
	public Page<VisaRequestMain> getAllVisaHistory(int size , int page);
	public Map<String,  Map<String,Long>> getVisaNameAndQt();
	public Map<String,  Map<String,Long>> getVisaNameAndIncome();
	public Map<String,  Map<String,Long>> getDatabyDate(String date);
	public String markVisaCompleted(VisaRequestMain main);
	
}
