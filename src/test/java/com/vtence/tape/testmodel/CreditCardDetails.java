package com.vtence.tape.testmodel;

import java.io.Serializable;
import java.util.Date;

public class CreditCardDetails extends PaymentMethod implements Serializable {

    private final CreditCardType cardType;
    private final String cardNumber;
    private final Date cardExpiryDate;

    public CreditCardDetails(CreditCardType cardType, String cardNumber, Date cardExpiryDate) {
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

    public Date getCardExpiryDate() {
        return cardExpiryDate;
    }
}