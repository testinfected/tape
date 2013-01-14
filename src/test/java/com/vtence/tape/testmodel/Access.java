package com.vtence.tape.testmodel;

import com.vtence.tape.support.FieldAccessor;

public class Access {

    public static FieldAccessor<Long> idOf(Object entity) {
        return FieldAccessor.access(entity, "id");
    }

    public static FieldAccessor<Product> productOf(Object entity) {
        return FieldAccessor.access(entity, "product");
    }

    public static FieldAccessor<Order> orderOf(Object entity) {
        return FieldAccessor.access(entity, "order");
    }

    private Access() {}
}