package edu.ukma.zlagoda.repositories;

import edu.ukma.zlagoda.dto.DateRange;
import edu.ukma.zlagoda.dto.StatEntry;
import edu.ukma.zlagoda.entities.CustomerCard;
import edu.ukma.zlagoda.entities.Employee;
import edu.ukma.zlagoda.entities.Receipt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ReceiptRepository {
    private final JdbcTemplate jdbcTemplate;

    private static final RowMapper<Receipt> ROW_MAPPER = (rs, i) -> {
        Employee employee = Employee.builder()
                .id(rs.getLong("id_employee"))
                .build();

        String cardNumber = rs.getString("card_number");

        CustomerCard card = CustomerCard.builder().number(cardNumber).build();

        return Receipt.builder()
                .id(rs.getLong("check_number"))
                .employee(employee)
                .customerCard(card)
                .dateOfPrinting(rs.getDate("print_date"))
                .total(rs.getDouble("sum_total"))
                .vat(rs.getDouble("vat"))
                .build();
    };

    private static final RowMapper<Receipt> ROW_MAPPER_FULL_INFO = (rs, i) -> {
        Employee employee = Employee.builder()
                .id(rs.getLong("id_employee"))
                .name(rs.getString("empl_name"))
                .surname(rs.getString("empl_surname"))
                .build();

        CustomerCard card = CustomerCard.builder()
                .number(rs.getString("card_number"))
                .holderName(rs.getString("cust_name"))
                .holderSurname(rs.getString("cust_surname"))
                .build();

        return Receipt.builder()
                .id(rs.getLong("check_number"))
                .employee(employee)
                .customerCard(card)
                .dateOfPrinting(rs.getDate("print_date"))
                .total(rs.getDouble("sum_total"))
                .vat(rs.getDouble("vat"))
                .build();
    };

    @Autowired
    public ReceiptRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String SELECT_BY_EMPLOYEE_SQL = "SELECT * FROM receipts " +
            "WHERE id_employee = ?";
    private static final String SELECT_ALL_SQL = """
            SELECT receipts.*, customer_cards.cust_name, customer_cards.cust_surname, employees.empl_name, employees.empl_surname
            FROM receipts
            LEFT JOIN customer_cards
            ON receipts.card_number = customer_cards.card_number
            LEFT JOIN employees
            ON receipts.id_employee = employees.id_employee;
            """;
    private static final String SELECT_BY_ID_SQL = "SELECT * FROM receipts WHERE check_number = ?";
    private static final String INSERT_SQL = "INSERT INTO receipts (id_employee, card_number, print_date, sum_total, vat) " +
            "VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE receipts SET id_employee = ?, card_number = ?, print_date = ?, sum_total = ?, vat = ? WHERE check_number = ?";
    private static final String DELETE_SQL = "DELETE FROM receipts WHERE check_number = ?";

    private static final String SUM_FOR_PERIOD = """
            SELECT COALESCE(sum((SELECT sum(sum_total)
                                 FROM receipts
                                 WHERE employees.id_employee = receipts.id_employee AND print_date BETWEEN (?) AND (?)
                                 GROUP BY id_employee)),
                            0) AS receipts_sum
            FROM employees
            WHERE empl_role = 'CASHIER';
            """;

    private static final String CASHIER_STATS_FOR_PERIOD = """
                SELECT id_employee,
                       empl_surname,
                       empl_name,
                       COALESCE(sum((SELECT sum(sum_total)
                                     FROM receipts
                                     WHERE receipts.id_employee = employees.id_employee
                                       AND print_date BETWEEN (?) AND (?)
                                     GROUP BY id_employee)),
                                0) AS receipts_sum
                FROM employees
                WHERE empl_role = 'CASHIER'
                group by id_employee;
            """;

    public long save(Receipt receipt) {
        jdbcTemplate.update(INSERT_SQL,
                receipt.getEmployee().getId(), Optional.ofNullable(receipt.getCustomerCard()).map(CustomerCard::getNumber).orElse(null),
                receipt.getDateOfPrinting(), receipt.getTotal(), receipt.getVat());

        return jdbcTemplate.query("SELECT max(check_number) FROM receipts", rs -> {
            rs.next();
            return rs.getLong(1);
        });
    }

    public List<Receipt> findAll() {
        return jdbcTemplate.query(SELECT_ALL_SQL, ROW_MAPPER_FULL_INFO);
    }

    public Receipt findById(long id) {
        return jdbcTemplate.query(SELECT_BY_ID_SQL, ROW_MAPPER, id).stream().findFirst().orElseThrow();
    }

    public List<Receipt> findByEmployeeId(long employeeId) {
        return jdbcTemplate.query(SELECT_BY_EMPLOYEE_SQL, ROW_MAPPER, employeeId);
    }

    public void update(Receipt receipt) {
        jdbcTemplate.update(UPDATE_SQL,
                receipt.getEmployee().getId(), receipt.getCustomerCard().getNumber(),
                receipt.getDateOfPrinting(), receipt.getTotal(), receipt.getVat(), receipt.getId());
    }

    public void delete(long id) {
        jdbcTemplate.update(DELETE_SQL, id);
    }

    public double sum(DateRange dateRange) {
        return jdbcTemplate.query(SUM_FOR_PERIOD, rs -> {
            rs.next();
            return rs.getDouble("receipts_sum");
        }, dateRange.from(), dateRange.to());
    }

    public List<StatEntry> getStatistics(DateRange dateRange) {
        return jdbcTemplate.query(CASHIER_STATS_FOR_PERIOD, (rs, i) -> {
            String name = rs.getString("empl_surname") + " " + rs.getString("empl_name");
            double sold = rs.getDouble("receipts_sum");
            return new StatEntry(name, String.valueOf(sold));
        }, dateRange.from(), dateRange.to());
    }
}
