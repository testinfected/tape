package com.vtence.tape;

import com.vtence.tape.support.Database;
import com.vtence.tape.support.JDBCTransactor;
import com.vtence.tape.support.UnitOfWork;
import com.vtence.tape.testmodel.Access;
import com.vtence.tape.testmodel.CreditCardDetails;
import com.vtence.tape.testmodel.Item;
import com.vtence.tape.testmodel.Order;
import com.vtence.tape.testmodel.PaymentMethod;
import com.vtence.tape.testmodel.Product;
import com.vtence.tape.testmodel.builders.CreditCardDetailsBuilder;
import com.vtence.tape.testmodel.builders.DateBuilder;
import com.vtence.tape.testmodel.builders.ItemBuilder;
import com.vtence.tape.testmodel.builders.OrderBuilder;
import com.vtence.tape.testmodel.builders.ProductBuilder;
import com.vtence.tape.testmodel.records.Schema;
import org.junit.After;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import static com.vtence.tape.support.TestEnvironment.memory;
import static com.vtence.tape.testmodel.builders.CreditCardDetailsBuilder.aCreditCard;
import static com.vtence.tape.testmodel.builders.DateBuilder.aDate;
import static com.vtence.tape.testmodel.builders.DateBuilder.calendarDate;
import static com.vtence.tape.testmodel.builders.ItemBuilder.anItem;
import static com.vtence.tape.testmodel.builders.OrderBuilder.anOrder;
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
    Table<Order> orders = Schema.ordersWith(payments);

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
    usingDecimalColumns() {
        Item item = roundTrip(anItem().of(aProduct()).priced(new BigDecimal("649.99")));

        assertThat("price", item.getPrice(), equalTo(new BigDecimal("649.99")));
    }

    /**
     * Date types are a little tricky. You need to convert the <code>Date</code> to the timezone of
     * the column definition because dates do not store a timezone component.
     *
     * The test schema defines that we store dates in UTC so we're converting our <code>Date</code>s
     * to UTC before saving to get the proper date back.
     *
     * This is done here in the test but you will likely want to do this
     * in the {@link Record} implementation.
     */
    @Test public void
    usingDateColumns() {
        // We store shipping dates in database as UTC (see schema)
        Order order = roundTrip(anOrder().shippedOn(calendarDate(2017, 8, 5).inZone("UTC").build()));

        assertThat("shipping date", order.getShippingDate(), equalTo(
                calendarDate(2017, 8, 5).inZone("UTC").build()));
    }

    /**
     * Time types are similarly tricky. Again, convert the <code>Date</code> to the timezone of
     * the column definition because times do not store a timezone component.
     *
     * The test schema defines that we store times in UTC so we're converting our <code>Date</code>s
     * to UTC before saving to get the proper time back.
     *
     * This is done here in the test but you will likely want to do this
     * in the {@link Record} implementation.
     */
    @Test public void
    usingTimeColumns() {
        // The date is not important here, but JDBC uses January 1, 1970 as the date
        DateBuilder someDate = calendarDate(1970, 1, 1);

        // We store shipping time in database as UTC (see schema)
        Order order = roundTrip(anOrder().shippedAt(aDate().atTime(15, 32, 45).inZone("UTC").build()));

        assertThat("shipping time", order.getShippingTime(), equalTo(
                someDate.atTime(15, 32, 45).inZone("UTC").build()));
    }

    /**
     * Same goes with timestamps. Convert the <code>Timestamp</code> to the timezone of
     * the column definition. Depending on the schema, the database column might or might not store timezone
     * information.
     *
     * The test schema defines that we store timestamps in UTC so we're converting our <code>Date</code>s
     * to UTC before saving to get the proper timestamps back.
     *
     * This is done here in the test but you will likely want to do this
     * in the {@link Record} implementation.
     */
    @Test public void
    usingTimestampsColumns() {
        // We store order timestamp in database as UTC (see schema)
        // Any point in time will do, wo now is perfectly fine
        Date someInstant = aDate().inZone("UTC").build();

        Order order = roundTrip(anOrder().placedAt(someInstant));

        assertThat("ordered at", order.getPlacedAt(), equalTo(someInstant));
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

    private Order roundTrip(OrderBuilder builder) {
        Order order = builder.build();
        persist(order);
        return Select.from(orders).first(connection);
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

    private void persist(final Order order) {
        transactor.perform(new UnitOfWork() {
            public void execute() {
                Insert.into(orders, order).execute(connection);
            }
        });
    }

    private Long idOf(Object entity) {
        return Access.idOf(entity).get();
    }
}
