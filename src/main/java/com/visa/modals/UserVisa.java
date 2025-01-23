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
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Builder
public class UserVisa {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String addressLine1;
    private String addressLine2;
    private String city;
    private String dateOfBirth;
    private String email;
    private String expiryDate;
    private String givenName;
    private String issueDate;
    private String issuePlace;
    private String mobile;
    private String passportBack;
    private String passportFront;
    private String passportNumber;
    private String pincode;
    private String placeOfBirth;
    private String sex;
    private String state;
    private String surname;
}
