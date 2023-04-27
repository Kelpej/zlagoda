package edu.ukma.zlagoda.repositories;

import edu.ukma.zlagoda.entities.ProductInStore;
import edu.ukma.zlagoda.entities.Receipt;
import edu.ukma.zlagoda.entities.Sale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SaleRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public SaleRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Sale sale) {
        String sql = "INSERT INTO sales (receipt_id, upc, quantity, price) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                sale.getReceipt().getId(),
                sale.getProduct().getUpc(),
                sale.getProduct().getQuantity(),
                sale.getProduct().getPrice());
    }

    public List<Sale> findByReceiptId(Long receiptId) {
        String sql = "SELECT * FROM sales WHERE receipt_id = ?";
        return jdbcTemplate.query(sql, new Object[]{receiptId}, (rs, rowNum) ->
                Sale.builder()
                        .receipt(
                                Receipt.builder()
                                .id(rs.getLong("receipt_id"))
                                .build()
                        )
                        .product(
                                ProductInStore.builder()
                                        .upc(rs.getString("upc"))
                                        .build())
                        .build()
        );
    }

    public List<Sale> findByProductId(Long productId) {
        String sql = "SELECT * FROM sales WHERE upc = ?";
        return jdbcTemplate.query(sql, new Object[]{productId}, (rs, rowNum) ->
                Sale.builder()
                        .receipt(Receipt.builder().id(rs.getLong("receipt_id")).build())
                        .product(ProductInStore.builder().upc(rs.getString("upc")).build())
                        .build());
    }
}
