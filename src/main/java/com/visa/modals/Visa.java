package com.visa.modals;

import java.util.List;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Visa {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String countyName;
	private Long visaFee;
	private Long serviceFee;
	private String visaType;
	private Long waitingTime;
	private String bannerImage;
	private String description;
	private String insaurance;
	private Long stayDuration;
	private Long visaValidity;
	
	@ElementCollection
	private List<String> documents;
	private String tag;
}
