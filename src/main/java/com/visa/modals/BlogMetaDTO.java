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
public class BlogMetaDTO {
	private Long id;
	private String countryName;
	private String bannerimg;
	private String blogHeading;
	private String blogDescription;
}
