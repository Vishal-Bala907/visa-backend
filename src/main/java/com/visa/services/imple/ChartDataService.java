package com.visa.services.imple;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.visa.modals.PaymentDetails;
import com.visa.modals.Visa;
import com.visa.modals.VisaRequestMain;
import com.visa.modals.VisaType;
import com.visa.repos.PaymentRepo;
import com.visa.repos.VisaTypeInterface;

@Service
public class ChartDataService {

	@Autowired
	private VisaTypeInterface type;
	@Autowired
	private PaymentRepo paymentRepo;

	public Map<String, Long> getDataAccordingToDays(List<VisaRequestMain> data) {
		Map<String, Long> map = new HashMap<>();
		List<VisaType> all = type.findAll();

		if (data.isEmpty()) {
			map.put("EMPTY", 0L);
			all.forEach(vt -> map.putIfAbsent(vt.getVisaType(), 0L));
			return map;
		}

		data.stream().forEach(vr -> {
			Visa visa = vr.getVisa();
			if (visa != null) {
				String visaType = visa.getVisaType();
				if (map.containsKey(visaType)) {
					Long long1 = map.get(visaType);
					map.put(visaType, map.getOrDefault(visaType, 0L) + 1);
				} else {
					map.put(visaType, 0L);
				}
			}
		});

		// Ensure all visa types are represented in the map
		all.forEach(vt -> map.putIfAbsent(vt.getVisaType(), 0L));
		return map;
	}

	public Map<String, Long> getIncomeAccordingToDays(List<VisaRequestMain> data) {
		Map<String, Long> map = new HashMap<>();
		List<VisaType> all = type.findAll();

		if (data.isEmpty()) {
			map.put("EMPTY", 0L);
			all.forEach(vt -> map.putIfAbsent(vt.getVisaType(), 0L));
			return map;
		}

		for (VisaRequestMain vr : data) {
			String paymentId = vr.getPaymentId();
			if (paymentId != null) {
				PaymentDetails paymentDetails = paymentRepo.findByPaymentId(paymentId);

				if (paymentDetails != null) {
					Long amount = paymentDetails.getAmount();
					if (amount != null) {
						Visa visa = vr.getVisa();
						if (visa != null) {
							String visaType = visa.getVisaType();
							map.merge(visaType, amount, Long::sum);
						}
					}
				}
			}
		}

		// Ensure all Visa Types are represented in the map
		all.forEach(vt -> map.putIfAbsent(vt.getVisaType(), 0L));
		return map;
	}

}
