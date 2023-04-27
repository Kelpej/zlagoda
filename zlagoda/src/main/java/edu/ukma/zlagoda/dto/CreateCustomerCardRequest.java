package edu.ukma.zlagoda.dto;

public record CreateCustomerCardRequest(String number,
                                        String holderName,
                                        String holderSurname,
                                        String holderPatronymic,
                                        String holderPhoneNumber,
                                        String city,
                                        String street,
                                        String zipCode,
                                        int discount) {
}
