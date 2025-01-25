package com.visa.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.visa.modals.PaymentDetails;


@Repository
public interface PaymentRepo extends JpaRepository<PaymentDetails, Long> {
	
	PaymentDetails findByPaymentId(String paymentId);
	
}
