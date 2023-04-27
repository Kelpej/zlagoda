package edu.ukma.zlagoda.rest;

import edu.ukma.zlagoda.dto.CreateCategoryRequest;
import edu.ukma.zlagoda.entities.Category;
import edu.ukma.zlagoda.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoriesController {

    private final CategoryRepository categoryRepository;

    @GetMapping
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @PostMapping
    public Category createCategory(@RequestBody CreateCategoryRequest request) {
        Category category = Category.builder()
                .name(request.name())
                .build();

        return categoryRepository.save(category);
    }

    @PutMapping
    public Category editCategory(@RequestBody Category edited) {
        categoryRepository.update(edited);
        return edited;
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id) {
        categoryRepository.delete(id);
    }
}
