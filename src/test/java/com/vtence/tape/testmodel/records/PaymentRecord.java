package com.vtence.tape.testmodel.records;

import com.vtence.tape.Column;
import com.vtence.tape.testmodel.CreditCardDetails;
import com.vtence.tape.testmodel.CreditCardType;
import com.vtence.tape.testmodel.PaymentMethod;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.vtence.tape.testmodel.Access.idOf;

public class PaymentRecord extends AbstractRecord<PaymentMethod> {

    private final Column<Long> id;
    private final Column<String> paymentType;
    private final Column<String> cardType;
    private final Column<String> cardNumber;
    private final Column<String> cardExpiryDate;
    private final Column<Integer> cardVerificationCode;

    public static final String CREDIT_CARD = "credit_card";

    public PaymentRecord(Column<Long> id,
                         Column<String> paymentType,
                         Column<String> cardType,
                         Column<String> cardNumber,
                         Column<String> cardExpiryDate,
                         Column<Integer> cardVerificationCode) {
        this.id = id;
        this.paymentType = paymentType;
        this.cardType = cardType;
        this.cardNumber = cardNumber;
        this.cardExpiryDate = cardExpiryDate;
        this.cardVerificationCode = cardVerificationCode;
    }

    public PaymentMethod hydrate(ResultSet rs) throws SQLException {
        if (!CREDIT_CARD.equals(paymentType.get(rs)))
            throw new IllegalArgumentException("payment of type " + paymentType.get(rs));

        CreditCardDetails creditCard = new CreditCardDetails(
                CreditCardType.valueOf(cardType.get(rs)), cardNumber.get(rs), cardExpiryDate.get(rs),
                cardVerificationCode.get(rs));
        idOf(creditCard).set(id.get(rs));
        return creditCard;
    }

    public void dehydrate(PreparedStatement st, PaymentMethod payment) throws SQLException {
        CreditCardDetails creditCard = (CreditCardDetails) payment;
        cardType.set(st, creditCard.getCardType().name());
        cardNumber.set(st, creditCard.getCardNumber());
        cardExpiryDate.set(st, creditCard.getCardExpiryDate());
        cardVerificationCode.set(st, creditCard.getCardVerificationCode());
        paymentType.set(st, CREDIT_CARD);
    }

}
