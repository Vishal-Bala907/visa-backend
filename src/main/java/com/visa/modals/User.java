package com.visa.modals;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@ToString
@Table(name = "user_table")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = 50)
    private String userName;

    @Column(unique = true, nullable = false)
    @Size(min = 10, max = 15, message = "Please enter a valid mobile number with 10 to 15 digits")
    private String mobileNumber;

    @Column(unique = true, nullable = false)
    @Email(message = "please enter a valid email")
    private String email;

    @Column(nullable = false)
    private String role;
    
//    @OneToMany(cascade = CascadeType.ALL)
//    private List<VisaRequestMain> visaRequestMains;


}
