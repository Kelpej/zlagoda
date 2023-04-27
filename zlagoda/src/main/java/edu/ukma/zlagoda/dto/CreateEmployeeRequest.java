package edu.ukma.zlagoda.dto;

import edu.ukma.zlagoda.entities.Address;
import edu.ukma.zlagoda.entities.EmployeeRole;
import edu.ukma.zlagoda.entities.Personality;
import lombok.ToString;

import java.sql.Date;

@ToString
public class CreateEmployeeRequest {
    private final String name;
    private final String surname;
    private final String patronymic;
    private final String role;
    private final long salary;
    private final Date birthday;
    private final Date employmentDay;
    private final String phoneNumber;
    private final String password;
    private final String city;
    private final String street;
    private final String zipCode;

    public CreateEmployeeRequest(String name,
                                 String surname,
                                 String patronymic,
                                 String role,
                                 long salary,
                                 Date birthday,
                                 Date employmentDay,
                                 String phoneNumber,
                                 String password, String city,
                                 String street,
                                 String zipCode) {
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.role = role;
        this.salary = salary;
        this.birthday = birthday;
        this.employmentDay = employmentDay;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.city = city;
        this.street = street;
        this.zipCode = zipCode;
    }

    public Address address() {
        return Address.builder()
                .city(city)
                .street(street)
                .zipCode(zipCode)
                .build();
    }

    public Personality personality() {
        return Personality.builder()
                .name(name)
                .surname(surname)
                .patronymic(patronymic)
                .phoneNumber(phoneNumber)
                .build();
    }

    public String password() {
        return this.password;
    }

    public EmployeeRole role() {
        return EmployeeRole.valueOf(role.trim().toUpperCase());
    }

    public long salary() {
        return salary;
    }

    public Date birthday() {
        return birthday;
    }

    public Date employmentDay() {
        return employmentDay;
    }
}
