package edu.ukma.zlagoda.repositories;

import edu.ukma.zlagoda.entities.Address;
import edu.ukma.zlagoda.entities.Employee;
import edu.ukma.zlagoda.entities.EmployeeRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class EmployeeRepository {
    private static final String SELECT_ALL_SQL = "SELECT * FROM employees";
    private static final String SELECT_BY_ID_SQL = "SELECT * FROM employees WHERE id_employee = ?";
    private static final String INSERT_SQL = "INSERT INTO employees (empl_name, empl_surname, empl_patronymic, empl_role, salary, date_of_birth, date_of_start, phone_number, password, city, street, zip_code) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE employees SET empl_name = ?, empl_surname = ?, empl_patronymic = ?, empl_role = ?, salary = ?, date_of_birth = ?, " +
            "date_of_start = ?, phone_number = ?, password = ?, city = ?, street = ?, zip_code = ? WHERE id_employee = ?";
    private static final String DELETE_SQL = "DELETE FROM employees WHERE id_employee = ?";
    private static final String SELECT_BY_NUMBER = "SELECT * FROM employees WHERE phone_number = ?";

    private static final String COUNT_BEST = """
            SELECT COUNT(*) AS best
            FROM employees E
            WHERE NOT EXISTS (
                SELECT card_number
                FROM customer_cards C
                WHERE NOT EXISTS (
                    SELECT *
                    FROM receipts R
                    WHERE C.card_number = R.card_number
                        AND E.id_employee = R.id_employee
                    )
                ) AND empl_role = 'CASHIER';
            """;
    private final JdbcTemplate jdbcTemplate;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public EmployeeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Employee> rowMapper = (resultSet, i) ->
            Employee.builder()
                    .id(resultSet.getLong("id_employee"))
                    .name(resultSet.getString("empl_name"))
                    .surname(resultSet.getString("empl_surname"))
                    .patronymic(resultSet.getString("empl_patronymic"))
                    .role(EmployeeRole.valueOf(resultSet.getString("empl_role")))
                    .salary(resultSet.getLong("salary"))
                    .birthday(resultSet.getDate("date_of_birth"))
                    .employmentDay(resultSet.getDate("date_of_start"))
                    .phoneNumber(resultSet.getString("phone_number"))
                    .password(resultSet.getString("password"))
                    .city(resultSet.getString("city"))
                    .zipCode(resultSet.getString("zip_code"))
                    .street(resultSet.getString("street"))
                    .build();

    public List<Employee> findAll() {
        return jdbcTemplate.query(SELECT_ALL_SQL, rowMapper);
    }

    public Optional<Employee> findById(long id) {
        return jdbcTemplate.query(SELECT_BY_ID_SQL, new Object[]{id}, rowMapper).stream().findFirst();
    }

    public void save(Employee employee) {
        Address employeeAddress = employee.getAddress();

        jdbcTemplate.update(INSERT_SQL,
                employee.getName(), employee.getSurname(), employee.getPatronymic(),
                employee.getRole().toString(),
                employee.getSalary(),
                employee.getBirthday(),
                employee.getEmploymentDay(),
                employee.getPhoneNumber(),
                passwordEncoder.encode(employee.getPassword()),
                employeeAddress.getCity(), employeeAddress.getStreet(), employeeAddress.getZipCode());
    }

    public void update(Employee employee) {
        Address employeeAddress = employee.getAddress();

        jdbcTemplate.update(UPDATE_SQL,
                employee.getName(), employee.getSurname(), employee.getPatronymic(),
                employee.getRole().toString(),
                employee.getSalary(),
                employee.getBirthday(),
                employee.getEmploymentDay(),
                employee.getPhoneNumber(),
                passwordEncoder.encode(employee.getPassword()),
                employeeAddress.getCity(), employeeAddress.getStreet(), employeeAddress.getZipCode(), employee.getId());
    }

    public void delete(Long id) {
        jdbcTemplate.update(DELETE_SQL, id);
    }

    public Optional<Employee> findByNumber(String phoneNumber) {
        return jdbcTemplate.query(SELECT_BY_NUMBER, new Object[]{phoneNumber}, rowMapper).stream().findFirst();
    }

    public int countBest() {
        return jdbcTemplate.query(COUNT_BEST, rs -> {
            rs.next();
            return rs.getInt("best");
        });
    }
}
