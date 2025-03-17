package com.java.functional.FunctionalJava.streams;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class ModernJavaFeaturesDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // Initialize dependencies
        DbLayer dbLayer = new DbLayer();
        ModernJavaFunctionalFeatures modernFeatures = new ModernJavaFunctionalFeatures(dbLayer);

        System.out.println("\n===== Java 9: Collection Factory Methods =====");
        List<String> languages = modernFeatures.getImmutableList();
        System.out.println("Immutable List: " + languages);

        Map<String, Integer> languageYears = modernFeatures.getImmutableMap();
        System.out.println("Immutable Map: " + languageYears);

        System.out.println("\n===== Java 9: Stream Improvements =====");
        List<Integer> numbersUntil100 = modernFeatures.getNumbersUntilGreaterThan100();
        System.out.println("Numbers until > 100: " + numbersUntil100);

        List<Integer> numbersAfter50 = modernFeatures.getNumbersAfterReaching50();
        System.out.println("Numbers after >= 50 (limited to 5): " + numbersAfter50);

        System.out.println("\n===== Java 9: Optional Improvements =====");
        try {
            String value = modernFeatures.getDefaultIfEmpty(Optional.empty());
            System.out.println("This won't print due to exception");
        } catch (RuntimeException e) {
            System.out.println("Exception caught: " + e.getMessage());
        }

        System.out.println("Optional as Stream (empty): " + 
                modernFeatures.getOptionalAsStream(Optional.empty()).count());
        System.out.println("Optional as Stream (with value): " + 
                modernFeatures.getOptionalAsStream(Optional.of("Hello")).count());

        System.out.println("\n===== Java 10: Local Variable Type Inference (var) =====");
        List<String> customerEmails = modernFeatures.processWithVar();
        System.out.println("Customer emails (using var): " + customerEmails);

        System.out.println("\n===== Java 11: String Methods =====");
        List<String> processedStrings = modernFeatures.processStrings(
                Arrays.asList("  Hello  ", "", "  World  ", "   ", "Java"));
        System.out.println("Processed strings: " + processedStrings);

        System.out.println("\n===== Java 11: Collection to Array =====");
        String[] array = modernFeatures.convertToArray(List.of("One", "Two", "Three"));
        System.out.println("Array: " + Arrays.toString(array));

        System.out.println("\n===== Java 12: Collectors.teeing =====");
        ModernJavaFunctionalFeatures.SalaryStats stats = modernFeatures.getSalaryStats();
        System.out.println("Salary Stats - Min: $" + stats.min() + 
                ", Max: $" + stats.max() + 
                ", Average: $" + stats.average());

        System.out.println("\n===== Java 14: Switch Expressions =====");
        System.out.println("Grade A: " + modernFeatures.getGradeDescription("A"));
        System.out.println("Grade C: " + modernFeatures.getGradeDescription("C"));
        System.out.println("Grade X: " + modernFeatures.getGradeDescription("X"));

        System.out.println("\n===== Java 15: Text Blocks =====");
        System.out.println(modernFeatures.getEmployeeReport());

        System.out.println("\n===== Java 16: Stream.toList() =====");
        List<String> employeeNames = modernFeatures.getEmployeeNames();
        System.out.println("Employee names: " + employeeNames);

        System.out.println("\n===== Java 16: Pattern Matching for instanceof =====");
        System.out.println(modernFeatures.processObject("Hello"));
        System.out.println(modernFeatures.processObject(42));
        System.out.println(modernFeatures.processObject(List.of(1, 2, 3)));
        System.out.println(modernFeatures.processObject(new Object()));

        System.out.println("\n===== Java 17: Sealed Classes =====");
        System.out.println(modernFeatures.describePerson(new Employee("John", 75000)));
        System.out.println(modernFeatures.describePerson(new Manager("Alice", 120000, 5)));
        System.out.println(modernFeatures.describePerson(new Contractor("Bob", 500)));

        System.out.println("\n===== Java 19-21: Virtual Threads =====");
        List<String> result = modernFeatures.processDataConcurrently(
                List.of("java", "kotlin", "scala")).get();
        System.out.println("Processed concurrently: " + result);

        try {
            System.out.println("\n===== Java 21: SequencedCollection =====");
            System.out.println(modernFeatures.getFirstAndLastCustomer());
        } catch (Exception e) {
            System.out.println("SequencedCollection not available in this Java version: " + e.getMessage());
        }

        System.out.println("\n===== Java 21: Record Patterns =====");
        try {
            System.out.println(modernFeatures.processPoint(new Point(10, 20)));
            System.out.println(modernFeatures.processPoint("Not a point"));
        } catch (Exception e) {
            System.out.println("Record patterns not available in this Java version: " + e.getMessage());
        }
    }
} 