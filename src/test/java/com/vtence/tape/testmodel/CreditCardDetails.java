package com.vtence.tape.testmodel;

import java.io.Serializable;

public class CreditCardDetails extends PaymentMethod implements Serializable {

    private final CreditCardType cardType;
    private final String cardNumber;
    private final String cardExpiryDate;

    public CreditCardDetails(CreditCardType cardType, String cardNumber, String cardExpiryDate) {
        this.cardType = cardType;
        this.cardNumber = cardNumber;
        this.cardExpiryDate = cardExpiryDate;
    }

    public CreditCardType getCardType() {
        return cardType;
    }

    public String getCardCommonName() {
        return cardType.commonName();
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getCardExpiryDate() {
        return cardExpiryDate;
    }
}