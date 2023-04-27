package edu.ukma.zlagoda.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sales")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Sale {
    @Id
    @ManyToOne
    private Receipt receipt;

    @ManyToOne
    private ProductInStore product;
}
