package com.java.functional.FunctionalJava.streams;

import com.java.functional.FunctionalJava.dto.*;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;


public class Java8FunctionalDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // Initialize dependencies
        DbLayer dbLayer = new DbLayer();
        Java8FunctionalService functionalService = new Java8FunctionalService(dbLayer);

        System.out.println("\n===== Basic Transformations =====");
        // Transform data using Function
        List<String> customerNames = functionalService.getCustomerNames();
        System.out.println("Customer names: " + customerNames);

        // Custom mapping example with lambda
        List<String> customerEmails = functionalService.mapData(
                dbLayer.getAllCustomer(),
                customer -> Optional.ofNullable(customer.getEmail()).orElse("No Email")
        );
        System.out.println("Customer emails: " + customerEmails);

        System.out.println("\n===== Filtering =====");
        // Filter data using Predicate
        List<Customer> premiumCustomers = functionalService.getPremiumCustomers();
        System.out.println("Premium customers: " + premiumCustomers.size());
        premiumCustomers.forEach(customer ->
                System.out.println("  - " + customer.getName() + ": " + customer.getPhoneNumbers().size() + " phone numbers")
        );

        // Composite predicate example
        List<Employees> highPaidGradeA = functionalService.getHighSalariedGradeAEmployees();
        System.out.println("\nHigh-paid Grade A employees: " + highPaidGradeA.size());
        highPaidGradeA.forEach(emp ->
                System.out.println("  - " + emp.getName() + ": $" + emp.getSalary())
        );

        System.out.println("\n===== Grouping and Partitioning =====");
        // Grouping example
        Map<String, List<Employees>> employeesByGrade = functionalService.getEmployeesByGrade();
        employeesByGrade.forEach((grade, employees) -> {
            System.out.println("Grade " + grade + " has " + employees.size() + " employees");
        });

        // Average salary by grade
        Map<String, Double> avgSalaryByGrade = functionalService.getAverageSalaryByGrade();
        avgSalaryByGrade.forEach((grade, avgSalary) -> {
            System.out.println("Grade " + grade + " average salary: $" + avgSalary);
        });

        // EmployeeSummary by grade using Lombok Builder
        List<EmployeeSummary> summaries = functionalService.getEmployeesSummaryByGrade();
        System.out.println("\nEmployee Summary by Grade:");
        summaries.forEach(summary -> {
            System.out.println("Grade: " + summary.getGrade());
            System.out.println("  Count: " + summary.getCount());
            System.out.println("  Min Salary: $" + summary.getMinSalary());
            System.out.println("  Max Salary: $" + summary.getMaxSalary());
            System.out.println("  Avg Salary: $" + summary.getAverageSalary());
        });

        // Partitioning example
        Map<Boolean, List<Employees>> salaryPartition = functionalService.partitionEmployeesBySalary(50000);
        System.out.println("\nEmployees with salary > $50,000: " + salaryPartition.get(true).size());
        System.out.println("Employees with salary ≤ $50,000: " + salaryPartition.get(false).size());

        System.out.println("\n===== FlatMap Examples =====");
        // FlatMap example
        List<String> allPhoneNumbers = functionalService.getAllPhoneNumbers();
        System.out.println("All phone numbers: " + allPhoneNumbers.size());
        System.out.println("Sample phone numbers: " + allPhoneNumbers.subList(0, Math.min(5, allPhoneNumbers.size())));

        // Distinct with flatMap
        List<String> uniquePhoneNumbers = functionalService.getUniquePhoneNumbers();
        System.out.println("Unique phone numbers: " + uniquePhoneNumbers.size());
        System.out.println("Sample unique numbers: " + uniquePhoneNumbers.subList(0, Math.min(5, uniquePhoneNumbers.size())));

        System.out.println("\n===== Reduction Operations =====");
        // Sum example
        double totalSalary = functionalService.getTotalSalary();
        System.out.println("Total salary (sum): $" + totalSalary);

        // Reduce example
        double totalSalaryReduce = functionalService.getTotalSalaryWithReduce();
        System.out.println("Total salary (reduce): $" + totalSalaryReduce);

        System.out.println("\n===== Optional Examples =====");
        // Optional examples
        Customer customerWithEmail = dbLayer.getCustomer();
        Customer customerWithoutEmail = dbLayer.getNullEmaillCustomer();

        System.out.println("Email or default (with email): " +
                functionalService.getEmailOrDefault(customerWithEmail, "default@example.com"));
        System.out.println("Email or default (null email): " +
                functionalService.getEmailOrDefault(customerWithoutEmail, "default@example.com"));

        System.out.println("Email domain (with email): " +
                functionalService.getEmailDomain(customerWithEmail));
        System.out.println("Email domain (null email): " +
                functionalService.getEmailDomain(customerWithoutEmail));

        System.out.println("\n===== Sorting Examples =====");
        // Custom sorting
        List<Employees> sortedBySalary = functionalService.getSortedEmployees(
                Comparator.comparing(Employees::getSalary).reversed()
        );
        System.out.println("Top 3 employees by salary:");
        sortedBySalary.stream().limit(3).forEach(emp ->
                System.out.println("  - " + emp.getName() + ": $" + emp.getSalary())
        );

        // Multiple sorting criteria
        List<Employees> sortedByGradeAndSalary = functionalService.getEmployeesSortedByGradeAndSalary();
        System.out.println("\nEmployees sorted by grade and then by salary (descending):");
        sortedByGradeAndSalary.forEach(emp ->
                System.out.println("  - Grade " + emp.getGrade() + ": " + emp.getName() + " ($" + emp.getSalary() + ")")
        );

        System.out.println("\n===== Pagination Example =====");
        // Pagination
        Pagination<Customer> customerPage = functionalService.getCustomerPage(0, 3);
        System.out.println("Customer page 1 (size 3):");
        System.out.println("  Page: " + (customerPage.getPage() + 1));
        System.out.println("  Size: " + customerPage.getSize());
        System.out.println("  Total elements: " + customerPage.getTotalElements());
        System.out.println("  Total pages: " + customerPage.getTotalPages());
        System.out.println("  Content:");
        customerPage.getContent().forEach(customer ->
                System.out.println("    - " + customer.getName())
        );

        System.out.println("\n===== Collector Examples =====");
        // Joining
        String customerNamesString = functionalService.getCustomerNamesAsString();
        System.out.println(customerNamesString);

        // Counting
        Map<String, Long> employeeCountByGrade = functionalService.getEmployeeCountByGrade();
        System.out.println("\nEmployee count by grade:");
        employeeCountByGrade.forEach((grade, count) ->
                System.out.println("  Grade " + grade + ": " + count)
        );

        // ToMap
        Map<Integer, String> employeeIdToNameMap = functionalService.getEmployeeIdToNameMap();
        System.out.println("\nEmployee ID to name map (sample):");
        employeeIdToNameMap.entrySet().stream().limit(3).forEach(entry ->
                System.out.println("  ID " + entry.getKey() + ": " + entry.getValue())
        );

        System.out.println("\n===== Optional Advanced Examples =====");
        // FindFirst with Optional
        Optional<Employees> highestPaidEmployee = functionalService.findHighestPaidEmployee();
        highestPaidEmployee.ifPresent(emp ->
                System.out.println("Highest paid employee: " + emp.getName() + " ($" + emp.getSalary() + ")")
        );

        // AllMatch, AnyMatch
        boolean allGradeA = functionalService.areAllEmployeesGradeA();
        System.out.println("Are all employees Grade A? " + allGradeA);

        boolean anyHighSalaried = functionalService.isAnyEmployeeHighSalaried(90000);
        System.out.println("Is any employee earning more than $90,000? " + anyHighSalaried);

        System.out.println("\n===== CustomerProfile Example =====");
        // CustomerProfile from Customer
        List<CustomerProfile> profiles = functionalService.createCustomerProfiles();
        System.out.println("Customer profiles:");
        profiles.forEach(profile ->
                System.out.println("  - " + profile.getCustomer().getName() + " (Category: " + profile.getCategory() + ")")
        );

        System.out.println("\n===== Async Example =====");
        // CompletableFuture
        CompletableFuture<List<String>> emailsFuture = functionalService.getCustomerEmailsAsync();
        List<String> emails = emailsFuture.get();  // Blocking for demo purposes
        System.out.println("Emails retrieved asynchronously: " + emails.size());

        System.out.println("\n===== Parallel Stream Example =====");
        // Parallel Stream
        long start = System.currentTimeMillis();
        double parallelTotal = functionalService.calculateTotalSalaryInParallel();
        long end = System.currentTimeMillis();
        System.out.println("Total salary (parallel): $" + parallelTotal);
        System.out.println("Parallel processing time: " + (end - start) + " ms");

        // ===== Additional Methods Testing =====
        System.out.println("\n===== Debug Logging Example =====");
        // Peek for debugging
        List<Employees> loggedEmployees = functionalService.getEmployeesWithSalaryLog();
        System.out.println("Employees with salary logging: " + loggedEmployees.size());

        System.out.println("\n===== Salary Ranges Example =====");
        // Mapping collector
        Map<String, List<Integer>> salaryRangesByGrade = functionalService.getSalaryRangesByGrade();
        System.out.println("Salary ranges by grade:");
        salaryRangesByGrade.forEach((grade, ranges) -> {
            System.out.println("  Grade " + grade + ": " + ranges);
        });

        System.out.println("\n===== Error Handling Example =====");
        // Optional and map with error handling
        functionalService.printCustomerEmail(1);  // Assuming customer with ID 1 exists
        functionalService.printCustomerEmail(999);  // Assuming customer with ID 999 doesn't exist

        System.out.println("\n===== Additional Customer Analysis =====");
        // Finding customer with most phone numbers
        Optional<Customer> customerWithMostPhones = functionalService.getCustomerWithMostPhoneNumbers();
        customerWithMostPhones.ifPresent(c -> 
            System.out.println("Customer with most phone numbers: " + c.getName() + 
                " (" + c.getPhoneNumbers().size() + " numbers)")
        );

        // Finding customers with invalid emails
        List<Customer> customersWithInvalidEmails = functionalService.getCustomersWithInvalidEmails();
        System.out.println("\nCustomers with invalid emails: " + customersWithInvalidEmails.size());
        customersWithInvalidEmails.forEach(c -> 
            System.out.println("  - " + c.getName() + ": " + c.getEmail())
        );

        System.out.println("\n===== Employee Salary Analysis =====");
        // Grouping employees by salary range
        Map<String, List<Employees>> employeesBySalaryRange = functionalService.getEmployeesBySalaryRange();
        System.out.println("Employees by salary range:");
        employeesBySalaryRange.forEach((range, emps) -> {
            System.out.println("  " + range + " salary: " + emps.size() + " employees");
        });

        // Average salary
        double avgSalary = functionalService.getAverageSalary();
        System.out.println("\nAverage salary across all employees: $" + avgSalary);

        // Highest paid employee by grade
        Map<String, Optional<Employees>> highestPaidByGrade = functionalService.getHighestPaidEmployeeByGrade();
        System.out.println("\nHighest paid employee by grade:");
        highestPaidByGrade.forEach((grade, empOpt) -> {
            empOpt.ifPresent(emp -> 
                System.out.println("  Grade " + grade + ": " + emp.getName() + " ($" + emp.getSalary() + ")")
            );
        });

        // Lowest paid employee in grade A
        Optional<Employees> lowestPaidGradeA = functionalService.getLowestPaidEmployeeInGrade("A");
        System.out.println("\nLowest paid employee in Grade A:");
        lowestPaidGradeA.ifPresent(emp -> 
            System.out.println("  " + emp.getName() + " ($" + emp.getSalary() + ")")
        );

        System.out.println("\n===== Phone Number Analysis =====");
        // Total phone numbers
        long totalPhoneNumbers = functionalService.getTotalPhoneNumbers();
        System.out.println("Total phone numbers across all customers: " + totalPhoneNumbers);

        System.out.println("\n===== Name Length Analysis =====");
        // Customer with longest name
        Optional<Customer> customerWithLongestName = functionalService.getCustomerWithLongestName();
        customerWithLongestName.ifPresent(c -> 
            System.out.println("Customer with longest name: " + c.getName() + 
                " (" + c.getName().length() + " characters)")
        );

        // Employee with shortest name
        Optional<Employees> employeeWithShortestName = functionalService.getEmployeeWithShortestName();
        employeeWithShortestName.ifPresent(emp -> 
            System.out.println("Employee with shortest name: " + emp.getName() + 
                " (" + emp.getName().length() + " characters)")
        );

        System.out.println("\n===== Grade Analysis =====");
        // Most common grade
        Optional<String> mostCommonGrade = functionalService.getMostCommonGrade();
        mostCommonGrade.ifPresent(grade -> 
            System.out.println("Most common grade: " + grade + 
                " (" + employeeCountByGrade.get(grade) + " employees)")
        );

        // Least common grade
        Optional<String> leastCommonGrade = functionalService.getLeastCommonGrade();
        leastCommonGrade.ifPresent(grade -> 
            System.out.println("Least common grade: " + grade + 
                " (" + employeeCountByGrade.get(grade) + " employees)")
        );

        System.out.println("\n===== Salary Difference Analysis =====");
        // Employee with highest salary difference from average
        Optional<Employees> empWithHighestDiff = functionalService.getEmployeeWithHighestSalaryDifference();
        empWithHighestDiff.ifPresent(emp -> 
            System.out.println("Employee with highest salary difference from average: " + 
                emp.getName() + " ($" + emp.getSalary() + ", diff: $" + 
                Math.abs(emp.getSalary() - avgSalary) + ")")
        );

        // Employee with lowest salary difference from average
        Optional<Employees> empWithLowestDiff = functionalService.getEmployeeWithLowestSalaryDifference();
        empWithLowestDiff.ifPresent(emp -> 
            System.out.println("Employee with lowest salary difference from average: " + 
                emp.getName() + " ($" + emp.getSalary() + ", diff: $" + 
                Math.abs(emp.getSalary() - avgSalary) + ")")
        );

        System.out.println("\n===== Unique Phone Numbers Analysis =====");
        // Customer with most unique phone numbers
        Optional<Customer> customerWithMostUniquePhones = functionalService.getCustomerWithMostUniquePhoneNumbers();
        customerWithMostUniquePhones.ifPresent(c -> {
            int uniqueCount = c.getPhoneNumbers().stream().distinct().toList().size();
            System.out.println("Customer with most unique phone numbers: " + c.getName() + 
                " (" + uniqueCount + " unique numbers)");
        });

        // Customer with least unique phone numbers
        Optional<Customer> customerWithLeastUniquePhones = functionalService.getCustomerWithLeastUniquePhoneNumbers();
        customerWithLeastUniquePhones.ifPresent(c -> {
            int uniqueCount = c.getPhoneNumbers().stream().distinct().toList().size();
            System.out.println("Customer with least unique phone numbers: " + c.getName() + 
                " (" + uniqueCount + " unique numbers)");
        });

        System.out.println("\n===== Category-based Phone Number Analysis =====");
        // Customer with most phone numbers in Premium category
        Optional<Customer> premiumCustomerWithMostPhones = 
            functionalService.getCustomerWithMostPhoneNumbersInCategory("Premium");
        premiumCustomerWithMostPhones.ifPresent(c -> 
            System.out.println("Premium customer with most phone numbers: " + c.getName() + 
                " (" + c.getPhoneNumbers().size() + " numbers)")
        );

        // Customer with least phone numbers in Regular category
        Optional<Customer> regularCustomerWithLeastPhones = 
            functionalService.getCustomerWithLeastPhoneNumbersInCategory("Regular");
        regularCustomerWithLeastPhones.ifPresent(c -> 
            System.out.println("Regular customer with least phone numbers: " + c.getName() + 
                " (" + c.getPhoneNumbers().size() + " numbers)")
        );

        // Customer with most unique phone numbers in Premium category
        Optional<Customer> premiumCustomerWithMostUniquePhones = 
            functionalService.getCustomerWithMostUniquePhoneNumbersInCategory("Premium");
        premiumCustomerWithMostUniquePhones.ifPresent(c -> {
            int uniqueCount = c.getPhoneNumbers().stream().distinct().toList().size();
            System.out.println("Premium customer with most unique phone numbers: " + c.getName() + 
                " (" + uniqueCount + " unique numbers)");
        });

        // Customer with least unique phone numbers in Regular category
        Optional<Customer> regularCustomerWithLeastUniquePhones = 
            functionalService.getCustomerWithLeastUniquePhoneNumbersInCategory("Regular");
        regularCustomerWithLeastUniquePhones.ifPresent(c -> {
            int uniqueCount = c.getPhoneNumbers().stream().distinct().toList().size();
            System.out.println("Regular customer with least unique phone numbers: " + c.getName() + 
                " (" + uniqueCount + " unique numbers)");
        });
    }
}