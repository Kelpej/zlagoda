package edu.ukma.zlagoda.repositories;

import edu.ukma.zlagoda.dto.StatEntry;
import edu.ukma.zlagoda.entities.Address;
import edu.ukma.zlagoda.entities.CustomerCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CustomerCardRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CustomerCardRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final String INSERT_SQL = "INSERT INTO customer_cards (card_number, cust_name, cust_surname, cust_patronymic, phone_number, city, street, zip_code, percent) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private final String UPDATE_SQL = "UPDATE customer_cards SET cust_name = ?, cust_surname = ?, cust_patronymic = ?, " +
            "phone_number = ?, city = ?, street = ?, zip_code = ?, percent = ? WHERE card_number = ?";
    private final String DELETE_SQL = "DELETE FROM customer_cards WHERE card_number = ?";
    private final String SELECT_ALL_SQL = "SELECT * FROM customer_cards";

    private final String SELECT_BY_NUMBER_SQL = "SELECT * FROM customer_cards WHERE card_number = ?";

    private static final String STATS = """
            SELECT card_number,
                   cust_name,
                   cust_surname,
                   sum((SELECT sum(sum_total)
                        FROM receipts R
                        WHERE R.card_number = C.card_number
                        GROUP BY card_number)) AS sum
            FROM customer_cards C
            group by card_number;
            """;

    private static final String COUNT_BEST_CLIENTS_BY_CASHIERS = """
            SELECT COUNT(*) AS best
            FROM customer_cards C
            WHERE NOT EXISTS (
                SELECT id_employee
                FROM employees E
                WHERE NOT EXISTS (
                    SELECT *
                    FROM receipts R
                    WHERE C.card_number = R.card_number
                        AND E.id_employee = R.id_employee
                    )
                );
            """;

    private static final String COUNT_BEST_CLIENTS_BY_PRODUCTS = """
            SELECT COUNT(*) AS best
            FROM customer_cards C
            WHERE NOT EXISTS (
                           SELECT upc
                           FROM products_in_store P
                           WHERE NOT EXISTS (
                                   SELECT upc
                                   FROM sales S
                                   WHERE P.upc = S.upc AND receipt_id IN (
                                            SELECT receipt_id
                                            FROM receipts R
                                            WHERE R.card_number = C.card_number
                                    )
                            )
            );
            """;

    public static final RowMapper<CustomerCard> ROW_MAPPER = (resultSet, i) ->
            CustomerCard.builder()
                    .number(resultSet.getString("card_number"))
                    .holderName(resultSet.getString("cust_name"))
                    .holderSurname(resultSet.getString("cust_surname"))
                    .holderPatronymic(resultSet.getString("cust_patronymic"))
                    .city(resultSet.getString("city"))
                    .street(resultSet.getString("street"))
                    .zipCode(resultSet.getString("zip_code"))
                    .discount(resultSet.getInt("percent"))
                    .build();

    public CustomerCard save(CustomerCard customerCard) {
        Address customerAddress = customerCard.getAddress();

        jdbcTemplate.update(INSERT_SQL,
                customerCard.getNumber(),
                customerCard.getHolderName(), customerCard.getHolderSurname(), customerCard.getHolderPatronymic(),
                customerCard.getHolderPhoneNumber(),
                customerAddress.getCity(), customerAddress.getStreet(), customerAddress.getZipCode(),
                customerCard.getDiscount());

        return customerCard;
    }

    public void update(CustomerCard customerCard) {
        Address customerAddress = customerCard.getAddress();

        jdbcTemplate.update(UPDATE_SQL,
                customerCard.getHolderName(), customerCard.getHolderSurname(), customerCard.getHolderPatronymic(),
                customerCard.getHolderPhoneNumber(),
                customerAddress.getCity(), customerAddress.getStreet(), customerAddress.getZipCode(),
                customerCard.getDiscount(), customerCard.getNumber());
    }

    public void deleteByNumber(String number) {
        jdbcTemplate.update(DELETE_SQL, number);
    }

    public List<CustomerCard> findAll() {
        return jdbcTemplate.query(SELECT_ALL_SQL, ROW_MAPPER);
    }

    public Optional<CustomerCard> findByNumber(String number) {
        List<CustomerCard> customerCards = jdbcTemplate.query(SELECT_BY_NUMBER_SQL, ROW_MAPPER, number);
        return Optional.ofNullable(customerCards.get(0));
    }

    public List<StatEntry> getStats() {
        return jdbcTemplate.query(STATS, (rs, i) -> {
            String name = rs.getString("cust_surname") + " " + rs.getString("cust_name");
            String value = String.valueOf(rs.getDouble("sum"));
            return new StatEntry(name, value);
        });
    }

    public int countBestClientsWhoBoughFromAllCashiers() {
        return jdbcTemplate.query(COUNT_BEST_CLIENTS_BY_CASHIERS, rs -> {
            rs.next();
            return rs.getInt("best");
        });
    }

    public int countBestClientsByProducts() {
        return jdbcTemplate.query(COUNT_BEST_CLIENTS_BY_PRODUCTS, rs -> {
            rs.next();
            return rs.getInt("best");
        });
    }
}
