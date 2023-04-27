package edu.ukma.zlagoda.rest;

import edu.ukma.zlagoda.dto.CreateProductInStoreWithName;
import edu.ukma.zlagoda.dto.ProductInStoreRequest;
import edu.ukma.zlagoda.entities.ProductInStore;
import edu.ukma.zlagoda.repositories.ProductInStoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products/store")
public class ProductInStoreController {
    private final ProductInStoreRepository productInStoreRepository;

    @Autowired
    public ProductInStoreController(ProductInStoreRepository productInStoreRepository) {
        this.productInStoreRepository = productInStoreRepository;
    }

    @GetMapping
    public List<CreateProductInStoreWithName> getProductsInStore() {
        return productInStoreRepository.findAllWithNames();
    }

    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    public ProductInStore createProductInStore(@RequestBody ProductInStoreRequest request) {
        ProductInStore product = ProductInStore.builder()
                .upc(request.upc())
                .productId(request.productId())
                .quantity(request.quantity())
                .onSale(request.onSale())
                .build();

        productInStoreRepository.save(product);
        return product;
    }

    @PutMapping
    @PreAuthorize("hasRole('MANAGER')")
    public ProductInStore editProductInStore(@RequestBody ProductInStore edited) {
        productInStoreRepository.update(edited);
        return edited;
    }

    @DeleteMapping("/{upc}")
    @PreAuthorize("hasRole('MANAGER')")

    public void deleteProductFromStore(@PathVariable String upc) {
        productInStoreRepository.deleteByUPC(upc);
    }
}
