package edu.ukma.zlagoda.repositories;

import edu.ukma.zlagoda.entities.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CategoryRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CategoryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String SELECT_ALL_CATEGORIES_SQL = "SELECT * FROM categories";
    private static final String SELECT_CATEGORY_BY_ID_SQL = "SELECT * FROM categories WHERE id = ?";
    private static final String INSERT_CATEGORY_SQL = "INSERT INTO categories(name) VALUES(?)";
    private static final String UPDATE_CATEGORY_SQL = "UPDATE categories SET name = ? WHERE id = ?";
    private static final String DELETE_CATEGORY_SQL = "DELETE FROM categories WHERE id = ?";

    private static final RowMapper<Category> rowMapper = (resultSet, i) ->
            Category.builder()
            .id(resultSet.getLong("id"))
            .name(resultSet.getString("name"))
            .build();

    public List<Category> findAll() {
        return jdbcTemplate.query(SELECT_ALL_CATEGORIES_SQL, rowMapper);
    }

    public Optional<Category> findById(long id) {
        List<Category> categories = jdbcTemplate.query(SELECT_CATEGORY_BY_ID_SQL, new Object[]{id}, rowMapper);
        return Optional.ofNullable(categories.get(0));
    }

    public Category save(Category category) {
        jdbcTemplate.update(INSERT_CATEGORY_SQL, category.getName());
        return category;
    }

    public void update(Category category) {
        jdbcTemplate.update(UPDATE_CATEGORY_SQL, category.getName(), category.getId());
    }

    public void delete(Long id) {
        jdbcTemplate.update(DELETE_CATEGORY_SQL, id);
    }
}
