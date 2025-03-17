package com.java.functional.FunctionalJava.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeSummary {
    private String grade;
    private Double averageSalary;
    private Double minSalary;
    private Double maxSalary;
    private Long count;
}