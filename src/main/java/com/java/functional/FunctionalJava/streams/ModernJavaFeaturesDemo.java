package com.java.functional.FunctionalJava.streams;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * Demonstrates modern Java functional programming features from Java 9 through Java 21.
 * This class serves as a runner for the ModernJavaFunctionalFeatures class.
 * 
 * Note: Some features may not be available depending on your Java version.
 * - Java 9-16 features should work on Java 17+
 * - Java 17-19 features require Java 17+
 * - Java 21 features require Java 21+
 */
public class ModernJavaFeaturesDemo {

    public static void main(String[] args) {
        try {
            runDemo();
        } catch (Exception e) {
            System.err.println("Error running demo: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void runDemo() throws ExecutionException, InterruptedException {
        // Initialize dependencies
        DbLayer dbLayer = new DbLayer();
        ModernJavaFunctionalFeatures modernFeatures = new ModernJavaFunctionalFeatures(dbLayer);

        // Print Java version information
        String javaVersion = System.getProperty("java.version");
        System.out.println("Running with Java version: " + javaVersion);
        System.out.println("Note: Some features may not be available in your Java version.\n");

        demonstrateJava9Features(modernFeatures);
        demonstrateJava10Features(modernFeatures);
        demonstrateJava11Features(modernFeatures);
        demonstrateJava12Features(modernFeatures);
        demonstrateJava14To16Features(modernFeatures);
        demonstrateJava17Features(modernFeatures);
        demonstrateJava19To21Features(modernFeatures);
    }
    
    private static void demonstrateJava9Features(ModernJavaFunctionalFeatures modernFeatures) {
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
    }
    
    private static void demonstrateJava10Features(ModernJavaFunctionalFeatures modernFeatures) {
        System.out.println("\n===== Java 10: Local Variable Type Inference (var) =====");
        List<String> customerEmails = modernFeatures.processWithVar();
        System.out.println("Customer emails (using var): " + customerEmails);
    }
    
    private static void demonstrateJava11Features(ModernJavaFunctionalFeatures modernFeatures) {
        System.out.println("\n===== Java 11: String Methods =====");
        List<String> processedStrings = modernFeatures.processStrings(
                Arrays.asList("  Hello  ", "", "  World  ", "   ", "Java"));
        System.out.println("Processed strings: " + processedStrings);

        System.out.println("\n===== Java 11: Collection to Array =====");
        String[] array = modernFeatures.convertToArray(List.of("One", "Two", "Three"));
        System.out.println("Array: " + Arrays.toString(array));
    }
    
    private static void demonstrateJava12Features(ModernJavaFunctionalFeatures modernFeatures) {
        System.out.println("\n===== Java 12: Collectors.teeing =====");
        ModernJavaFunctionalFeatures.SalaryStats stats = modernFeatures.getSalaryStats();
        System.out.println("Salary Stats - Min: $" + stats.min() + 
                ", Max: $" + stats.max() + 
                ", Average: $" + stats.average());
    }
    
    private static void demonstrateJava14To16Features(ModernJavaFunctionalFeatures modernFeatures) {
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
    }
    
    private static void demonstrateJava17Features(ModernJavaFunctionalFeatures modernFeatures) {
        try {
            System.out.println("\n===== Java 17: Sealed Classes =====");
            System.out.println(modernFeatures.describePerson(new ModernJavaFunctionalFeatures.Employee("John", 75000)));
            System.out.println(modernFeatures.describePerson(new ModernJavaFunctionalFeatures.Manager("Alice", 120000, 5)));
            System.out.println(modernFeatures.describePerson(new ModernJavaFunctionalFeatures.Contractor("Bob", 500)));
        } catch (Exception e) {
            System.out.println("Sealed classes not available or error occurred: " + e.getMessage());
        }
    }
    
    private static void demonstrateJava19To21Features(ModernJavaFunctionalFeatures modernFeatures) {
        try {
            System.out.println("\n===== Java 19-21: Virtual Threads =====");
            List<String> result = modernFeatures.processDataConcurrently(
                    List.of("java", "kotlin", "scala")).get();
            System.out.println("Processed concurrently: " + result);
        } catch (Exception e) {
            System.out.println("Error with concurrent processing: " + e.getMessage());
        }

        try {
            System.out.println("\n===== Java 21: SequencedCollection =====");
            System.out.println(modernFeatures.getFirstAndLastCustomer());
        } catch (Exception e) {
            System.out.println("SequencedCollection not available in this Java version: " + e.getMessage());
        }

        try {
            System.out.println("\n===== Java 21: Record Patterns =====");
            System.out.println(modernFeatures.processPoint(new ModernJavaFunctionalFeatures.Point(10, 20)));
            System.out.println(modernFeatures.processPoint("Not a point"));
        } catch (Exception e) {
            System.out.println("Record patterns not available in this Java version: " + e.getMessage());
        }
    }
} 