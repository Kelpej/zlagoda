package edu.ukma.zlagoda.repositories;

import edu.ukma.zlagoda.entities.Category;
import edu.ukma.zlagoda.entities.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepository {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Product> rowMapper;

    @Autowired
    public ProductRepository(JdbcTemplate jdbcTemplate, CategoryRepository categoryRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = (resultSet, i) -> {
            Category category = categoryRepository
                    .findById(resultSet.getLong("category_id"))
                    .orElseThrow();

            return Product.builder()
                    .id(resultSet.getLong("id"))
                    .category(category)
                    .name(resultSet.getString("name"))
                    .description(resultSet.getString("description"))
                    .build();
        };
    }

    private static final String SELECT_ALL_PRODUCTS =
            "SELECT id, category_id, name, description FROM products";
    private static final String SELECT_PRODUCT_BY_ID =
            "SELECT id, category_id, name, description FROM products WHERE id = ?";
    private static final String INSERT_PRODUCT =
            "INSERT INTO products (category_id, name, description) VALUES (?, ?, ?)";
    private static final String UPDATE_PRODUCT =
            "UPDATE products SET category_id = ?, name = ?, description = ? WHERE id = ?";

    private static final String DELETE_PRODUCT =
            "DELETE FROM products WHERE id = ?";

    public List<Product> findAll() {
        return jdbcTemplate.query(SELECT_ALL_PRODUCTS, rowMapper);
    }

    public Optional<Product> findById(Long id) {
        return jdbcTemplate.query(SELECT_PRODUCT_BY_ID, rowMapper, id).stream().findFirst();
    }

    public void save(Product product) {
        jdbcTemplate.update(INSERT_PRODUCT, product.getCategory().getId(), product.getName(), product.getDescription());
    }

    public void update(Product product) {
        jdbcTemplate.update(UPDATE_PRODUCT, product.getCategory().getId(), product.getName(), product.getDescription(), product.getId());
    }

    public void delete(Long id) {
        jdbcTemplate.update(DELETE_PRODUCT, id);
    }
}
