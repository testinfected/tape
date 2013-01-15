package com.vtence.tape.testmodel.records;

import com.vtence.tape.Column;
import com.vtence.tape.Table;
import com.vtence.tape.testmodel.CreditCardDetails;
import com.vtence.tape.testmodel.CreditCardType;
import com.vtence.tape.testmodel.PaymentMethod;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.vtence.tape.testmodel.Access.idOf;

public class PaymentRecord extends AbstractRecord<PaymentMethod> {

    private final Table<PaymentMethod> payments = new Table<PaymentMethod>("payments", this);

    private final Column<Long> id = payments.LONG("id");
    private final Column<String> paymentType = payments.STRING("payment_type");
    private final Column<String> cardType = payments.STRING("card_type");
    private final Column<String> cardNumber = payments.STRING("card_number");
    private final Column<String> cardExpiryDate = payments.STRING("card_expiry_date");

    public static final String CREDIT_CARD = "credit_card";

    public static Table<PaymentMethod> payments() {
        return new PaymentRecord().payments;
    }

    public CreditCardDetails hydrate(ResultSet rs) throws SQLException {
        if (!paymentType.get(rs).equals(CREDIT_CARD)) throw new IllegalArgumentException("payment of type " + paymentType.get(rs));

        CreditCardDetails creditCard = new CreditCardDetails(
                CreditCardType.valueOf(cardType.get(rs)), cardNumber.get(rs), cardExpiryDate.get(rs));
        idOf(creditCard).set(id.get(rs));
        return creditCard;
    }

    public void dehydrate(PreparedStatement st, PaymentMethod payment) throws SQLException {
        CreditCardDetails creditCard = (CreditCardDetails) payment;
        id.set(st, idOf(creditCard).get());
        cardType.set(st, creditCard.getCardType().name());
        cardNumber.set(st, creditCard.getCardNumber());
        cardExpiryDate.set(st, creditCard.getCardExpiryDate());
        paymentType.set(st, CREDIT_CARD);
    }
}