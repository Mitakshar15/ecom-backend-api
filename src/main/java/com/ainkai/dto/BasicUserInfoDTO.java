package com.ainkai.dto;

import com.ainkai.model.Address;
import com.ainkai.model.PaymentInformation;
import com.ainkai.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BasicUserInfoDTO {
    private Long id;

    private String firstName;

    public static  BasicUserInfoDTO fromEntity(User user){
        BasicUserInfoDTO dto = new BasicUserInfoDTO();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());

        return dto;
    }
}
