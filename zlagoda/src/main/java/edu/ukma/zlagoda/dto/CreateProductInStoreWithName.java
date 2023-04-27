package edu.ukma.zlagoda.dto;

import lombok.Builder;

@Builder
public record CreateProductInStoreWithName(String upc,
                                           Long productId,
                                           String name,
                                           int quantity,
                                           double price,
                                           boolean onSale) {

}
