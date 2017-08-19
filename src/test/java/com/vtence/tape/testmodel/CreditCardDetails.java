package com.vtence.tape.testmodel;

import java.util.Date;

public class CreditCardDetails extends PaymentMethod {

    private final CreditCardType cardType;
    private final String cardNumber;
    private final Date cardExpiryDate;
    private final int cardVerificationCode;

    public CreditCardDetails(CreditCardType cardType, String cardNumber, Date cardExpiryDate, int cardVerificationCode) {
        this.cardType = cardType;
        this.cardNumber = cardNumber;
        this.cardExpiryDate = cardExpiryDate;
        this.cardVerificationCode = cardVerificationCode;
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

    public int getCardVerificationCode() {
        return cardVerificationCode;
    }
}