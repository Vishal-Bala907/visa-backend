package com.visa.modals;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Builder
public class VisaRequestMain {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private LocalDate startDate;
	private LocalDate endDate;
	private String appointmentDetails;
	private String purposeOfVisit;
	private String mobileNumber;
	@OneToOne
	private Visa visa;
	
	//
	private Boolean completionStatus;
	private Boolean paymentStatus;
	private Long timestamp;
	private String paymentId;
	
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<VisaRequest> visaRequest;
}
