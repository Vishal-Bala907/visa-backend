package com.visa.modals;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class EmbassyFeesStructure {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private Long appointmentFees;
	@OneToMany(cascade = CascadeType.ALL , fetch = FetchType.EAGER)
	private List<EmbassyFees> fees;
}
