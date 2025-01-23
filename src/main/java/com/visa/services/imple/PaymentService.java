package com.visa.services.imple;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.visa.configs.RazorpayConfig;

@Service
public class PaymentService {
    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    @Autowired
    private RazorpayConfig config;

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
}
