package com.vtence.tape;

import com.vtence.tape.support.Database;
import com.vtence.tape.support.JDBCTransactor;
import com.vtence.tape.testmodel.CreditCardDetails;
import com.vtence.tape.testmodel.Item;
import com.vtence.tape.testmodel.LineItem;
import com.vtence.tape.testmodel.Order;
import com.vtence.tape.testmodel.PaymentMethod;
import com.vtence.tape.testmodel.Product;
import com.vtence.tape.testmodel.builders.Builder;
import com.vtence.tape.testmodel.records.Schema;
import org.junit.After;
import org.junit.Test;

import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.vtence.tape.support.TestEnvironment.memory;
import static com.vtence.tape.testmodel.Access.idOf;
import static com.vtence.tape.testmodel.Access.orderOf;
import static com.vtence.tape.testmodel.builders.CreditCardDetailsBuilder.aVisa;
import static com.vtence.tape.testmodel.builders.ItemBuilder.a;
import static com.vtence.tape.testmodel.builders.ItemBuilder.anItem;
import static com.vtence.tape.testmodel.builders.OrderBuilder.anOrder;
import static com.vtence.tape.testmodel.builders.ProductBuilder.aProduct;
import static com.vtence.tape.testmodel.matchers.Items.itemWithProductNumber;
import static com.vtence.tape.testmodel.matchers.Lines.lineWithItemNumber;
import static com.vtence.tape.testmodel.matchers.Orders.orderWithNumber;
import static com.vtence.tape.testmodel.matchers.Products.productNamed;
import static com.vtence.tape.testmodel.matchers.Products.sameProductAs;
import static org.hamcrest.CoreMatchers.everyItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

public class SelectionTest {

    Database database = Database.in(memory());

    /**
     * All statements accepts a {@link Connection}
     */
    Connection connection = database.start();
    /**
     * An alternative to using a <code>Connection</code> is to provide a {@link StatementExecutor}
     */
    StatementExecutor executor = database::execute;

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
        Product original = persist(aProduct().withNumber("12345678")
                                             .named("English Bulldog")
                                             .describedAs("A muscular heavy dog"));

        Product record = assertFound(Select.from(products)
                                           .first(connection));

        assertThat("record", record, sameProductAs(original));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void
    selectingAllRecordsFromATable() {
        persist(aProduct().named("Bulldog"),
                aProduct().named("Dalmatian"),
                aProduct().named("Labrador"));

        Collection<Product> selection = Select.from(products)
                                              .list(connection);

        assertThat("selection", selection, hasSize(equalTo(3)));
        assertThat("selected products", selection, containsInAnyOrder(
                productNamed("Bulldog"),
                productNamed("Dalmatian"),
                productNamed("Labrador")));
    }

    @Test
    public void
    selectingTheFirstInACollectionOfRecords() {
        persist(aProduct().named("Bulldog"),
                aProduct().named("Dalmatian"),
                aProduct().named("Labrador"));

        Product selection = assertFound(Select.from(products)
                                              // use an executor rather than a connection this time
                                              .first(executor));
        assertThat("selection", selection, is(productNamed("Bulldog")));
    }

    @Test
    public void
    selectingOnlyThoseRecordsThatFulfillASpecifiedCriterion() {
        persist(aProduct().named("Bulldog"),
                aProduct().named("Dalmatian"),
                aProduct().named("Labrador"));

        Product selection = assertFound(Select.from(products)
                                              .where("name = ?", "Labrador")
                                              // another way to select records is to stream results
                                              .stream(connection)
                                              .findFirst());
        assertThat("selection", selection, is(productNamed("Labrador")));
    }

    @Test
    public void
    extractingRecordsBasedOnMultipleCriteria() {
        persist(aProduct().named("English Bulldog").describedAs("female"),
                aProduct().named("French Bulldog").describedAs("male"),
                aProduct().named("Labrador Retriever").describedAs("male"));

        Product selection = assertFound(Select.from(products)
                                              .where("name LIKE ? AND description = ?", "%Bulldog", "male")
                                              // streaming can be done with an executor too ...
                                              .stream(executor)
                                              .findFirst());
        assertThat("selection", selection, is(productNamed("French Bulldog")));
    }

    @Test
    public void
    aliasingTheTableName() {
        persist(aProduct().named("Bulldog"),
                aProduct().named("Dalmatian"),
                aProduct().named("Labrador"));

        List<Product> selection = Select.from(products, "p")
                                        .where("p.name = ?", "Labrador")
                                        // ... as is listing
                                        .list(executor);
        assertThat("selection", selection, hasSize(1));
    }

    @Test
    public void
    joiningWithAnotherTable() {
        persist(a(persist(aProduct().named("Boxer").withNumber("BOXER"))).priced("1199.00"));
        persist(aProduct().named("Bulldog").withNumber("BULLDOG"));

        List<Product> inventory = Select.from(products)
                                        .join(items, "items.product_id = products.id")
                                        .list(connection);
        assertThat("selection", inventory, contains(productNamed("Boxer")));
    }

    @Test
    public void
    leftJoiningWithAnotherTable() {
        persist(a(persist(aProduct().named("Boxer").withNumber("BOXER"))).priced("1199.00"));
        persist(aProduct().named("Bulldog").withNumber("BULLDOG"));

        List<Product> inventory = Select.from(products)
                                        .leftJoin(items, "items.product_id = products.id")
                                        .where("items.number IS NOT NULL ")
                                        .list(connection);
        assertThat("selection", inventory, contains(productNamed("Boxer")));
    }

    @Test
    public void
    queryingDataFromMultipleTables() {
        Product boxer = persist(aProduct().named("Boxer").withNumber("BOXER"));
        persist(a(boxer).priced("1199.00"));
        Product bulldog = persist(aProduct().named("Bulldog").withNumber("BULLDOG"));
        persist(a(bulldog).priced("899.00"));
        persist(a(bulldog).priced("699.00"));

        List<Item> selection = Select.from(items)
                                     .join(products, "items.product_id = products.id", true)
                                     .join(products, "p2", "items.product_id = p2.id", true)
                                     .where("products.name = ?", "Bulldog")
                                     .list(connection);
        assertThat("selection", selection, hasSize(2));
        assertThat("selection", selection, everyItem(itemWithProductNumber("BULLDOG")));
    }

    @Test
    public void
    queryingDataFromMultipleTablesWithTableAliases() {
        Product bulldog = persist(aProduct().named("Bulldog").withNumber("BULLDOG"));
        persist(a(bulldog).priced("899.00"));

        Item selection = assertFound(Select.from(items, "i")
                                           .join(products, "p", "i.product_id = p.id", true)
                                           .where("p.name = ?", "Bulldog")
                                           .first(connection));
        assertThat("selection", selection, itemWithProductNumber("BULLDOG"));
    }

    @Test
    public void
    queryingDataFromMultipleTablesThatMightOnlyExistInTheFirstTable() {
        persist(
                anOrder().withNumber("00000001"),
                anOrder().withNumber("10000001"),
                anOrder().withNumber("10000002")
                         .paidUsing(persist(aVisa().withNumber("4111111111111111").withExpiryDate("12/18"))));

        List<Order> selection = Select.from(orders)
                                      .leftJoin(payments, "orders.payment_id = payments.id", true)
                                      .where("orders.number LIKE ?", "1000%")
                                      .list(connection);
        assertThat("selection", selection, hasSize(2));
        assertThat("selection", selection, everyItem(orderWithNumber(startsWith("1000"))));
    }

    @Test
    public void
    usingAliasesWithLeftJoins() {
        persist(anOrder().withNumber("10000002")
                         .paidUsing(persist(aVisa().withNumber("4111111111111111").withExpiryDate("12/18"))));

        Order selection = assertFound(Select.from(orders, "o")
                                            .leftJoin(payments, "p", "o.payment_id = p.id", true)
                                            .where("o.number LIKE ?", "1000%")
                                            .first(connection));
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
                                         .orderBy("order_line DESC")
                                         .list(connection);
        assertThat("selection", selection, contains(
                lineWithItemNumber("00000003"),
                lineWithItemNumber("00000002"),
                lineWithItemNumber("00000001")));
    }

    @Test
    public void
    selectingOnlyDistinctRecords() {
        Product singleProduct = persist(aProduct().withNumber("00000001"));
        persist(anItem().withNumber("1").of(singleProduct));
        persist(anItem().withNumber("2").of(singleProduct));
        persist(anItem().withNumber("3").of(persist(aProduct().withNumber("00000002"))));
        persist(anItem().withNumber("4").of(persist(aProduct().withNumber("00000003"))));

        List<Product> distinct = Select.distinct(products)
                                       .join(items, "items.product_id = products.id")
                                       .list(connection);
        assertThat("distinct products", distinct, hasSize(3));
    }

    @Test
    public void
    countingTheNumberOfRecords() {
        persist(
                aProduct().withNumber("00000001"),
                aProduct().withNumber("00000002"),
                aProduct().withNumber("00000003"),
                aProduct().withNumber("00000004"),
                aProduct().withNumber("00000005")
        );

        int count = Select.from(products)
                          .count(connection);
        assertThat("total products", count, is(5));
    }

    @Test
    public void
    countingOnlyDistinctRecords() {
        Product singleProduct = persist(aProduct().withNumber("00000001"));
        persist(anItem().withNumber("1").of(singleProduct));
        persist(anItem().withNumber("2").of(singleProduct));
        persist(anItem().withNumber("3").of(persist(aProduct().withNumber("00000002"))));
        persist(anItem().withNumber("4").of(persist(aProduct().withNumber("00000003"))));

        int count = Select.distinct(products)
                          .join(items, "items.product_id = products.id")
                          .count(connection);
        assertThat("distinct products", count, is(3));
    }

    private <T> T assertFound(Optional<T> record) {
        assertThat("found", record.isPresent(), is(true));
        return record.get();
    }

    @SafeVarargs
    private final <T> void persist(final Builder<T>... builders) {
        for (final Builder<T> builder : builders) {
            persist(builder);
        }
    }

    private <T> T persist(final Builder<T> builder) {
        return persist(builder.build());
    }

    private <T> T persist(final T entity) {
        return transactor.perform(() -> Insert.into(tableFor(entity), entity).execute(connection));
    }

    @SuppressWarnings("unchecked")
    private <T> Table<T> tableFor(T entity) {
        Map<Class, Table> tables = new HashMap<>();
        tables.put(Item.class, items);
        tables.put(Product.class, products);
        tables.put(CreditCardDetails.class, payments);
        tables.put(Order.class, orders);
        tables.put(LineItem.class, lineItems);
        return (Table<T>) tables.get(entity.getClass());
    }
}
