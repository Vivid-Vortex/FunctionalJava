package com.java.functional.FunctionalJava.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class Employees implements Comparable<Employees> {
    private Integer id;
    private String name;
    private String grade;
    private double salary;

    @Override
    public int compareTo(Employees other) {
        return this.id.compareTo(other.id);
    }
}