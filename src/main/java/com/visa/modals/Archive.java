package com.visa.modals;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Builder
@Entity
public class Archive {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String mobileNumber;
	private String visaName;
	private String visaType;
	private Long visaId;
	private LocalDate date;
	private Long timestamp;
}
