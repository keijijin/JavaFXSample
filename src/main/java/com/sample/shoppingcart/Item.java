package com.sample.shoppingcart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    private String name;
    private String unit;
    private int unitQuantity;
    private BigDecimal price;
    private String picture;

    public String toString() {
        return this.name;
    }
}
