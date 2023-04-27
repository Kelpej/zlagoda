package edu.ukma.zlagoda.dto;

public record ProductInStoreRequest(String upc,
                                    Long productId,
                                    int quantity,
                                    double price,
                                    boolean onSale) {
}
