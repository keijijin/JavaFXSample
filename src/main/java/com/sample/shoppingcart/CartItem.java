package com.sample.shoppingcart;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.math.BigDecimal;

public class CartItem {
    private final SimpleStringProperty itemName = new SimpleStringProperty("");
    private final SimpleIntegerProperty quantity = new SimpleIntegerProperty(0);
    private final SimpleObjectProperty<BigDecimal> price = new SimpleObjectProperty<BigDecimal>();

    public CartItem() {}

    public CartItem(String n, int q, BigDecimal p) {
        setItemName(n);
        setQuantity(q);
        setPrice(p);
    }

    public void setPrice(BigDecimal p) {
        this.price.setValue(p);
    }

    public BigDecimal getPrice() {
        return this.price.get();
    }

    public void setQuantity(int q) {
        this.quantity.set(q);
    }
    public int getQuantity() {
        return this.quantity.get();
    }

    public void setItemName(String n) {
        this.itemName.set(n);
    }
    public String getItemName() {
        return this.itemName.get();
    }
}
