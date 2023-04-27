package edu.ukma.zlagoda.entities;

import lombok.Builder;

@Builder
public record Personality(
        String name,
        String surname,
        String patronymic,
        String phoneNumber
) {
}
