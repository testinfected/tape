package com.vtence.tape.testmodel.builders;

import com.vtence.tape.testmodel.CreditCardDetails;
import com.vtence.tape.testmodel.CreditCardType;

public class CreditCardDetailsBuilder implements Builder<CreditCardDetails> {

    private CreditCardType cardType = CreditCardType.visa;
    private String cardNumber;
    private String cardExpiryDate;

    public static CreditCardDetailsBuilder aVisa() {
        return aCreditCard().ofType(CreditCardType.visa);
    }

    public static CreditCardDetailsBuilder aCreditCard() {
        return new CreditCardDetailsBuilder();
    }

    public CreditCardDetailsBuilder ofType(CreditCardType type) {
        this.cardType = type;
        return this;
    }

    public CreditCardDetailsBuilder withNumber(String cardNumber) {
        this.cardNumber = cardNumber;
        return this;
    }

    public CreditCardDetailsBuilder withExpiryDate(String cardExpiryDate) {
        this.cardExpiryDate = cardExpiryDate;
        return this;
    }

    public CreditCardDetails build() {
        return new CreditCardDetails(cardType, cardNumber, cardExpiryDate);
    }
}