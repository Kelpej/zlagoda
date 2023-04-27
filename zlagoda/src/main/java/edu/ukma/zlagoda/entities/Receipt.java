package edu.ukma.zlagoda.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "receipts")
@Builder
@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Receipt {
    @Id
    @GeneratedValue(generator = "iterator")
    @Column(name = "check_number", nullable = false)
    private long id;

    @ManyToOne
    @JoinColumn(name = "id_employee", nullable = false)
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "card_number")
    private CustomerCard customerCard;

    @Column(name = "print_date", nullable = false)
    private Date dateOfPrinting;

    @Column(name = "sum_total", nullable = false)
    private double total;

    @Column(name = "vat", nullable = false)
    private double vat;
}
