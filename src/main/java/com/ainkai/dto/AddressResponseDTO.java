package com.ainkai.dto;

import com.ainkai.model.Address;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddressResponseDTO {

    private Long id;


    private String firstName;


    private String lastName;


    private String streetAddress;

    private String city;

    private String state;


    private String zipCode;

    private String mobile;


    public static AddressResponseDTO fromEntity(Address address){

        AddressResponseDTO dto = new AddressResponseDTO();
        dto.setId(address.getId());
        dto.setFirstName(address.getFirstName());
        dto.setLastName(address.getLastName());
        dto.setStreetAddress(address.getStreetAddress());
        dto.setCity(address.getCity());
        dto.setState(address.getState());
        dto.setZipCode(address.getZipCode());
        dto.setMobile(address.getMobile());
        return dto;
    }
    public static List<AddressResponseDTO> fromEntityList(List<Address> addresses){
        List<AddressResponseDTO>responseDTOList = new ArrayList<>();
        for(Address address:addresses){
            responseDTOList.add(fromEntity(address));
        }
        return responseDTOList;
    }



}
