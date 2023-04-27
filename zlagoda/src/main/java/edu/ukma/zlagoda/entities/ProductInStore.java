package edu.ukma.zlagoda.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "products_in_store")
@Builder(toBuilder = true)
@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductInStore {
    @Id
    @GeneratedValue(generator = "increment")
    @Column(name = "upc")
    private String upc;

    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private long productId;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "price")
    private double price;

    @Column(name = "on_sale")
    private boolean onSale;
}
