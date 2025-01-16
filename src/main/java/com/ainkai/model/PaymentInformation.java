package com.ainkai.model;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


//Not an Entity because its Embeded in user Model
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentInformation {

    @Column(name = "cardholder_name")
    private String cardHolderName;

    @Column(name = "card_number")
    private String cardNumber;

    @Column(name = "expiration_date")
    private String expirationDate;

    @Column(name = "cvv")
    private String cvv;

}
