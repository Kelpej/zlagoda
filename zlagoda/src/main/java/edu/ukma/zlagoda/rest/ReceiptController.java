package edu.ukma.zlagoda.rest;

import edu.ukma.zlagoda.dto.CheckoutRequest;
import edu.ukma.zlagoda.dto.DateRange;
import edu.ukma.zlagoda.dto.StatEntry;
import edu.ukma.zlagoda.entities.Receipt;
import edu.ukma.zlagoda.repositories.ReceiptRepository;
import edu.ukma.zlagoda.service.ReceiptService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/receipts")
@RequiredArgsConstructor
public class ReceiptController {
    private final ReceiptRepository receiptRepository;
    private final ReceiptService receiptService;

    @GetMapping
    public List<Receipt> getReceipts() {
        return receiptRepository.findAll();
    }

    @PostMapping
    public Receipt checkout(@RequestBody CheckoutRequest request) {
        return receiptService.checkout(request);
    }

    @PutMapping
    @PreAuthorize("hasRole('MANAGER')")
    public Receipt editReceipt(@RequestBody Receipt edited) {
        receiptRepository.update(edited);
        return edited;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public void deleteProductFromStore(@PathVariable Long id) {
        receiptRepository.delete(id);
    }

    @PostMapping("/sum")
    @PreAuthorize("hasRole('MANAGER')")
    public double sumForPeriod(@RequestBody DateRange dateRange) {
        return receiptRepository.sum(dateRange);
    }

    @PostMapping("/stats")
    @PreAuthorize("hasRole('MANAGER')")
    public List<StatEntry>cashierStats(@RequestBody DateRange dateRange) {
        return receiptRepository.getStatistics(dateRange);
    }
}
