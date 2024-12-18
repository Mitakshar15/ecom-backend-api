package com.ainkai.request;

import com.ainkai.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddressRequest {


    private Long userId;


    private String firstName;


    private String lastName;


    private String streetAddress;


    private String city;


    private String state;


    private String zipCode;

    private String mobile;

}
