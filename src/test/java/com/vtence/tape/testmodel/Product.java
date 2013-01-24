package com.vtence.tape.testmodel;

public class Product extends Entity {

    private final String number;
    private String name;
    private String description;

    public Product(String number, String name, String description) {
        this.number = number;
        this.name = name;
        this.description = description;
    }

    public String getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        if (number != null ? !number.equals(product.number) : product.number != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return number != null ? number.hashCode() : 0;
    }

    @Override
    public String toString() {
        return number + " (" + name + ")";
    }
}
