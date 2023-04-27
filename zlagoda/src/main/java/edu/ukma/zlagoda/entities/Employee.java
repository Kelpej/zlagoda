package edu.ukma.zlagoda.entities;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;

@Entity
@Table(name = "employees")
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Employee {
    @Id
    @GeneratedValue(generator = "increment")
    @Column(name = "id_employee", nullable = false)
    private long id;

    @Column(name = "empl_name", nullable = false)
    private String name;

    @Column(name = "empl_surname", nullable = false)
    private String surname;

    @Column(name = "empl_patronymic")
    private String patronymic;

    @Column(name = "empl_role", nullable = false)
    private EmployeeRole role;

    @Column(name = "salary", nullable = false)
    private long salary;

    @Column(name = "date_of_birth", nullable = false)
    private Date birthday;

    @Column(name = "date_of_start", nullable = false)
    private Date employmentDay;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "city")
    @Getter(AccessLevel.NONE)
    private String city;

    @Column(name = "street")
    @Getter(AccessLevel.NONE)
    private String street;

    @Column(name = "zip_code")
    @Getter(AccessLevel.NONE)
    private String zipCode;

    public Address getAddress() {
        return Address.builder()
                .city(city)
                .street(street)
                .zipCode(zipCode)
                .build();
    }
}
