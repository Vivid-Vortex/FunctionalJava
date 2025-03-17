package com.java.functional.FunctionalJava.streams;

import com.java.functional.FunctionalJava.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

public class Java8FunctionalServiceTest {
    
    private Java8FunctionalService service;
    private DbLayer dbLayer;

    @BeforeEach
    void setUp() {
        dbLayer = new DbLayer();
        service = new Java8FunctionalService(dbLayer);
    }

    @Test
    void testGetCustomerNames() {
        List<String> names = service.getCustomerNames();
        assertNotNull(names);
        assertFalse(names.isEmpty());
        assertEquals(10, names.size());
        assertTrue(names.contains("Aman"));
        assertTrue(names.contains("Bman"));
    }

    @Test
    void testMapData() {
        List<Integer> ids = service.mapData(dbLayer.getAllCustomer(), Customer::getId);
        assertNotNull(ids);
        assertEquals(10, ids.size());
        assertTrue(ids.contains(1));
        assertTrue(ids.contains(2));
    }

    @Test
    void testGetPremiumCustomers() {
        List<Customer> premiumCustomers = service.getPremiumCustomers();
        assertNotNull(premiumCustomers);
        assertFalse(premiumCustomers.isEmpty());
        
        // All premium customers should have more than 1 phone number
        premiumCustomers.forEach(customer -> 
            assertTrue(customer.getPhoneNumbers().size() > 1)
        );
    }

    @Test
    void testFilterData() {
        Predicate<Customer> hasEmail = customer -> customer.getEmail() != null;
        List<Customer> customersWithEmail = service.filterData(dbLayer.getAllCustomer(), hasEmail);
        
        assertNotNull(customersWithEmail);
        assertTrue(customersWithEmail.size() < dbLayer.getAllCustomer().size());
        
        // All filtered customers should have email
        customersWithEmail.forEach(customer -> 
            assertNotNull(customer.getEmail())
        );
    }

    @Test
    void testGetHighSalariedGradeAEmployees() {
        List<Employees> highSalariedGradeA = service.getHighSalariedGradeAEmployees();
        
        assertNotNull(highSalariedGradeA);
        
        // All should be grade A and have salary > 70000
        highSalariedGradeA.forEach(emp -> {
            assertEquals("A", emp.getGrade());
            assertTrue(emp.getSalary() > 70000);
        });
    }

    @Test
    void testGetEmployeesByGrade() {
        Map<String, List<Employees>> employeesByGrade = service.getEmployeesByGrade();
        
        assertNotNull(employeesByGrade);
        assertTrue(employeesByGrade.containsKey("A"));
        assertTrue(employeesByGrade.containsKey("B"));
        assertTrue(employeesByGrade.containsKey("C"));
        
        // Check that employees are correctly grouped
        employeesByGrade.forEach((grade, employees) -> 
            employees.forEach(emp -> assertEquals(grade, emp.getGrade()))
        );
    }

    @Test
    void testGetAverageSalaryByGrade() {
        Map<String, Double> avgSalaryByGrade = service.getAverageSalaryByGrade();
        
        assertNotNull(avgSalaryByGrade);
        assertTrue(avgSalaryByGrade.containsKey("A"));
        assertTrue(avgSalaryByGrade.containsKey("B"));
        assertTrue(avgSalaryByGrade.containsKey("C"));
        
        // Grade A should have higher average salary than Grade B and C
        assertTrue(avgSalaryByGrade.get("A") > avgSalaryByGrade.get("B"));
        assertTrue(avgSalaryByGrade.get("B") > avgSalaryByGrade.get("C"));
    }

    @Test
    void testGetEmployeesSummaryByGrade() {
        List<EmployeeSummary> summaries = service.getEmployeesSummaryByGrade();
        
        assertNotNull(summaries);
        assertFalse(summaries.isEmpty());
        
        // Check that each summary has the correct data
        summaries.forEach(summary -> {
            assertNotNull(summary.getGrade());
            assertTrue(summary.getCount() > 0);
            assertTrue(summary.getMaxSalary() >= summary.getMinSalary());
            assertTrue(summary.getAverageSalary() >= summary.getMinSalary());
            assertTrue(summary.getAverageSalary() <= summary.getMaxSalary());
        });
    }

    @Test
    void testPartitionEmployeesBySalary() {
        double threshold = 50000;
        Map<Boolean, List<Employees>> partitioned = service.partitionEmployeesBySalary(threshold);
        
        assertNotNull(partitioned);
        assertTrue(partitioned.containsKey(true));
        assertTrue(partitioned.containsKey(false));
        
        // Check that employees are correctly partitioned
        partitioned.get(true).forEach(emp -> 
            assertTrue(emp.getSalary() > threshold)
        );
        
        partitioned.get(false).forEach(emp -> 
            assertTrue(emp.getSalary() <= threshold)
        );
    }

    @Test
    void testGetAllPhoneNumbers() {
        List<String> allPhoneNumbers = service.getAllPhoneNumbers();
        
        assertNotNull(allPhoneNumbers);
        assertFalse(allPhoneNumbers.isEmpty());
        
        // Size should be the sum of all customer phone numbers
        int expectedSize = dbLayer.getAllCustomer().stream()
                .mapToInt(c -> c.getPhoneNumbers().size())
                .sum();
        
        assertEquals(expectedSize, allPhoneNumbers.size());
    }

    @Test
    void testGetUniquePhoneNumbers() {
        List<String> uniquePhoneNumbers = service.getUniquePhoneNumbers();
        
        assertNotNull(uniquePhoneNumbers);
        assertFalse(uniquePhoneNumbers.isEmpty());
        
        // Check that all phone numbers are unique
        Set<String> phoneNumberSet = new HashSet<>(uniquePhoneNumbers);
        assertEquals(phoneNumberSet.size(), uniquePhoneNumbers.size());
    }

    @Test
    void testGetTotalSalary() {
        double totalSalary = service.getTotalSalary();
        
        // Total should be the sum of all employee salaries
        double expectedTotal = dbLayer.getEmployees().stream()
                .mapToDouble(Employees::getSalary)
                .sum();
        
        assertEquals(expectedTotal, totalSalary);
    }

    @Test
    void testGetTotalSalaryWithReduce() {
        double totalSalary = service.getTotalSalaryWithReduce();
        
        // Total should be the sum of all employee salaries
        double expectedTotal = dbLayer.getEmployees().stream()
                .mapToDouble(Employees::getSalary)
                .sum();
        
        assertEquals(expectedTotal, totalSalary);
    }

    @Test
    void testGetEmailOrDefault() {
        Customer customerWithEmail = dbLayer.getCustomer();
        Customer customerWithoutEmail = dbLayer.getNullEmaillCustomer();
        String defaultEmail = "default@example.com";
        
        String email1 = service.getEmailOrDefault(customerWithEmail, defaultEmail);
        String email2 = service.getEmailOrDefault(customerWithoutEmail, defaultEmail);
        
        assertEquals(customerWithEmail.getEmail(), email1);
        assertEquals(defaultEmail, email2);
    }

    @Test
    void testGetEmailDomain() {
        Customer customerWithEmail = dbLayer.getCustomer();
        Customer customerWithoutEmail = dbLayer.getNullEmaillCustomer();
        
        String domain1 = service.getEmailDomain(customerWithEmail);
        String domain2 = service.getEmailDomain(customerWithoutEmail);
        
        assertEquals("gmail.com", domain1);
        assertEquals("No valid email", domain2);
    }

    @Test
    void testGetSortedEmployees() {
        List<Employees> sortedBySalaryDesc = service.getSortedEmployees(
                Comparator.comparing(Employees::getSalary).reversed());
        
        assertNotNull(sortedBySalaryDesc);
        assertEquals(dbLayer.getEmployees().size(), sortedBySalaryDesc.size());
        
        // Check that employees are sorted by salary in descending order
        for (int i = 0; i < sortedBySalaryDesc.size() - 1; i++) {
            assertTrue(sortedBySalaryDesc.get(i).getSalary() >= sortedBySalaryDesc.get(i + 1).getSalary());
        }
    }

    @Test
    void testGetEmployeesSortedByGradeAndSalary() {
        List<Employees> sorted = service.getEmployeesSortedByGradeAndSalary();
        
        assertNotNull(sorted);
        assertEquals(dbLayer.getEmployees().size(), sorted.size());
        
        // Check that employees are sorted by grade and then by salary in descending order
        for (int i = 0; i < sorted.size() - 1; i++) {
            if (sorted.get(i).getGrade().equals(sorted.get(i + 1).getGrade())) {
                assertTrue(sorted.get(i).getSalary() >= sorted.get(i + 1).getSalary());
            } else {
                assertTrue(sorted.get(i).getGrade().compareTo(sorted.get(i + 1).getGrade()) <= 0);
            }
        }
    }

    @Test
    void testGetCustomerPage() {
        int page = 0;
        int size = 3;
        Pagination<Customer> pagination = service.getCustomerPage(page, size);
        
        assertNotNull(pagination);
        assertEquals(page, pagination.getPage());
        assertEquals(size, pagination.getSize());
        assertEquals(dbLayer.getAllCustomer().size(), pagination.getTotalElements());
        assertEquals(Math.ceil((double) dbLayer.getAllCustomer().size() / size), pagination.getTotalPages());
        assertEquals(size, pagination.getContent().size());
    }

    @Test
    void testGetEmployeeCountByGrade() {
        Map<String, Long> countByGrade = service.getEmployeeCountByGrade();
        
        assertNotNull(countByGrade);
        assertTrue(countByGrade.containsKey("A"));
        assertTrue(countByGrade.containsKey("B"));
        assertTrue(countByGrade.containsKey("C"));
        
        // Check that counts are correct
        Map<String, List<Employees>> employeesByGrade = service.getEmployeesByGrade();
        employeesByGrade.forEach((grade, employees) -> 
            assertEquals((long) employees.size(), countByGrade.get(grade))
        );
    }

    @Test
    void testGetCustomerNamesAsString() {
        String namesString = service.getCustomerNamesAsString();
        
        assertNotNull(namesString);
        assertTrue(namesString.startsWith("Customers: "));
        assertTrue(namesString.endsWith("."));
        
        // Check that all customer names are in the string
        dbLayer.getAllCustomer().forEach(customer -> 
            assertTrue(namesString.contains(customer.getName()))
        );
    }

    @Test
    void testFindHighestPaidEmployee() {
        Optional<Employees> highestPaid = service.findHighestPaidEmployee();
        
        assertTrue(highestPaid.isPresent());
        
        // Check that no employee has higher salary
        double maxSalary = highestPaid.get().getSalary();
        dbLayer.getEmployees().forEach(emp -> 
            assertTrue(emp.getSalary() <= maxSalary)
        );
    }

    @Test
    void testAreAllEmployeesGradeA() {
        boolean allGradeA = service.areAllEmployeesGradeA();
        
        // Should be false since we have employees with grades B and C
        assertFalse(allGradeA);
    }

    @Test
    void testIsAnyEmployeeHighSalaried() {
        boolean anyHighSalaried = service.isAnyEmployeeHighSalaried(90000);
        
        // Should be true since we have at least one employee with salary > 90000
        assertTrue(anyHighSalaried);
        
        boolean anyVeryHighSalaried = service.isAnyEmployeeHighSalaried(100000);
        
        // Should be false since no employee has salary > 100000
        assertFalse(anyVeryHighSalaried);
    }

    @Test
    void testGetEmployeesWithSalaryLog() {
        List<Employees> loggedEmployees = service.getEmployeesWithSalaryLog();
        
        assertNotNull(loggedEmployees);
        assertFalse(loggedEmployees.isEmpty());
        
        // All employees should have salary > 50000
        loggedEmployees.forEach(emp -> 
            assertTrue(emp.getSalary() > 50000)
        );
    }

    @Test
    void testGetCustomersAsync() throws ExecutionException, InterruptedException {
        CompletableFuture<List<Customer>> customersFuture = service.getCustomersAsync();
        
        assertNotNull(customersFuture);
        
        List<Customer> customers = customersFuture.get();
        assertNotNull(customers);
        assertEquals(dbLayer.getAllCustomer().size(), customers.size());
    }

    @Test
    void testGetCustomerEmailsAsync() throws ExecutionException, InterruptedException {
        CompletableFuture<List<String>> emailsFuture = service.getCustomerEmailsAsync();
        
        assertNotNull(emailsFuture);
        
        List<String> emails = emailsFuture.get();
        assertNotNull(emails);
        
        // Should only include non-null emails
        long expectedCount = dbLayer.getAllCustomer().stream()
                .filter(c -> c.getEmail() != null)
                .count();
        
        assertEquals(expectedCount, emails.size());
    }

    @Test
    void testCalculateTotalSalaryInParallel() {
        double totalSalary = service.calculateTotalSalaryInParallel();
        
        // Total should be the sum of all employee salaries
        double expectedTotal = dbLayer.getEmployees().stream()
                .mapToDouble(Employees::getSalary)
                .sum();
        
        assertEquals(expectedTotal, totalSalary);
    }

    @Test
    void testGetEmployeeIdToNameMap() {
        Map<Integer, String> idToNameMap = service.getEmployeeIdToNameMap();
        
        assertNotNull(idToNameMap);
        assertEquals(dbLayer.getEmployees().size(), idToNameMap.size());
        
        // Check that mapping is correct
        dbLayer.getEmployees().forEach(emp -> 
            assertEquals(emp.getName(), idToNameMap.get(emp.getId()))
        );
    }

    @Test
    void testGetSalaryRangesByGrade() {
        Map<String, List<Integer>> rangesByGrade = service.getSalaryRangesByGrade();
        
        assertNotNull(rangesByGrade);
        assertTrue(rangesByGrade.containsKey("A"));
        assertTrue(rangesByGrade.containsKey("B"));
        assertTrue(rangesByGrade.containsKey("C"));
        
        // Check that ranges are correct
        Map<String, List<Employees>> employeesByGrade = service.getEmployeesByGrade();
        employeesByGrade.forEach((grade, employees) -> {
            List<Integer> expectedRanges = employees.stream()
                    .map(emp -> (int) (Math.floor(emp.getSalary() / 10000) * 10000))
                    .sorted()
                    .toList();
            
            assertEquals(expectedRanges.size(), rangesByGrade.get(grade).size());
        });
    }

    @Test
    void testCreateCustomerProfiles() {
        List<CustomerProfile> profiles = service.createCustomerProfiles();
        
        assertNotNull(profiles);
        assertEquals(dbLayer.getAllCustomerAsMap().size(), profiles.size());
        
        // Check that profiles are correctly created
        profiles.forEach(profile -> {
            assertNotNull(profile.getCustomer());
            assertNotNull(profile.getCategory());
        });
    }

    @Test
    void testPrintCustomerEmail() {
        // This method has void return type and just prints to console
        // We can only verify it doesn't throw exceptions
        assertDoesNotThrow(() -> service.printCustomerEmail(1));
        assertDoesNotThrow(() -> service.printCustomerEmail(999)); // Non-existent ID
    }

    @Test
    void testGetCustomerWithMostPhoneNumbers() {
        Optional<Customer> customer = service.getCustomerWithMostPhoneNumbers();
        
        assertTrue(customer.isPresent());
        
        // Check that no customer has more phone numbers
        int maxPhoneNumbers = customer.get().getPhoneNumbers().size();
        dbLayer.getAllCustomer().forEach(c -> 
            assertTrue(c.getPhoneNumbers().size() <= maxPhoneNumbers)
        );
    }

    @Test
    void testGetCustomersWithInvalidEmails() {
        List<Customer> customersWithInvalidEmails = service.getCustomersWithInvalidEmails();
        
        assertNotNull(customersWithInvalidEmails);
        
        // All should have null or invalid email
        customersWithInvalidEmails.forEach(c -> 
            assertTrue(c.getEmail() == null || !c.getEmail().contains("@"))
        );
    }

    @Test
    void testGetEmployeesBySalaryRange() {
        Map<String, List<Employees>> employeesBySalaryRange = service.getEmployeesBySalaryRange();
        
        assertNotNull(employeesBySalaryRange);
        assertTrue(employeesBySalaryRange.containsKey("Low"));
        assertTrue(employeesBySalaryRange.containsKey("Medium"));
        assertTrue(employeesBySalaryRange.containsKey("High"));
        
        // Check that employees are correctly grouped
        employeesBySalaryRange.get("Low").forEach(emp -> 
            assertTrue(emp.getSalary() < 30000)
        );
        
        employeesBySalaryRange.get("Medium").forEach(emp -> 
            assertTrue(emp.getSalary() >= 30000 && emp.getSalary() < 70000)
        );
        
        employeesBySalaryRange.get("High").forEach(emp -> 
            assertTrue(emp.getSalary() >= 70000)
        );
    }

    @Test
    void testGetAverageSalary() {
        double avgSalary = service.getAverageSalary();
        
        // Average should be the average of all employee salaries
        double expectedAvg = dbLayer.getEmployees().stream()
                .mapToDouble(Employees::getSalary)
                .average()
                .orElse(0.0);
        
        assertEquals(expectedAvg, avgSalary);
    }

    @Test
    void testGetHighestPaidEmployeeByGrade() {
        Map<String, Optional<Employees>> highestPaidByGrade = service.getHighestPaidEmployeeByGrade();
        
        assertNotNull(highestPaidByGrade);
        assertTrue(highestPaidByGrade.containsKey("A"));
        assertTrue(highestPaidByGrade.containsKey("B"));
        assertTrue(highestPaidByGrade.containsKey("C"));
        
        // Check that highest paid employees are correct
        Map<String, List<Employees>> employeesByGrade = service.getEmployeesByGrade();
        employeesByGrade.forEach((grade, employees) -> {
            Optional<Employees> highestPaid = highestPaidByGrade.get(grade);
            assertTrue(highestPaid.isPresent());
            
            double maxSalary = highestPaid.get().getSalary();
            employees.forEach(emp -> 
                assertTrue(emp.getSalary() <= maxSalary)
            );
        });
    }

    @Test
    void testGetLowestPaidEmployeeInGrade() {
        String grade = "A";
        Optional<Employees> lowestPaid = service.getLowestPaidEmployeeInGrade(grade);
        
        assertTrue(lowestPaid.isPresent());
        assertEquals(grade, lowestPaid.get().getGrade());
        
        // Check that no grade A employee has lower salary
        double minSalary = lowestPaid.get().getSalary();
        dbLayer.getEmployees().stream()
                .filter(emp -> grade.equals(emp.getGrade()))
                .forEach(emp -> 
                    assertTrue(emp.getSalary() >= minSalary)
                );
    }

    @Test
    void testGetTotalPhoneNumbers() {
        long totalPhoneNumbers = service.getTotalPhoneNumbers();
        
        // Total should be the sum of all customer phone numbers
        long expectedTotal = dbLayer.getAllCustomer().stream()
                .mapToLong(c -> c.getPhoneNumbers().size())
                .sum();
        
        assertEquals(expectedTotal, totalPhoneNumbers);
    }

    @Test
    void testGetCustomerWithLongestName() {
        Optional<Customer> customer = service.getCustomerWithLongestName();
        
        assertTrue(customer.isPresent());
        
        // Check that no customer has longer name
        int maxLength = customer.get().getName().length();
        dbLayer.getAllCustomer().forEach(c -> 
            assertTrue(c.getName().length() <= maxLength)
        );
    }

    @Test
    void testGetEmployeeWithShortestName() {
        Optional<Employees> employee = service.getEmployeeWithShortestName();
        
        assertTrue(employee.isPresent());
        
        // Check that no employee has shorter name
        int minLength = employee.get().getName().length();
        dbLayer.getEmployees().forEach(emp -> 
            assertTrue(emp.getName().length() >= minLength)
        );
    }

    @Test
    void testGetMostCommonGrade() {
        Optional<String> mostCommonGrade = service.getMostCommonGrade();
        
        assertTrue(mostCommonGrade.isPresent());
        
        // Check that it's the grade with most employees
        Map<String, Long> countByGrade = service.getEmployeeCountByGrade();
        String expectedGrade = countByGrade.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
        
        assertEquals(expectedGrade, mostCommonGrade.get());
    }

    @Test
    void testGetLeastCommonGrade() {
        Optional<String> leastCommonGrade = service.getLeastCommonGrade();
        
        assertTrue(leastCommonGrade.isPresent());
        
        // Check that it's the grade with fewest employees
        Map<String, Long> countByGrade = service.getEmployeeCountByGrade();
        String expectedGrade = countByGrade.entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
        
        assertEquals(expectedGrade, leastCommonGrade.get());
    }

    @Test
    void testGetEmployeeWithHighestSalaryDifference() {
        Optional<Employees> employee = service.getEmployeeWithHighestSalaryDifference();
        
        assertTrue(employee.isPresent());
        
        // Check that no employee has higher salary difference from average
        double avgSalary = service.getAverageSalary();
        double maxDiff = Math.abs(employee.get().getSalary() - avgSalary);
        
        dbLayer.getEmployees().forEach(emp -> 
            assertTrue(Math.abs(emp.getSalary() - avgSalary) <= maxDiff)
        );
    }

    @Test
    void testGetEmployeeWithLowestSalaryDifference() {
        Optional<Employees> employee = service.getEmployeeWithLowestSalaryDifference();
        
        assertTrue(employee.isPresent());
        
        // Check that no employee has lower salary difference from average
        double avgSalary = service.getAverageSalary();
        double minDiff = Math.abs(employee.get().getSalary() - avgSalary);
        
        dbLayer.getEmployees().forEach(emp -> 
            assertTrue(Math.abs(emp.getSalary() - avgSalary) >= minDiff)
        );
    }

    @Test
    void testGetCustomerWithMostUniquePhoneNumbers() {
        Optional<Customer> customer = service.getCustomerWithMostUniquePhoneNumbers();
        
        assertTrue(customer.isPresent());
        
        // Check that no customer has more unique phone numbers
        int maxUniquePhoneNumbers = (int) customer.get().getPhoneNumbers().stream().distinct().count();
        
        dbLayer.getAllCustomer().forEach(c -> {
            int uniquePhoneNumbers = (int) c.getPhoneNumbers().stream().distinct().count();
            assertTrue(uniquePhoneNumbers <= maxUniquePhoneNumbers);
        });
    }

    @Test
    void testGetCustomerWithLeastUniquePhoneNumbers() {
        Optional<Customer> customer = service.getCustomerWithLeastUniquePhoneNumbers();
        
        assertTrue(customer.isPresent());
        
        // Check that no customer has fewer unique phone numbers
        int minUniquePhoneNumbers = (int) customer.get().getPhoneNumbers().stream().distinct().count();
        
        dbLayer.getAllCustomer().forEach(c -> {
            int uniquePhoneNumbers = (int) c.getPhoneNumbers().stream().distinct().count();
            assertTrue(uniquePhoneNumbers >= minUniquePhoneNumbers);
        });
    }

    @Test
    void testGetCustomerWithMostPhoneNumbersInCategory() {
        String category = "Premium";
        Optional<Customer> customer = service.getCustomerWithMostPhoneNumbersInCategory(category);
        
        // May not be present if no customers in that category
        if (customer.isPresent()) {
            // Check that no customer in the same category has more phone numbers
            int maxPhoneNumbers = customer.get().getPhoneNumbers().size();
            
            Map<Customer, String> customerMap = dbLayer.getAllCustomerAsMap();
            customerMap.entrySet().stream()
                    .filter(entry -> category.equals(entry.getValue()))
                    .forEach(entry -> 
                        assertTrue(entry.getKey().getPhoneNumbers().size() <= maxPhoneNumbers)
                    );
        }
    }

    @Test
    void testGetCustomerWithLeastPhoneNumbersInCategory() {
        String category = "Premium";
        Optional<Customer> customer = service.getCustomerWithLeastPhoneNumbersInCategory(category);
        
        // May not be present if no customers in that category
        if (customer.isPresent()) {
            // Check that no customer in the same category has fewer phone numbers
            int minPhoneNumbers = customer.get().getPhoneNumbers().size();
            
            Map<Customer, String> customerMap = dbLayer.getAllCustomerAsMap();
            customerMap.entrySet().stream()
                    .filter(entry -> category.equals(entry.getValue()))
                    .forEach(entry -> 
                        assertTrue(entry.getKey().getPhoneNumbers().size() >= minPhoneNumbers)
                    );
        }
    }

    @Test
    void testGetCustomerWithMostUniquePhoneNumbersInCategory() {
        String category = "Premium";
        Optional<Customer> customer = service.getCustomerWithMostUniquePhoneNumbersInCategory(category);
        
        // May not be present if no customers in that category
        if (customer.isPresent()) {
            // Check that no customer in the same category has more unique phone numbers
            int maxUniquePhoneNumbers = (int) customer.get().getPhoneNumbers().stream().distinct().count();
            
            Map<Customer, String> customerMap = dbLayer.getAllCustomerAsMap();
            customerMap.entrySet().stream()
                    .filter(entry -> category.equals(entry.getValue()))
                    .forEach(entry -> {
                        int uniquePhoneNumbers = (int) entry.getKey().getPhoneNumbers().stream().distinct().count();
                        assertTrue(uniquePhoneNumbers <= maxUniquePhoneNumbers);
                    });
        }
    }

    @Test
    void testGetCustomerWithLeastUniquePhoneNumbersInCategory() {
        String category = "Premium";
        Optional<Customer> customer = service.getCustomerWithLeastUniquePhoneNumbersInCategory(category);
        
        // May not be present if no customers in that category
//        if (customer.isPresent()) {
//            // Check that no customer in the same category has fewer unique phone numbers
//            int minUniquePhoneNumbers = (int) customer.get().getPhoneNumbers().stream().distinct().count();
//
//            Map<Customer, String> customerMap = dbLayer.getAllCustomerAsMap();
//            customerMap.entrySet().stream()
//                    .filter(entry -> category.equals(entry.getValue()))
//                    .forEach(entry -> {
//                        int uniquePhoneNumbers = (int) entry.getKey().getPhoneNumbers().stream().distinct().count();
//                        assertTrue(uniquePhoneNumbers >= minUniquePhoneNumbers);
//                    });
//        }

        customer.ifPresent(customer1 -> {
            // Check that no customer in the same category has fewer unique phone numbers
            int minUniquePhoneNumbers = (int) customer.get().getPhoneNumbers().stream().distinct().count();

            Map<Customer, String> customerMap = dbLayer.getAllCustomerAsMap();
            customerMap.entrySet().stream()
                    .filter(entry -> category.equals(entry.getValue()))
                    .forEach(entry -> {
                        int uniquePhoneNumbers = (int) entry.getKey().getPhoneNumbers().stream().distinct().count();
                        assertTrue(uniquePhoneNumbers >= minUniquePhoneNumbers);
                    });
        });
    }
}
