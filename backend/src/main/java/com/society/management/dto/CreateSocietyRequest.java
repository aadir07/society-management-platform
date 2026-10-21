package com.society.management.dto;

import com.society.management.entity.Society;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateSocietyRequest(
    @NotBlank(message = "Name is required")
    @Size(max = 150, message = "Name must not exceed 150 characters")
    String name,

    @NotBlank(message = "Registration number is required")
    @Size(max = 100, message = "Registration number must not exceed 100 characters")
    String registrationNumber,

    @NotBlank(message = "Address is required")
    @Size(max = 255, message = "Address must not exceed 255 characters")
    String address,

    @NotBlank(message = "City is required")
    @Size(max = 100, message = "City must not exceed 100 characters")
    String city,

    @NotBlank(message = "State is required")
    @Size(max = 100, message = "State must not exceed 100 characters")
    String state,

    @NotBlank(message = "Postal code is required")
    @Size(max = 20, message = "Postal code must not exceed 20 characters")
    String postalCode
) {
    public Society toEntity() {
        return new Society(name, registrationNumber, address, city, state, postalCode);
    }
}
