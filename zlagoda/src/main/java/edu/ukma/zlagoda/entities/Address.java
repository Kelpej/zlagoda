package edu.ukma.zlagoda.entities;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Address {
    String city;
    String street;
    String zipCode;
}
