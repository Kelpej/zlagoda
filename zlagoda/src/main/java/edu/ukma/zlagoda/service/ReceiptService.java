package edu.ukma.zlagoda.service;

import edu.ukma.zlagoda.dto.CheckoutRequest;
import edu.ukma.zlagoda.entities.*;
import edu.ukma.zlagoda.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReceiptService {
    private static final double VAT = 0.2;
    private final EmployeeRepository employeeRepository;
    private final CustomerCardRepository customerCardRepository;
    private final ProductInStoreRepository productInStoreRepository;
    private final SaleRepository saleRepository;
    private final ReceiptRepository receiptRepository;

    public Receipt checkout(CheckoutRequest request) {
        Employee employee = employeeRepository.findById(request.employee().getId()).orElseThrow();

        CustomerCard customerCard;
        if (request.customerCard() != null && request.customerCard().getNumber() != null) {
            customerCard = customerCardRepository.findByNumber(request.customerCard().getNumber()).orElseThrow();
        } else {
            customerCard = null;
        }

        if (checkForInvalidGoods(request.goods())) {
            throw new RuntimeException("Invalid cart contents");
        }

        List<ProductInStore> withDiscount = request.goods().stream().map(good -> {
            if (customerCard != null) {
                return good.toBuilder()
                        .price(good.getPrice() * (1 - customerCard.getDiscount() / (double) 100))
                        .build();
            }

            return good;
        }).toList();

        double total = withDiscount.stream()
                .mapToDouble(good -> good.getPrice() * good.getQuantity())
                .sum();

        Receipt receipt = Receipt.builder()
                .employee(employee)
                .customerCard(customerCard)
                .total(total)
                .vat(total * VAT)
                .dateOfPrinting(new Date())
                .build();

        long savedId = receiptRepository.save(receipt);

        Receipt saved = receiptRepository.findById(savedId);

        withDiscount.forEach(product -> {
            Sale sale = Sale.builder().product(product).receipt(saved).build();
            saleRepository.save(sale);
        });

        withDiscount.stream().map(good -> {
                    ProductInStore storedProduct = productInStoreRepository.findByUPC(good.getUpc()).orElseThrow();

                    return storedProduct.toBuilder()
                            .quantity(storedProduct.getQuantity() - good.getQuantity())
                            .build();
                })
                .forEach(productInStoreRepository::update);

        return receipt;
    }

    private boolean checkForInvalidGoods(List<ProductInStore> goods) {
        return goods.stream().anyMatch(good ->
                good.getQuantity() > productInStoreRepository.findByUPC(good.getUpc()).orElseThrow().getQuantity());
    }
}
