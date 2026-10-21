package com.society.management.dto;

import com.society.management.entity.Society;

import java.time.OffsetDateTime;

public record SocietyResponse(
    Long id,
    String name,
    String registrationNumber,
    String address,
    String city,
    String state,
    String postalCode,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt
) {
    public static SocietyResponse fromEntity(Society society) {
        if (society == null) {
            return null;
        }
        return new SocietyResponse(
            society.getId(),
            society.getName(),
            society.getRegistrationNumber(),
            society.getAddress(),
            society.getCity(),
            society.getState(),
            society.getPostalCode(),
            society.getCreatedAt(),
            society.getUpdatedAt()
        );
    }
}
