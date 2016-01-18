package com.vtence.tape;

import com.vtence.tape.support.Database;
import com.vtence.tape.support.JDBCTransactor;
import com.vtence.tape.support.UnitOfWork;
import com.vtence.tape.testmodel.CreditCardDetails;
import com.vtence.tape.testmodel.Item;
import com.vtence.tape.testmodel.LineItem;
import com.vtence.tape.testmodel.Order;
import com.vtence.tape.testmodel.PaymentMethod;
import com.vtence.tape.testmodel.Product;
import com.vtence.tape.testmodel.builders.Builder;
import com.vtence.tape.testmodel.records.Schema;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Test;

import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.vtence.tape.support.TestEnvironment.memory;
import static com.vtence.tape.testmodel.Access.idOf;
import static com.vtence.tape.testmodel.Access.orderOf;
import static com.vtence.tape.testmodel.builders.CreditCardDetailsBuilder.aVisa;
import static com.vtence.tape.testmodel.builders.ItemBuilder.a;
import static com.vtence.tape.testmodel.builders.ItemBuilder.anItem;
import static com.vtence.tape.testmodel.builders.OrderBuilder.anOrder;
import static com.vtence.tape.testmodel.builders.ProductBuilder.aProduct;
import static com.vtence.tape.testmodel.matchers.HasFieldWithValue.hasField;
import static com.vtence.tape.testmodel.matchers.Items.itemWithProductNumber;
import static com.vtence.tape.testmodel.matchers.Lines.lineWithItemNumber;
import static com.vtence.tape.testmodel.matchers.Orders.orderWithNumber;
import static com.vtence.tape.testmodel.matchers.Products.productNamed;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.everyItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.samePropertyValuesAs;

public class SelectionTest {

    Database database = Database.in(memory());

    Connection connection = database.start();
    JDBCTransactor transactor = new JDBCTransactor(connection);

    Table<Product> products = Schema.products();
    Table<Item> items = Schema.itemsOf(products);
    Table<PaymentMethod> payments = Schema.payments();
    Table<Order> orders = Schema.ordersWith(payments);
    Table<LineItem> lineItems = Schema.lineItems();

    @After
    public void
    stopDatabase() {
        database.stop();
    }

    @Test
    public void
    retrievingASingleRecordWithAllColumns() {
        Product original = persist(aProduct().withNumber("12345678").named("English Bulldog").describedAs("A muscular heavy dog"));

        Product record = Select.from(products).first(connection);
        assertThat("record", record, sameProductAs(original));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void
    selectingAllRecordsFromATable() {
        persist(aProduct().named("Bulldog"), aProduct().named("Dalmatian"), aProduct().named("Labrador"));

        Collection<Product> selection = Select.from(products).list(connection);
        assertThat("selection", selection, hasSize(equalTo(3)));
        assertThat("selected products", selection, containsInAnyOrder(
                productNamed("Bulldog"),
                productNamed("Dalmatian"),
                productNamed("Labrador")));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void
    selectingTheFirstInACollectionOfRecords() {
        persist(aProduct().named("Bulldog"), aProduct().named("Dalmatian"), aProduct().named("Labrador"));

        Product selection = Select.from(products).first(connection);
        assertThat("selection", selection, is(productNamed("Bulldog")));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void
    selectingOnlyThoseRecordsThatFulfillASpecifiedCriterion() {
        persist(aProduct().named("Bulldog"), aProduct().named("Dalmatian"), aProduct().named("Labrador"));

        Product selection = Select.from(products).where("name = ?", "Labrador").first(connection);
        assertThat("selection", selection, is(productNamed("Labrador")));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void
    extractingRecordsBasedOnMultipleCriteria() {
        persist(aProduct().named("English Bulldog").describedAs("female"),
                aProduct().named("French Bulldog").describedAs("male"),
                aProduct().named("Labrador Retriever").describedAs("male"));

        Product selection = Select.from(products)
                                  .where("name like ? and description = ?", "%Bulldog", "male")
                                  .first(connection);
        assertThat("selection", selection, is(productNamed("French Bulldog")));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void
    aliasingTheTableName() {
        persist(aProduct().named("Bulldog"), aProduct().named("Dalmatian"), aProduct().named("Labrador"));

        List<Product> selection = Select.from(products, "p")
                                        .where("p.name = ?", "Labrador")
                                        .list(connection);
        assertThat("selection", selection, hasSize(1));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void
    queryingDataFromMultipleTables() {
        Product boxer = persist(aProduct().named("Boxer").withNumber("BOXER"));
        persist(a(boxer).priced("1199.00"));
        Product bulldog = persist(aProduct().named("Bulldog").withNumber("BULLDOG"));
        persist(a(bulldog).priced("899.00"));
        persist(a(bulldog).priced("699.00"));

        List<Item> selection = Select.from(items)
                                     .join(products, "items.product_id = products.id")
                                     .where("products.name = ?", "Bulldog")
                                     .list(connection);
        assertThat("selection", selection, hasSize(2));
        assertThat("selection", selection, everyItem(itemWithProductNumber("BULLDOG")));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void
    queryingDataFromMultipleTablesWithTableAliases() {
        Product bulldog = persist(aProduct().named("Bulldog").withNumber("BULLDOG"));
        persist(a(bulldog).priced("899.00"));

        Item selection = Select.from(items, "i")
                               .join(products, "p", "i.product_id = p.id")
                               .where("p.name = ?", "Bulldog")
                               .first(connection);
        assertThat("selection", selection, itemWithProductNumber("BULLDOG"));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void
    queryingDataFromMultipleTablesWhenItMightOnlyExistInTheFirstTable() {
        persist(
                anOrder().withNumber("00000001"),
                anOrder().withNumber("10000001"),
                anOrder().withNumber("10000002")
                         .paidUsing(persist(aVisa().withNumber("4111111111111111").withExpiryDate("12/18"))));

        List<Order> selection = Select.from(orders)
                                      .leftJoin(payments, "orders.payment_id = payments.id")
                                      .where("orders.number like ?", "1000%")
                                      .list(connection);
        assertThat("selection", selection, hasSize(2));
        assertThat("selection", selection, everyItem(orderWithNumber(startsWith("1000"))));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void
    usingAliasesWithLeftJoins() {
        persist(anOrder().withNumber("10000002")
                         .paidUsing(persist(aVisa().withNumber("4111111111111111").withExpiryDate("12/18"))));

        Order selection = Select.from(orders, "o")
                                .leftJoin(payments, "p", "o.payment_id = p.id")
                                .where("o.number like ?", "1000%")
                                .first(connection);
        assertThat("selection", selection, orderWithNumber(startsWith("1000")));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void
    orderingExtractedRecords() {
        Order order = persist(anOrder().containing(
                anItem().withNumber("00000001"),
                anItem().withNumber("00000002"),
                anItem().withNumber("00000003")));
        for (LineItem lineItem : order.getLineItems()) {
            orderOf(lineItem).set(order);
            persist(lineItem);
        }

        List<LineItem> selection = Select.from(lineItems)
                                         .where("order_id = ?", idOf(order).get())
                                         .orderBy("order_line desc")
                                         .list(connection);
        assertThat("selection", selection, contains(
                lineWithItemNumber("00000003"),
                lineWithItemNumber("00000002"),
                lineWithItemNumber("00000001")));
    }

    private <T> void persist(final Builder<T>... builders) {
        for (final Builder<T> builder : builders) {
            persist(builder);
        }
    }

    private <T> T persist(final Builder<T> builder) {
        return persist(builder.build());
    }

    private <T> T persist(final T entity) {
        transactor.perform(new UnitOfWork() {
            public void execute() {
                Insert.into(tableFor(entity), entity).execute(connection);
            }
        });
        return entity;
    }

    @SuppressWarnings("unchecked")
    private <T> Table<T> tableFor(T entity) {
        Map<Class, Table> tables = new HashMap<Class, Table>();
        tables.put(Item.class, items);
        tables.put(Product.class, products);
        tables.put(CreditCardDetails.class, payments);
        tables.put(Order.class, orders);
        tables.put(LineItem.class, lineItems);
        return (Table<T>) tables.get(entity.getClass());
    }

    private Matcher<Product> sameProductAs(Product original) {
        return allOf(hasField("id", equalTo(idOf(original).get())), samePropertyValuesAs(original));
    }
}
