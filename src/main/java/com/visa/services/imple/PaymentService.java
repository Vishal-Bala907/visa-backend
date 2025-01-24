package com.visa.services.imple;

import java.util.Date;
import java.util.Optional;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.visa.configs.RazorpayConfig;
import com.visa.modals.Archive;
import com.visa.modals.PaymentDetails;
import com.visa.modals.VisaRequestMain;
import com.visa.repos.ArchiveRepo;
import com.visa.repos.PaymentRepo;
import com.visa.repos.VisaRequestMainRepo;

@Service
public class PaymentService {
	private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

	@Autowired
	private RazorpayConfig config;
	@Autowired
	private VisaRequestMainRepo mainRepo;
	@Autowired
	private ArchiveRepo archiveRepo;
	@Autowired
	private PaymentRepo paymentRepo;

	public String createOrder(double amount) {
		if (amount <= 0) {
			throw new IllegalArgumentException("Amount must be greater than zero.");
		}

		try {
			RazorpayClient client = new RazorpayClient(config.getKey(), config.getSecret());
			JSONObject orderRequest = new JSONObject();

			String receiptId = "txn_" + System.currentTimeMillis();
			orderRequest.put("amount", amount * 100); // amount in the smallest currency unit
			orderRequest.put("currency", "INR");
			orderRequest.put("receipt", receiptId);
			orderRequest.put("payment_capture", 1); // auto capture

			logger.info("Creating Razorpay order with receipt: {}", receiptId);

			Order order = client.orders.create(orderRequest);
			logger.info("Order created successfully: {}", order.toString());
			return order.toString();
		} catch (RazorpayException e) {
			logger.error("Error while creating Razorpay order", e);
			throw new RuntimeException("Error creating Razorpay order", e);
		}
	}

	public String handlePayment(PaymentDetails details) {
		final String mobile = details.getMobileNumber();
		final Long visaId = details.getVisaReqId();
		final Long reqId = details.getReqId();
		final Long timestamp = new Date().getTime();

		// Handle missing payment details
		if (details.getPaymentId() == null || details.getOrderId() == null || details.getSignature() == null) {
			return handleInvalidPayment(details, reqId, timestamp);
		}

//		System.out.println(details);
		return handleValidPayment(details, reqId, mobile, visaId, timestamp);
	}

	private String handleInvalidPayment(PaymentDetails details, Long reqId, Long timestamp) {
		details.setPaymentId("0");
		details.setOrderId("0");
		details.setSignature("0");
		details.setTimestamp(timestamp);
		Optional<VisaRequestMain> byId = mainRepo.findById(reqId); // refers to the VisaRequestMain instance
		if (byId.isEmpty()) {
			return "Visa request not found.";
		}

		paymentRepo.save(details);
		VisaRequestMain visaRequestMain = byId.get();
		visaRequestMain.setPaymentStatus(false);
		visaRequestMain.setPaymentId(details.getPaymentId());
		mainRepo.save(visaRequestMain);

		return "failed...";
	}

	private String handleValidPayment(PaymentDetails details, Long reqId, String mobile, Long visaId, Long timestamp) {
		Optional<VisaRequestMain> byId = mainRepo.findById(reqId);
		details.setTimestamp(timestamp);
		if (byId.isEmpty()) {
			return "Visa request not found.";
		}
		PaymentDetails savedDetails = paymentRepo.save(details);
		System.out.println(savedDetails);
		VisaRequestMain visaRequestMain = byId.get();
		visaRequestMain.setPaymentStatus(true);
		visaRequestMain.setPaymentId(savedDetails.getPaymentId());
		mainRepo.save(visaRequestMain);

		// Check for existing archive and delete if found
		Archive archive = archiveRepo.findByMobileNumberAndVisaId(mobile, visaId);
		if (archive != null) {
			archiveRepo.delete(archive);
			return "Payment successful and archive deleted.";
		}

		return "Payment successful.";
	}

}
