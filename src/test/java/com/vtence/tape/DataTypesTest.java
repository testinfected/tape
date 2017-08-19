package com.vtence.tape;

import com.vtence.tape.support.Database;
import com.vtence.tape.support.JDBCTransactor;
import com.vtence.tape.support.UnitOfWork;
import com.vtence.tape.testmodel.Access;
import com.vtence.tape.testmodel.CreditCardDetails;
import com.vtence.tape.testmodel.Item;
import com.vtence.tape.testmodel.PaymentMethod;
import com.vtence.tape.testmodel.Product;
import com.vtence.tape.testmodel.builders.CreditCardDetailsBuilder;
import com.vtence.tape.testmodel.builders.ItemBuilder;
import com.vtence.tape.testmodel.builders.ProductBuilder;
import com.vtence.tape.testmodel.records.Schema;
import org.junit.After;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.text.ParseException;

import static com.vtence.tape.support.TestEnvironment.memory;
import static com.vtence.tape.testmodel.builders.CreditCardDetailsBuilder.aCreditCard;
import static com.vtence.tape.testmodel.builders.DateBuilder.calendarDate;
import static com.vtence.tape.testmodel.builders.ItemBuilder.anItem;
import static com.vtence.tape.testmodel.builders.ProductBuilder.aProduct;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Tests to demonstrate usages of the various SQL data types.
 *
 * See the {@link Schema} for table definitions
 */
public class DataTypesTest {

    Database database = Database.in(memory());
    Connection connection = database.start();
    JDBCTransactor transactor = new JDBCTransactor(connection);

    Table<Product> products = Schema.products();
    Table<Item> items = Schema.itemsOf(products);
    Table<PaymentMethod> payments = Schema.payments();

    @After public void
    stopDatabase() {
        database.stop();
    }

    @Test public void
    usingLongColumns() {
        Product persisted = roundTrip(aProduct());

        assertThat("id", idOf(persisted), notNullValue());
    }

    @Test public void
    usingStringColumns() {
        Product persisted = roundTrip(aProduct().named("Husky"));

        assertThat("name", persisted.getName(), equalTo("Husky"));
    }

    @Test public void
    usingIntegersColumns() {
        CreditCardDetails card = roundTrip(aCreditCard().withCardVerificationCode(654));

        assertThat("cvc", card.getCardVerificationCode(), is(654));
    }

    @Test public void
    decimals() {
        Item item = roundTrip(anItem().of(aProduct()).priced(new BigDecimal("649.99")));

        assertThat("price", item.getPrice(), equalTo(new BigDecimal("649.99")));
    }

    /**
     * Date types are a little tricky. You need to convert the date to the timezone of
     * the column definition because dates do not store a timezone component.
     *
     * The test schema defines that we store dates in UTC so we're converting our dates
     * to UTC before saving to get the proper date back.
     * This is done here in the test but you will likely want to do this
     * in the {@link Record} implementation.
     */
    @Test public void
    usingDateColumns() throws ParseException {
        // We store expiry dates in database as UTC (see schema)
        CreditCardDetails card = roundTrip(aCreditCard().withExpiryDate(
                calendarDate(2018, 3, 1).inZone("UTC").build()));

        assertThat("expiry date", card.getCardExpiryDate(), equalTo(
                calendarDate(2018, 3, 1).inZone("UTC").build()));
    }

    private Product roundTrip(ProductBuilder builder) {
        Product product = builder.build();
        persist(product);
        return Select.from(products).first(connection);
    }

    private Item roundTrip(ItemBuilder builder) {
        Item item = builder.build();
        persist(item.getProduct());
        persist(item);
        return Select.from(items).join(products, "product_id = products.id").first(connection);
    }

    private CreditCardDetails roundTrip(CreditCardDetailsBuilder builder) {
        CreditCardDetails card = builder.build();
        persist(card);
        return (CreditCardDetails) Select.from(payments).first(connection);
    }

    private void persist(final Product product) {
        transactor.perform(new UnitOfWork() {
            public void execute() {
                Insert.into(products, product).execute(connection);
            }
        });
    }

    private void persist(final Item item) {
        transactor.perform(new UnitOfWork() {
            public void execute() {
                Insert.into(items, item).execute(connection);
            }
        });
    }

    private void persist(final CreditCardDetails card) {
        transactor.perform(new UnitOfWork() {
            public void execute() {
                Insert.into(payments, card).execute(connection);
            }
        });
    }

    private Long idOf(Object entity) {
        return Access.idOf(entity).get();
    }
}
