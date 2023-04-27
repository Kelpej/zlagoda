package edu.ukma.zlagoda.rest;

import edu.ukma.zlagoda.dto.CreateCustomerCardRequest;
import edu.ukma.zlagoda.dto.StatEntry;
import edu.ukma.zlagoda.entities.CustomerCard;
import edu.ukma.zlagoda.repositories.CustomerCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerCardController {
    private final CustomerCardRepository customerCardRepository;

    @GetMapping
    public List<CustomerCard> getAllCustomerCards() {
        return customerCardRepository.findAll();
    }

    @PostMapping
    public CustomerCard createCustomerCard(@RequestBody CreateCustomerCardRequest request) {
        CustomerCard card = CustomerCard.builder()
                .number(request.number())
                .holderName(request.holderName())
                .holderSurname(request.holderSurname())
                .holderPatronymic(request.holderPatronymic())
                .holderPhoneNumber(request.holderPhoneNumber())
                .city(request.city())
                .zipCode(request.zipCode())
                .street(request.city())
                .discount(request.discount())
                .build();

        return customerCardRepository.save(card);
    }

    @PutMapping
    public CustomerCard editCustomerCard(@RequestBody CustomerCard edited) {
        customerCardRepository.update(edited);
        return edited;
    }

    @DeleteMapping("/{number}")
    public void deleteCustomerCard(@PathVariable String number) {
        customerCardRepository.deleteByNumber(number);
    }

    @GetMapping("/stats")
    public List<StatEntry> statistics() {
        return customerCardRepository.getStats();
    }

    @GetMapping("/count_best_by_cashiers")
    public int countBestByCashiers() {
        return customerCardRepository.countBestClientsWhoBoughFromAllCashiers();
    }

    @GetMapping("/count_best_by_products")
    public int countBestByProducts() {
        return customerCardRepository.countBestClientsByProducts();
    }
}
