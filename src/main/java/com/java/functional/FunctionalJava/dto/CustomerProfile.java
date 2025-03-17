package com.java.functional.FunctionalJava.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerProfile {
    private Customer customer;
    private String category;
    private List<String> preferences;

    public static CustomerProfile fromCustomer(Customer customer, String category) {
        return CustomerProfile.builder()
                .customer(customer)
                .category(category)
                .build();
    }
}
