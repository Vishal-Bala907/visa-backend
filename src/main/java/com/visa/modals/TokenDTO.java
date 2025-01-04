package com.visa.modals;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TokenDTO {
	private String token;
	private String userName;
	private String email;
	private String number;
	private String role;
}
