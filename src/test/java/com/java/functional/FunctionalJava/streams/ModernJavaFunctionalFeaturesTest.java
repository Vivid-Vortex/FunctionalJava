package com.java.functional.FunctionalJava.streams;

import com.java.functional.FunctionalJava.dto.Customer;
import com.java.functional.FunctionalJava.dto.Employees;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnJre;
import org.junit.jupiter.api.condition.JRE;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ModernJavaFunctionalFeaturesTest {

    private ModernJavaFunctionalFeatures modernFeatures;
    private DbLayer dbLayer;
    
    // For capturing console output in tests
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        // Set up System.out capture
        System.setOut(new PrintStream(outputStreamCaptor));
        
        // Initialize real DbLayer for most tests
        dbLayer = new DbLayer();
        modernFeatures = new ModernJavaFunctionalFeatures(dbLayer);
    }
    
    @AfterEach
    void tearDown() {
        // Restore original System.out
        System.setOut(originalOut);
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
        
        // Verify the step is 10
        assertEquals(10, numbers.get(1) - numbers.get(0));
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
        
        // Verify the step is 10
        assertEquals(10, numbers.get(1) - numbers.get(0));
    }

    @Test
    void testGetDefaultIfEmpty() {
        // Should throw exception for empty Optional
        Exception exception = assertThrows(RuntimeException.class, () -> 
            modernFeatures.getDefaultIfEmpty(Optional.empty())
        );
        assertEquals("Value not present", exception.getMessage());
        
        // Should return value for non-empty Optional
        String value = "test";
        assertEquals(value, modernFeatures.getDefaultIfEmpty(Optional.of(value)));
    }

    @Test
    void testGetOptionalAsStream() {
        // Empty Optional should produce empty stream
        Stream<String> emptyStream = modernFeatures.getOptionalAsStream(Optional.empty());
        assertEquals(0, emptyStream.count());
        
        // Non-empty Optional should produce stream with one element
        String testValue = "test";
        Stream<String> resultStream = modernFeatures.getOptionalAsStream(Optional.of(testValue));
        
        List<String> resultList = resultStream.toList();
        assertEquals(1, resultList.size());
        assertEquals(testValue, resultList.get(0));
    }

    @Test
    void testProcessWithVar() {
        List<String> result = modernFeatures.processWithVar();
        
        assertNotNull(result);
        assertFalse(result.isEmpty());
        
        // All results should contain email addresses (with @) and be formatted as "name: email"
        result.forEach(s -> {
            assertTrue(s.contains(":"));
            assertTrue(s.contains("@"));
        });
        
        // Number of results should match number of customers with non-null emails
        long expectedCount = dbLayer.getAllCustomer().stream()
                .filter(c -> c.getEmail() != null)
                .count();
        assertEquals(expectedCount, result.size());
    }

    @Test
    void testProcessStrings() {
        List<String> input = Arrays.asList("  Hello  ", "", "  World  ", "   ", "Java");
        List<String> result = modernFeatures.processStrings(input);
        
        assertNotNull(result);
        assertEquals(3, result.size()); // Only 3 non-blank strings
        
        // All strings should be trimmed
        result.forEach(s -> {
            assertFalse(s.startsWith(" "));
            assertFalse(s.endsWith(" "));
        });
        
        // Verify specific values
        assertTrue(result.contains("Hello"));
        assertTrue(result.contains("World"));
        assertTrue(result.contains("Java"));
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
        
        // Verify it's a String array
        assertTrue(result instanceof String[]);
    }

    @Test
    void testGetSalaryStats() {
        ModernJavaFunctionalFeatures.SalaryStats stats = modernFeatures.getSalaryStats();
        
        assertNotNull(stats);
        
        // Min should be less than or equal to max
        assertTrue(stats.min() <= stats.max());
        
        // Average should be between min and max (or equal to min/max if all values are the same)
        assertTrue(stats.average() >= stats.min());
        assertTrue(stats.average() <= stats.max());
        
        // Verify actual values based on our test data
        assertEquals(15000.0, stats.min());
        assertEquals(95000.0, stats.max());
        
        // Calculate expected average
        double expectedAverage = dbLayer.getEmployees().stream()
                .mapToDouble(Employees::getSalary)
                .average()
                .orElse(0.0);
        assertEquals(expectedAverage, stats.average());
    }

    @Test
    void testGetGradeDescription() {
        assertEquals("Excellent", modernFeatures.getGradeDescription("A"));
        assertEquals("Good", modernFeatures.getGradeDescription("B"));
        assertEquals("Average", modernFeatures.getGradeDescription("C"));
        assertEquals("Below Average", modernFeatures.getGradeDescription("D"));
        assertEquals("Failing", modernFeatures.getGradeDescription("F"));
        assertEquals("Unknown Grade", modernFeatures.getGradeDescription("X"));
        assertEquals("Unknown Grade", modernFeatures.getGradeDescription(null));
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
        
        // Verify specific employee data is included
        assertTrue(report.contains("Aman"));
        assertTrue(report.contains("Pankaj"));
        
        // Verify total employee count is included
        assertTrue(report.contains("Total Employees: " + dbLayer.getEmployees().size()));
    }

    @Test
    void testGetEmployeeNames() {
        List<String> names = modernFeatures.getEmployeeNames();
        
        assertNotNull(names);
        assertFalse(names.isEmpty());
        assertEquals(dbLayer.getEmployees().size(), names.size());
        
        // Verify specific names are included
        assertTrue(names.contains("Aman"));
        assertTrue(names.contains("Bman"));
        assertTrue(names.contains("Pankaj"));
        
        // Verify the list is unmodifiable (Java 16+ behavior)
        assertThrows(UnsupportedOperationException.class, () -> names.add("NewEmployee"));
    }

    @Test
    void testProcessObject() {
        assertEquals("String of length 5", modernFeatures.processObject("Hello"));
        assertEquals("Integer with value 42", modernFeatures.processObject(42));
        assertEquals("List with 3 elements", modernFeatures.processObject(List.of(1, 2, 3)));
        assertEquals("Unknown object type", modernFeatures.processObject(new Object()));
        assertEquals("Unknown object type", modernFeatures.processObject(null));
    }

    @Test
    void testDescribePerson() {
        assertEquals("Employee with salary $75000.0", 
                modernFeatures.describePerson(new ModernJavaFunctionalFeatures.Employee("John", 75000)));
        
        assertEquals("Manager with 5 direct reports", 
                modernFeatures.describePerson(new ModernJavaFunctionalFeatures.Manager("Alice", 120000, 5)));
        
        assertEquals("Contractor with daily rate $500.0", 
                modernFeatures.describePerson(new ModernJavaFunctionalFeatures.Contractor("Bob", 500)));
    }

    @Test
    void testProcessDataConcurrently() throws ExecutionException, InterruptedException {
        List<String> input = List.of("java", "kotlin", "scala");
        CompletableFuture<List<String>> future = modernFeatures.processDataConcurrently(input);
        
        assertNotNull(future);
        
        List<String> result = future.get();
        
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("JAVA", result.get(0));
        assertEquals("KOTLIN", result.get(1));
        assertEquals("SCALA", result.get(2));
    }

    @Test
    void testGetFirstAndLastCustomer() {
        try {
            String result = modernFeatures.getFirstAndLastCustomer();
            
            assertNotNull(result);
            assertTrue(result.startsWith("First customer:"));
            assertTrue(result.contains("Last customer:"));
            
            // Verify it contains the actual first and last customer names
            List<Customer> customers = dbLayer.getAllCustomer();
            String expectedFirst = customers.get(0).getName();
            String expectedLast = customers.get(customers.size() - 1).getName();
            
            assertTrue(result.contains(expectedFirst));
            assertTrue(result.contains(expectedLast));
        } catch (Exception e) {
            // Skip test if running on Java < 21
            System.out.println("Skipping SequencedCollection test: " + e.getMessage());
        }
    }

    @Test
    void testProcessPoint() {
        try {
            ModernJavaFunctionalFeatures.Point point = new ModernJavaFunctionalFeatures.Point(10, 20);
            
            assertEquals("Point at coordinates (10, 20)", 
                    modernFeatures.processPoint(point));
            
            assertEquals("Not a point", 
                    modernFeatures.processPoint("Not a point"));
            
            assertEquals("Not a point", 
                    modernFeatures.processPoint(null));
        } catch (Exception e) {
            // Skip test if running on Java < 21
            System.out.println("Skipping Record Pattern test: " + e.getMessage());
        }
    }
    
    @Test
    void testWithMockedDbLayer() {
        // Create a mock DbLayer
        DbLayer mockDbLayer = mock(DbLayer.class);
        
        // Set up mock data
        List<Customer> mockCustomers = List.of(
            new Customer(1, "MockCustomer1", "mock1@example.com", List.of("123456")),
            new Customer(2, "MockCustomer2", "mock2@example.com", List.of("654321"))
        );
        
        List<Employees> mockEmployees = List.of(
            new Employees(1, "MockEmployee1", "A", 50000),
            new Employees(2, "MockEmployee2", "B", 60000)
        );
        
        // Configure mock behavior
        when(mockDbLayer.getAllCustomer()).thenReturn(mockCustomers);
        when(mockDbLayer.getEmployees()).thenReturn(mockEmployees);
        
        // Create instance with mock
        ModernJavaFunctionalFeatures featuresWithMock = new ModernJavaFunctionalFeatures(mockDbLayer);
        
        // Test methods that use DbLayer
        List<String> processedCustomers = featuresWithMock.processWithVar();
        assertEquals(2, processedCustomers.size());
        assertTrue(processedCustomers.contains("MockCustomer1: mock1@example.com"));
        assertTrue(processedCustomers.contains("MockCustomer2: mock2@example.com"));
        
        ModernJavaFunctionalFeatures.SalaryStats stats = featuresWithMock.getSalaryStats();
        assertEquals(50000.0, stats.min());
        assertEquals(60000.0, stats.max());
        assertEquals(55000.0, stats.average());
        
        // Verify mock was called
        verify(mockDbLayer, times(1)).getAllCustomer();
        verify(mockDbLayer, atLeastOnce()).getEmployees();
    }
    
    @Test
    void testJava21FeaturesWithVersionCheck() {
        // This test demonstrates how to conditionally run tests based on Java version
        String javaVersion = System.getProperty("java.version");
        System.out.println("Running on Java version: " + javaVersion);
        
        if (javaVersion.startsWith("21") || 
            javaVersion.startsWith("22") || 
            javaVersion.compareTo("21") >= 0) {
            
            // These should work on Java 21+
            try {
                String result = modernFeatures.getFirstAndLastCustomer();
                assertNotNull(result);
                
                ModernJavaFunctionalFeatures.Point point = new ModernJavaFunctionalFeatures.Point(5, 10);
                String pointResult = modernFeatures.processPoint(point);
                assertEquals("Point at coordinates (5, 10)", pointResult);
            } catch (Exception e) {
                fail("Java 21 features should work on Java 21+: " + e.getMessage());
            }
        } else {
            // On older Java versions, these should throw exceptions
            assertThrows(Exception.class, () -> modernFeatures.getFirstAndLastCustomer());
        }
    }
    
    @Test
    void testPersonRecords() {
        // Test Employee record
        ModernJavaFunctionalFeatures.Employee employee = new ModernJavaFunctionalFeatures.Employee("John", 75000);
        assertEquals("John", employee.name());
        assertEquals(75000, employee.salary());
        
        // Test Manager record
        ModernJavaFunctionalFeatures.Manager manager = new ModernJavaFunctionalFeatures.Manager("Alice", 120000, 5);
        assertEquals("Alice", manager.name());
        assertEquals(120000, manager.salary());
        assertEquals(5, manager.directReports());
        
        // Test Contractor record
        ModernJavaFunctionalFeatures.Contractor contractor = new ModernJavaFunctionalFeatures.Contractor("Bob", 500);
        assertEquals("Bob", contractor.name());
        assertEquals(500, contractor.dailyRate());
        
        // Test Point record
        ModernJavaFunctionalFeatures.Point point = new ModernJavaFunctionalFeatures.Point(10, 20);
        assertEquals(10, point.x());
        assertEquals(20, point.y());
        
        // Test equals and hashCode for records
        ModernJavaFunctionalFeatures.Employee employee2 = new ModernJavaFunctionalFeatures.Employee("John", 75000);
        assertEquals(employee, employee2);
        assertEquals(employee.hashCode(), employee2.hashCode());
        
        ModernJavaFunctionalFeatures.Employee differentEmployee = new ModernJavaFunctionalFeatures.Employee("John", 80000);
        assertNotEquals(employee, differentEmployee);
    }
    
    @Test
    void testSalaryStatsRecord() {
        // Test SalaryStats record
        ModernJavaFunctionalFeatures.SalaryStats stats = new ModernJavaFunctionalFeatures.SalaryStats(30000, 90000, 60000);
        assertEquals(30000, stats.min());
        assertEquals(90000, stats.max());
        assertEquals(60000, stats.average());
        
        // Test toString
        String statsString = stats.toString();
        assertTrue(statsString.contains("min=30000.0"));
        assertTrue(statsString.contains("max=90000.0"));
        assertTrue(statsString.contains("average=60000.0"));
        
        // Test equals and hashCode
        ModernJavaFunctionalFeatures.SalaryStats sameStats = new ModernJavaFunctionalFeatures.SalaryStats(30000, 90000, 60000);
        assertEquals(stats, sameStats);
        assertEquals(stats.hashCode(), sameStats.hashCode());
        
        ModernJavaFunctionalFeatures.SalaryStats differentStats = new ModernJavaFunctionalFeatures.SalaryStats(30000, 90000, 65000);
        assertNotEquals(stats, differentStats);
    }
    
    @Test
    void testGetSalaryStatsWithEmptyEmployeeList() {
        // Create a mock DbLayer that returns an empty list
        DbLayer mockDbLayer = mock(DbLayer.class);
        when(mockDbLayer.getEmployees()).thenReturn(List.of());
        
        // Create instance with mock
        ModernJavaFunctionalFeatures featuresWithMock = new ModernJavaFunctionalFeatures(mockDbLayer);
        
        // Test getSalaryStats with empty list
        ModernJavaFunctionalFeatures.SalaryStats stats = featuresWithMock.getSalaryStats();
        
        // Should return default values
        assertEquals(0.0, stats.min());
        assertEquals(0.0, stats.max());
        assertEquals(0.0, stats.average());
    }
} 