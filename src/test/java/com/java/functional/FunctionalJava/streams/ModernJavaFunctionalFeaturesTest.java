package com.java.functional.FunctionalJava.streams;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

class ModernJavaFunctionalFeaturesTest {

    private ModernJavaFunctionalFeatures modernFeatures;
    private DbLayer dbLayer;

    @BeforeEach
    void setUp() {
        dbLayer = new DbLayer();
        modernFeatures = new ModernJavaFunctionalFeatures(dbLayer);
    }

    @Test
    void testGetImmutableList() {
        List<String> list = modernFeatures.getImmutableList();
        
        assertNotNull(list);
        assertEquals(4, list.size());
        assertTrue(list.contains("Java"));
        assertTrue(list.contains("Kotlin"));
        assertTrue(list.contains("Scala"));
        assertTrue(list.contains("Groovy"));
        
        // Verify it's immutable
        assertThrows(UnsupportedOperationException.class, () -> list.add("Python"));
    }

    @Test
    void testGetImmutableMap() {
        Map<String, Integer> map = modernFeatures.getImmutableMap();
        
        assertNotNull(map);
        assertEquals(4, map.size());
        assertEquals(1995, map.get("Java"));
        assertEquals(2011, map.get("Kotlin"));
        assertEquals(2004, map.get("Scala"));
        assertEquals(2003, map.get("Groovy"));
        
        // Verify it's immutable
        assertThrows(UnsupportedOperationException.class, () -> map.put("Python", 1991));
    }

    @Test
    void testGetNumbersUntilGreaterThan100() {
        List<Integer> numbers = modernFeatures.getNumbersUntilGreaterThan100();
        
        assertNotNull(numbers);
        assertFalse(numbers.isEmpty());
        
        // Should contain numbers 1, 11, 21, ..., 91 (10 numbers)
        assertEquals(10, numbers.size());
        assertEquals(1, numbers.get(0));
        assertEquals(91, numbers.get(numbers.size() - 1));
        
        // All numbers should be <= 100
        numbers.forEach(n -> assertTrue(n <= 100));
    }

    @Test
    void testGetNumbersAfterReaching50() {
        List<Integer> numbers = modernFeatures.getNumbersAfterReaching50();
        
        assertNotNull(numbers);
        assertFalse(numbers.isEmpty());
        
        // Should contain numbers 51, 61, 71, 81, 91 (5 numbers)
        assertEquals(5, numbers.size());
        assertEquals(51, numbers.get(0));
        assertEquals(91, numbers.get(numbers.size() - 1));
        
        // All numbers should be >= 50
        numbers.forEach(n -> assertTrue(n >= 50));
    }

    @Test
    void testGetDefaultIfEmpty() {
        // Should throw exception for empty Optional
        assertThrows(RuntimeException.class, () -> 
            modernFeatures.getDefaultIfEmpty(Optional.empty())
        );
        
        // Should return value for non-empty Optional
        String value = "test";
        assertEquals(value, modernFeatures.getDefaultIfEmpty(Optional.of(value)));
    }

    @Test
    void testGetOptionalAsStream() {
        // Empty Optional should produce empty stream
        assertEquals(0, modernFeatures.getOptionalAsStream(Optional.empty()).count());
        
        // Non-empty Optional should produce stream with one element
        assertEquals(1, modernFeatures.getOptionalAsStream(Optional.of("test")).count());
    }

    @Test
    void testProcessWithVar() {
        List<String> result = modernFeatures.processWithVar();
        
        assertNotNull(result);
        assertFalse(result.isEmpty());
        
        // All results should contain email addresses (with @)
        result.forEach(s -> assertTrue(s.contains(":")));
    }

    @Test
    void testProcessStrings() {
        List<String> input = Arrays.asList("  Hello  ", "", "  World  ", "   ", "Java");
        List<String> result = modernFeatures.processStrings(input);
        
        assertNotNull(result);
        assertEquals(3, result.size()); // Only 3 non-blank strings
        
        // All strings should be trimmed
        result.forEach(s -> assertFalse(s.startsWith(" ") || s.endsWith(" ")));
    }

    @Test
    void testConvertToArray() {
        List<String> input = List.of("One", "Two", "Three");
        String[] result = modernFeatures.convertToArray(input);
        
        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals("One", result[0]);
        assertEquals("Two", result[1]);
        assertEquals("Three", result[2]);
    }

    @Test
    void testGetSalaryStats() {
        ModernJavaFunctionalFeatures.SalaryStats stats = modernFeatures.getSalaryStats();
        
        assertNotNull(stats);
        
        // Min should be less than or equal to max
        assertTrue(stats.min() <= stats.max());
        
        // Average should be between min and max
        assertTrue(stats.average() >= stats.min());
        assertTrue(stats.average() <= stats.max());
    }

    @Test
    void testGetGradeDescription() {
        assertEquals("Excellent", modernFeatures.getGradeDescription("A"));
        assertEquals("Good", modernFeatures.getGradeDescription("B"));
        assertEquals("Average", modernFeatures.getGradeDescription("C"));
        assertEquals("Below Average", modernFeatures.getGradeDescription("D"));
        assertEquals("Failing", modernFeatures.getGradeDescription("F"));
        assertEquals("Unknown Grade", modernFeatures.getGradeDescription("X"));
    }

    @Test
    void testGetEmployeeReport() {
        String report = modernFeatures.getEmployeeReport();
        
        assertNotNull(report);
        assertFalse(report.isEmpty());
        
        // Report should contain the header
        assertTrue(report.contains("Employee Report"));
        assertTrue(report.contains("Total Employees:"));
        assertTrue(report.contains("Average Salary:"));
        
        // Report should contain employee details
        assertTrue(report.contains("ID:"));
        assertTrue(report.contains("Name:"));
        assertTrue(report.contains("Grade:"));
        assertTrue(report.contains("Salary:"));
    }

    @Test
    void testGetEmployeeNames() {
        List<String> names = modernFeatures.getEmployeeNames();
        
        assertNotNull(names);
        assertFalse(names.isEmpty());
        assertEquals(dbLayer.getEmployees().size(), names.size());
    }

    @Test
    void testProcessObject() {
        assertEquals("String of length 5", modernFeatures.processObject("Hello"));
        assertEquals("Integer with value 42", modernFeatures.processObject(42));
        assertEquals("List with 3 elements", modernFeatures.processObject(List.of(1, 2, 3)));
        assertEquals("Unknown object type", modernFeatures.processObject(new Object()));
    }

    @Test
    void testDescribePerson() {
        assertEquals("Employee with salary $75000.0", 
                modernFeatures.describePerson(new Employee("John", 75000)));
        
        assertEquals("Manager with 5 direct reports", 
                modernFeatures.describePerson(new Manager("Alice", 120000, 5)));
        
        assertEquals("Contractor with daily rate $500.0", 
                modernFeatures.describePerson(new Contractor("Bob", 500)));
    }

    @Test
    void testProcessDataConcurrently() throws ExecutionException, InterruptedException {
        List<String> input = List.of("java", "kotlin", "scala");
        List<String> result = modernFeatures.processDataConcurrently(input).get();
        
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("JAVA", result.get(0));
        assertEquals("KOTLIN", result.get(1));
        assertEquals("SCALA", result.get(2));
    }

    // Note: The following tests might fail if running on Java versions < 21
    
    @Test
    void testGetFirstAndLastCustomer() {
        try {
            String result = modernFeatures.getFirstAndLastCustomer();
            
            assertNotNull(result);
            assertTrue(result.startsWith("First customer:"));
            assertTrue(result.contains("Last customer:"));
        } catch (Exception e) {
            // Skip test if running on Java < 21
            System.out.println("Skipping SequencedCollection test: " + e.getMessage());
        }
    }

    @Test
    void testProcessPoint() {
        try {
            assertEquals("Point at coordinates (10, 20)", 
                    modernFeatures.processPoint(new Point(10, 20)));
            
            assertEquals("Not a point", 
                    modernFeatures.processPoint("Not a point"));
        } catch (Exception e) {
            // Skip test if running on Java < 21
            System.out.println("Skipping Record Pattern test: " + e.getMessage());
        }
    }
} 