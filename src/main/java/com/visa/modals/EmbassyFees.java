package com.visa.modals;
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
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class EmbassyFees {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private Long minAge;
	private Long maxAge;
	private Long fees;
}
