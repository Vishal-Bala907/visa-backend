package com.visa.modals;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Builder
public class ImageUpdateDTO {
	private Long visaId;
	private String image;
	private String originalName;
}
