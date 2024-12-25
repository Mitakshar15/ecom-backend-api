package com.ainkai.dto;

import com.ainkai.model.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {

    private Long id;


    private String firstName;


    private String lastName;

    private String email;



    private String role;



    private String mobile;




    private List<PaymentInformation> paymentInformation;




    private LocalDateTime createdAt;


    public static  UserResponseDTO fromEntity(User user){
       UserResponseDTO dto = new UserResponseDTO();
       dto.setId(user.getId());
       dto.setEmail(user.getEmail());
       dto.setMobile(user.getMobile());
       dto.setRole(user.getRole());
       dto.setFirstName(user.getFirstName());
       dto.setLastName(user.getLastName());
       dto.setCreatedAt(user.getCreatedAt());
       dto.setPaymentInformation(user.getPaymentInformation());

       return dto;
    }





}
