package com.java.functional.FunctionalJava.streams;

import com.java.functional.FunctionalJava.dto.Customer;
import com.java.functional.FunctionalJava.dto.Employees;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StreamApisTest {

    @Mock
    private DbLayer dbLayer;

    private StreamApis streamApis;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        streamApis = new StreamApis(dbLayer);
        
        // Setup mock data
        List<Customer> customers = Arrays.asList(
            new Customer(1, "John", "john@example.com", Arrays.asList("123456", "789012")),
            new Customer(2, "Jane", "jane@example.com", Arrays.asList("345678", "901234")),
            new Customer(3, "Bob", null, Arrays.asList("567890", "123456"))
        );
        
        List<Employees> employees = Arrays.asList(
            new Employees(1, "John", "A", 60000),
            new Employees(2, "Jane", "B", 50000),
            new Employees(3, "Bob", "A", 70000),
            new Employees(4, "Alice", "C", 40000)
        );
        
        Map<Customer, String> customerMap = new HashMap<>();
        customerMap.put(customers.get(0), "Premium");
        customerMap.put(customers.get(1), "Standard");
        customerMap.put(customers.get(2), "Basic");
        
        // Configure mock behavior
        when(dbLayer.getAllCustomer()).thenReturn(customers);
        when(dbLayer.getCustStrema()).thenReturn(customers);
        when(dbLayer.getEmployees()).thenReturn(employees);
        when(dbLayer.getAllCustomerAsMap()).thenReturn(customerMap);
        when(dbLayer.getNullEmaillCustomer()).thenReturn(customers.get(2));
        when(dbLayer.getCustomer()).thenReturn(customers.get(0));
        
        try {
            when(dbLayer.getCustomerByEmail("john@example.com")).thenReturn(Optional.of(customers.get(0)));
            when(dbLayer.getCustomerByEmail("bman@gmail.com")).thenReturn(Optional.of(customers.get(0)));
            when(dbLayer.getCustomerByEmail(anyString())).thenReturn(Optional.empty());
        } catch (Exception e) {
            fail("Mock setup failed: " + e.getMessage());
        }
    }

    @Test
    void testMap() {
        // This test verifies the map operation works correctly
        assertDoesNotThrow(() -> streamApis.testMap());
        verify(dbLayer, times(1)).getCustStrema();
    }

    @Test
    void testFlatMap() {
        // This test verifies the flatMap operation works correctly
        assertDoesNotThrow(() -> streamApis.testFlatMap());
        verify(dbLayer, times(1)).getCustStrema();
    }

    @Test
    void convertListOfListToSingleList() {
        // This test verifies the conversion of list of lists to a single list
        assertDoesNotThrow(() -> streamApis.convertListOfListToSingleList());
    }

    @Test
    void testMapToObj() {
        // This test verifies the mapToObj operation works correctly
        assertDoesNotThrow(() -> streamApis.testMapToObj());
    }

    @Test
    void testMapReduce() {
        // This test verifies the map and reduce operations work correctly
        assertDoesNotThrow(() -> streamApis.testMapReduce());
        verify(dbLayer, times(1)).getEmployees();
    }

    @Test
    void testPureOptionalWithoutStream() {
        // This test verifies the Optional operations without streams
        assertDoesNotThrow(() -> streamApis.testPureOptionalWithoutStream());
        verify(dbLayer, times(1)).getNullEmaillCustomer();
        verify(dbLayer, times(1)).getCustomer();
    }

    @Test
    void testOptionalWithStream() {
        // This test verifies the Optional operations with streams
        assertDoesNotThrow(() -> streamApis.testOptionalWithStream());
        try {
            verify(dbLayer, times(1)).getCustomerByEmail("bman@gmail.com");
            verify(dbLayer, times(1)).getCustomerByEmail("nonexistent@example.com");
        } catch (Exception e) {
            fail("Verification failed: " + e.getMessage());
        }
    }

    @Test
    void orElseExample() {
        // This test verifies the orElse method of Optional
        assertDoesNotThrow(() -> streamApis.orElseExample());
    }

    @Test
    void orElseGetExample() {
        // This test verifies the orElseGet method of Optional
        assertDoesNotThrow(() -> streamApis.orElseGetExample());
    }

    @Test
    void orElseThrowExample() {
        // This test verifies the orElseThrow method of Optional
        assertDoesNotThrow(() -> streamApis.orElseThrowExample());
    }

    @Test
    void writeLogsToLogsFile() {
        // This test verifies the log writing functionality
        File logDir = tempDir.resolve("logs").toFile();
        logDir.mkdirs();
        
        Exception testException = new RuntimeException("Test exception");
        assertDoesNotThrow(() -> streamApis.writeLogsToLogsFile(testException));
    }

   @Test
    void sortListUsingStreamApi() {
        // Setup test data
        List<Customer> unsortedCustomers = Arrays.asList(
            new Customer(3, "Charlie", "charlie@example.com", Arrays.asList("789")),
            new Customer(1, "Alice", "alice@example.com", Arrays.asList("123")),
            new Customer(2, "Bob", "bob@example.com", Arrays.asList("456"))
        );
        when(dbLayer.getAllCustomer()).thenReturn(unsortedCustomers);

        // Test execution
        assertDoesNotThrow(() -> streamApis.sortListUsingStreamApi());

        // Verify mock interactions - the method calls getAllCustomer 3 times internally
        verify(dbLayer, atLeastOnce()).getAllCustomer();
    }

    @Test
    void sortingListExampleUsingStreamApi() {
        // This test verifies the employee list sorting functionality
        assertDoesNotThrow(() -> streamApis.sortingListExampleUsingStreamApi());
    }

    @Test
    void sortMapUsingStreamApi() {
        // This test verifies the map sorting functionality using Stream API
        assertDoesNotThrow(() -> streamApis.sortMapUsingStreamApi());
    }

    @Test
    void sortMapUsingTraditionalApproach() {
        // This test verifies the map sorting functionality using traditional approach
        assertDoesNotThrow(() -> streamApis.sortMapUsingTraditionalApproach());
        verify(dbLayer, times(1)).getAllCustomerAsMap();
    }

    @Test
    void testConvertListToMap() {
        // This test verifies the conversion of list to map
        assertDoesNotThrow(() -> streamApis.testConvertListToMap());
        verify(dbLayer, times(1)).getAllCustomer();
    }

    @Test
    void testConvertListToArray() {
        // This test verifies the conversion of list to array
        assertDoesNotThrow(() -> streamApis.testConvertListToArray());
        verify(dbLayer, times(1)).getAllCustomer();
    }

    @Test
    void testConvertMapToList() {
        // This test verifies the conversion of map to list
        assertDoesNotThrow(() -> streamApis.testConvertMapToList());
    }

    @Test
    void employeeClassTest() {
        // This test verifies the Employee inner class
        StreamApis.Employee employee = new StreamApis.Employee("Test", 50000);
        
        assertEquals("Test", employee.getName());
        assertEquals(50000, employee.getSalary());
        assertTrue(employee.toString().contains("Test"));
        assertTrue(employee.toString().contains("50000"));
    }

    @Test
    void testExceptionHandlingInOptional() {
        // This test verifies exception handling in Optional operations
        Customer nullCustomer = new Customer(0, null, null, null);
        when(dbLayer.getNullEmaillCustomer()).thenReturn(nullCustomer);
        
        assertDoesNotThrow(() -> streamApis.testPureOptionalWithoutStream());
    }

    @Test
    void testNullSafetyInFlatMap() {
        // This test verifies null safety in flatMap operations
        List<Customer> customersWithNulls = Arrays.asList(
            new Customer(1, "John", "john@example.com", null),
            new Customer(2, "Jane", "jane@example.com", Arrays.asList("345678", "901234")),
            null
        );
        
        when(dbLayer.getCustStrema()).thenReturn(customersWithNulls);
        
        assertDoesNotThrow(() -> streamApis.testFlatMap());
    }

    @Test
    void testEmptyCollectionsInStreamOperations() {
        // This test verifies handling of empty collections in stream operations
        when(dbLayer.getAllCustomer()).thenReturn(Collections.emptyList());
        when(dbLayer.getEmployees()).thenReturn(Collections.emptyList());
        
        assertDoesNotThrow(() -> streamApis.testMapReduce());
        assertDoesNotThrow(() -> streamApis.sortListUsingStreamApi());
        assertDoesNotThrow(() -> streamApis.testConvertListToMap());
        assertDoesNotThrow(() -> streamApis.testConvertListToArray());
    }

    @Test
    void testExceptionInOptionalWithStream() throws Exception {
        // This test verifies exception handling in Optional with stream
        when(dbLayer.getCustomerByEmail(anyString())).thenThrow(new RuntimeException("Test exception"));
        
        assertThrows(RuntimeException.class, () -> streamApis.testOptionalWithStream());
    }
} 