package edu.ukma.zlagoda.repositories;

import edu.ukma.zlagoda.dto.CreateProductInStoreWithName;
import edu.ukma.zlagoda.entities.ProductInStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductInStoreRepository {

    private static final String SELECT_BY_UPC_SQL = "SELECT * FROM products_in_store WHERE upc = ?";
    private static final RowMapper<ProductInStore> PRODUCT_MAPPER = (rs, rowNum) ->
            ProductInStore.builder()
                    .upc(rs.getString("upc"))
                    .productId(rs.getLong("product_id"))
                    .quantity(rs.getInt("quantity"))
                    .price(rs.getDouble("price"))
                    .onSale(rs.getBoolean("on_sale"))
                    .build();

    private static final RowMapper<CreateProductInStoreWithName> PRODUCT_WITH_NAME_MAPPER = (rs, rowNum) ->
            CreateProductInStoreWithName.builder()
                    .upc(rs.getString("upc"))
                    .productId(rs.getLong("product_id"))
                    .quantity(rs.getInt("quantity"))
                    .name(rs.getString("name"))
                    .price(rs.getDouble("price"))
                    .onSale(rs.getBoolean("on_sale"))
                    .build();

    private static final String INSERT_SQL = "INSERT INTO products_in_store (upc, product_id, quantity, price, on_sale) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_ALL_SQL = "SELECT * FROM products_in_store";
    private static final String UPDATE_SQL = "UPDATE products_in_store SET product_id = ?, quantity = ?, price = ?, on_sale = ? WHERE upc = ?";
    private static final String DELETE_BY_UPC_SQL = "DELETE FROM products_in_store WHERE upc = ?";
    private static final String SELECT_ALL_WITH_NAMES = "SELECT products_in_store.*, products.id, products.name FROM products_in_store INNER JOIN products ON products_in_store.product_id = products.id;";
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ProductInStoreRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public ProductInStore save(ProductInStore productInStore) {
        jdbcTemplate.update(INSERT_SQL,
                productInStore.getUpc(),
                productInStore.getProductId(),
                productInStore.getQuantity(),
                productInStore.getPrice(),
                productInStore.isOnSale());

        return productInStore;
    }

    public List<ProductInStore> findAll() {
        return jdbcTemplate.query(SELECT_ALL_SQL, PRODUCT_MAPPER);
    }

    public List<CreateProductInStoreWithName> findAllWithNames() {
        return jdbcTemplate.query(SELECT_ALL_WITH_NAMES, PRODUCT_WITH_NAME_MAPPER);
    }


    public Optional<ProductInStore> findByUPC(String upc) {
        return jdbcTemplate.query(SELECT_BY_UPC_SQL, new Object[]{upc}, PRODUCT_MAPPER).stream().findFirst();
    }

    public void update(ProductInStore productInStore) {
        jdbcTemplate.update(UPDATE_SQL,
                productInStore.getProductId(),
                productInStore.getQuantity(),
                productInStore.getPrice(),
                productInStore.isOnSale(),
                productInStore.getUpc());
    }

    public void deleteByUPC(String upc) {
        jdbcTemplate.update(DELETE_BY_UPC_SQL, upc);
    }
}