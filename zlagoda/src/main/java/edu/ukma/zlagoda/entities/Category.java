package edu.ukma.zlagoda.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Category {
    @Id
    @GeneratedValue(generator = "increment")
    @Column(nullable = false)
    private long id;

    @Column(nullable = false)
    private String name;
}
