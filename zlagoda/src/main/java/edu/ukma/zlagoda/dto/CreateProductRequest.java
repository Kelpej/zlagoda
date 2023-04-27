package edu.ukma.zlagoda.dto;

import edu.ukma.zlagoda.entities.Category;

public record CreateProductRequest(String name, String description, Category category) {

}
