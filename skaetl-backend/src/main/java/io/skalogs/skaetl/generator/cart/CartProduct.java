package io.skalogs.skaetl.generator.cart;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CartProduct {
    private String name;
    private Integer price;
    private Integer stock;
}
