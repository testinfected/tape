package com.vtence.tape.testmodel.builders;

import com.vtence.tape.testmodel.CreditCardDetails;
import com.vtence.tape.testmodel.CreditCardType;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CreditCardDetailsBuilder implements Builder<CreditCardDetails> {

    public final DateFormat expiryDateFormat = new SimpleDateFormat("MM/yy");

    private CreditCardType cardType = CreditCardType.visa;
    private String cardNumber;
    private Date cardExpiryDate;
    private int cardVerificationCode;

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

    public CreditCardDetailsBuilder withExpiryDate(String date) throws ParseException {
        return withExpiryDate(expiryDateFormat.parse(date));
    }

    public CreditCardDetailsBuilder withExpiryDate(Date cardExpiryDate) {
        this.cardExpiryDate = cardExpiryDate;
        return this;
    }

    public CreditCardDetailsBuilder withCardVerificationCode(int cardVerificationCode) {
        this.cardVerificationCode = cardVerificationCode;
        return this;
    }

    public CreditCardDetails build() {
        return new CreditCardDetails(cardType, cardNumber, cardExpiryDate, cardVerificationCode);
    }
}