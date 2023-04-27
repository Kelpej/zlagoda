package edu.ukma.zlagoda.rest;

import edu.ukma.zlagoda.dto.CreateProductRequest;
import edu.ukma.zlagoda.entities.Product;
import edu.ukma.zlagoda.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductRepository productRepository;

    @GetMapping()
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable Long id) {
        return productRepository.findById(id).orElseThrow();
    }

    @PostMapping
    public Product createProduct(@RequestBody CreateProductRequest request) {
        Product product = Product.builder()
                .name(request.name())
                .description(request.description())
                .category(request.category())
                .build();

        productRepository.save(product);
        return product;
    }

    @PutMapping
    @PreAuthorize("hasRole('MANAGER')")
    public Product editProduct(@RequestBody Product edited) {
        productRepository.update(edited);
        return edited;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public void deleteProduct(@PathVariable Long id) {
        productRepository.delete(id);
    }
}
