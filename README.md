## What is Tape?

Tape is a lightweight library for doing CRUD operations over JDBC with a fluent API.

I wrote tape because I wanted to do write persistence code using plain old JDBC instead of having to use an heavyweight ORM framework.

Tape is designed to be:
* Lightweight without any dependency
* Small and easy to understand in a short amount of time
* Transparent without magic happening behind the the scenes
* Fast to setup and fire so your database integration tests run fast
* Easy to get started with

Tape is not:
* An Object Relational Mapping (ORM) framework. There are already good ORM frameworks available
* An abstraction over SQL. The intention is to retain the simplicity and power of SQL as a language
* An object-oriented language over SQL
* A code generation framework. It does not try to generate Java representations of your database schema. You have to do that yourself.

The intention is simply to make it easier to work with JDBC for typical CRUD scenarios.

## What does it look like?

Following is a simple example of how Tape may be used:

```java
List<Item> bulldogs = Select.from(items).
                          join(products, "items.product_id = products.id").
                          where("products.name = ?", "Bulldog").
                          list(connection);
```

For more examples, the best is to have a look at the [tests](https://github.com/testinfected/tape/blob/master/src/test/java/com/vtence/tape/SelectionTest.java)
