[![Build Status](https://travis-ci.org/testinfected/tape.png?branch=master)](https://travis-ci.org/testinfected/tape)

## What is Tape?

Tape is a lightweight data mapping library, designed as a fluent API over JDBC.

## What does it look like?

Following is a simple illustration of how Tape may be used.

```java
// Describe the mapping for Items and Products
Table<Product> products = ...
Table<Item> items = ...

// Select all items whose product name is 'Bulldog' using the given connection
List<Item> bulldogs = Select.from(items).
                          join(products, "items.product_id = products.id").
                          where("products.name = ?", "Bulldog").
                          list(connection);
```

## How does it work?

Tape is designed to be:
* Lightweight - without any dependency
* Small - easy to understand in a short amount of time
* Transparent - without magic happening behind the the scenes
* Fast to setup and fire - so your database integration tests run fast
* Easy to get started with

Tape is not:
* An Object Relational Mapping (ORM) framework. There are already good ORM frameworks available
* An abstraction over SQL. The intention is to retain the simplicity and power of SQL as a language
* An object-oriented language over SQL
* A code generation framework. It does not try to generate Java representations of your database schema. You have to do that yourself.

The intention is simply to make it fun and easy to write data access code without the need to resort to an ORM framework.

## Learn More

For further details and other examples, head to the [Wiki](https://github.com/testinfected/tape/wiki) or take a look at the [tests](https://github.com/testinfected/tape/blob/master/src/test/java/com/vtence/tape).
