package edu.ukma.zlagoda.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "customer_cards")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerCard {
    @Id
    @Column(name = "card_number", nullable = false)
    private String number;

    @Column(name = "cust_name", nullable = false)
    private String holderName;

    @Column(name = "cust_surname", nullable = false)
    private String holderSurname;


    @Column(name = "cust_patronymic")
    private String holderPatronymic;

    @Column(name = "phone_number", nullable = false)
    private String holderPhoneNumber;

    @Column(name = "city", nullable = false)
    @Getter(AccessLevel.NONE)
    private String city;

    @Column(name = "street", nullable = false)
    @Getter(AccessLevel.NONE)
    private String street;

    @Column(name = "zip_code", nullable = false)
    @Getter(AccessLevel.NONE)
    private String zipCode;

    @Column(name = "percent", nullable = false)
    private int discount;

    public Address getAddress() {
        return Address.builder()
                .city(city)
                .street(street)
                .zipCode(zipCode)
                .build();
    }
}
