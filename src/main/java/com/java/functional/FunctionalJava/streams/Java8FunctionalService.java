package com.java.functional.FunctionalJava.streams;

import com.java.functional.FunctionalJava.dto.*;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

//@Service
public class Java8FunctionalService {

    private final DbLayer dbLayer;

    // Constructor injection
    public Java8FunctionalService(DbLayer dbLayer) {
        this.dbLayer = dbLayer;
    }

    // Example of using Function interface to transform data
    public List<String> getCustomerNames() {
        return mapData(dbLayer.getAllCustomer(), Customer::getName);
    }

    public <T, R> List<R> mapData(List<T> list, Function<T, R> mapper) {
        return list.stream()
                .map(mapper)
                .collect(Collectors.toList());
    }

    // Example of using Predicate for filtering
    public List<Customer> getPremiumCustomers() {
        return filterData(dbLayer.getAllCustomer(),
                customer -> customer.getPhoneNumbers().size() > 1);
    }

    public <T> List<T> filterData(List<T> list, Predicate<T> predicate) {
        return list.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    // Example of composing predicates
    public List<Employees> getHighSalariedGradeAEmployees() {
        Predicate<Employees> isGradeA = emp -> "A".equals(emp.getGrade());
        Predicate<Employees> isHighSalaried = emp -> emp.getSalary() > 70000;

        return dbLayer.getEmployees().stream()
                .filter(isGradeA.and(isHighSalaried))
                .collect(Collectors.toList());
    }

    // Example of grouping data
    public Map<String, List<Employees>> getEmployeesByGrade() {
        return dbLayer.getEmployees().stream()
                .collect(Collectors.groupingBy(Employees::getGrade));
    }

    // Example of more complex grouping with downstream collector
    public Map<String, Double> getAverageSalaryByGrade() {
        return dbLayer.getEmployees().stream()
                .collect(Collectors.groupingBy(
                        Employees::getGrade,
                        Collectors.averagingDouble(Employees::getSalary)
                ));
    }

    // Example of creating DTOs with streams
    public List<EmployeeSummary> getEmployeesSummaryByGrade() {
        Map<String, List<Employees>> employeesByGrade = getEmployeesByGrade();

        return employeesByGrade.entrySet().stream()
                .map(entry -> {
                    String grade = entry.getKey();
                    List<Employees> employees = entry.getValue();

                    double avgSalary = employees.stream()
                            .mapToDouble(Employees::getSalary)
                            .average()
                            .orElse(0.0);

                    double minSalary = employees.stream()
                            .mapToDouble(Employees::getSalary)
                            .min()
                            .orElse(0.0);

                    double maxSalary = employees.stream()
                            .mapToDouble(Employees::getSalary)
                            .max()
                            .orElse(0.0);

                    return EmployeeSummary.builder()
                            .grade(grade)
                            .averageSalary(avgSalary)
                            .minSalary(minSalary)
                            .maxSalary(maxSalary)
                            .count((long) employees.size())
                            .build();
                })
                .collect(Collectors.toList());
    }

    // Example of partitioning data
    public Map<Boolean, List<Employees>> partitionEmployeesBySalary(double threshold) {
        return dbLayer.getEmployees().stream()
                .collect(Collectors.partitioningBy(emp -> emp.getSalary() > threshold));
    }

    // Example of flatMap for nested collections
    public List<String> getAllPhoneNumbers() {
        return dbLayer.getAllCustomer().stream()
                .flatMap(customer -> customer.getPhoneNumbers().stream())
                .collect(Collectors.toList());
    }

    // Example of distinct with flatMap
    public List<String> getUniquePhoneNumbers() {
        return dbLayer.getAllCustomer().stream()
                .flatMap(customer -> customer.getPhoneNumbers().stream())
                .distinct()
                .collect(Collectors.toList());
    }

    // Example of reduce operation
    public double getTotalSalary() {
        return dbLayer.getEmployees().stream()
                .mapToDouble(Employees::getSalary)
                .sum();
    }

    // Example of reduce with identity and accumulator
    public double getTotalSalaryWithReduce() {
        return dbLayer.getEmployees().stream()
                .map(Employees::getSalary)
                .reduce(0.0, Double::sum);
    }

    // Example of using Optional for safer null handling
    public String getEmailOrDefault(Customer customer, String defaultEmail) {
        return Optional.ofNullable(customer.getEmail())
                .orElse(defaultEmail);
    }

    // Example of Optional with transformation
    public String getEmailDomain(Customer customer) {
        return Optional.ofNullable(customer.getEmail())
                .filter(email -> email.contains("@"))
                .map(email -> email.substring(email.indexOf("@") + 1))
                .orElse("No valid email");
    }

    // Example of custom sorting
    public List<Employees> getSortedEmployees(Comparator<Employees> comparator) {
        return dbLayer.getEmployees().stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    // Example of multiple sort criteria
    public List<Employees> getEmployeesSortedByGradeAndSalary() {
        return dbLayer.getEmployees().stream()
                .sorted(Comparator.comparing(Employees::getGrade)
                        .thenComparing(Employees::getSalary, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    // Example of pagination using the Pagination DTO
    public Pagination<Customer> getCustomerPage(int page, int size) {
        List<Customer> allCustomers = dbLayer.getAllCustomer();
        long totalElements = allCustomers.size();

        List<Customer> pageContent = allCustomers.stream()
                .skip((long) page * size)
                .limit(size)
                .collect(Collectors.toList());

        return Pagination.of(pageContent, page, size, totalElements);
    }

    // Example of counting
    public Map<String, Long> getEmployeeCountByGrade() {
        return dbLayer.getEmployees().stream()
                .collect(Collectors.groupingBy(
                        Employees::getGrade,
                        Collectors.counting()
                ));
    }

    // Example of joining
    public String getCustomerNamesAsString() {
        return dbLayer.getAllCustomer().stream()
                .map(Customer::getName)
                .collect(Collectors.joining(", ", "Customers: ", "."));
    }

    // Example of findFirst with Optional
    public Optional<Employees> findHighestPaidEmployee() {
        return dbLayer.getEmployees().stream()
                .max(Comparator.comparing(Employees::getSalary));
    }

    // Example of anyMatch, allMatch, noneMatch
    public boolean areAllEmployeesGradeA() {
        return dbLayer.getEmployees().stream()
                .allMatch(emp -> "A".equals(emp.getGrade()));
    }

    public boolean isAnyEmployeeHighSalaried(double threshold) {
        return dbLayer.getEmployees().stream()
                .anyMatch(emp -> emp.getSalary() > threshold);
    }

    // Example of peek for debugging or side effects
    public List<Employees> getEmployeesWithSalaryLog() {
        return dbLayer.getEmployees().stream()
                .peek(emp -> System.out.println("Processing employee: " + emp.getName()))
                .filter(emp -> emp.getSalary() > 50000)
                .peek(emp -> System.out.println("High salary: " + emp.getName() + " - " + emp.getSalary()))
                .collect(Collectors.toList());
    }

    // Example of CompletableFuture for async operations
    public CompletableFuture<List<Customer>> getCustomersAsync() {
        return CompletableFuture.supplyAsync(dbLayer::getAllCustomer);
    }

    // Example of composing CompletableFutures
    public CompletableFuture<List<String>> getCustomerEmailsAsync() {
        return getCustomersAsync()
                .thenApply(customers -> customers.stream()
                        .map(Customer::getEmail)
                        .filter(email -> email != null)
                        .collect(Collectors.toList()));
    }

    // Example of parallel streams
    public double calculateTotalSalaryInParallel() {
        return dbLayer.getEmployees().parallelStream()
                .mapToDouble(Employees::getSalary)
                .sum();
    }

    // Example of toMap collector
    public Map<Integer, String> getEmployeeIdToNameMap() {
        return dbLayer.getEmployees().stream()
                .collect(Collectors.toMap(
                        Employees::getId,
                        Employees::getName,
                        (name1, name2) -> name1 // Merge function in case of duplicates
                ));
    }

    // Example of mapping collector
    public Map<String, List<Integer>> getSalaryRangesByGrade() {
        return dbLayer.getEmployees().stream()
                .collect(Collectors.groupingBy(
                        Employees::getGrade,
                        Collectors.mapping(
                                employee -> salaryToRange(employee.getSalary()),
                                Collectors.toList()
                        )
                ));
    }

    private Integer salaryToRange(double salary) {
        return (int) (Math.floor(salary / 10000) * 10000);
    }

    // Example of converting to CustomerProfile objects
    public List<CustomerProfile> createCustomerProfiles() {
        Map<Customer, String> customerMap = dbLayer.getAllCustomerAsMap();

        return customerMap.entrySet().stream()
                .map(entry -> CustomerProfile.fromCustomer(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    // Example of Optional and map with error handling
    public void printCustomerEmail(int customerId) {
        dbLayer.getAllCustomer().stream()
                .filter(c -> c.getId() == customerId)
                .findFirst()
                .map(Customer::getEmail)
                .ifPresentOrElse(
                        email -> System.out.println("Email: " + email),
                        () -> System.out.println("No email found")
                );
    }

    // Additional Scenarios and Use Cases

    // Example of finding the customer with the most phone numbers
    public Optional<Customer> getCustomerWithMostPhoneNumbers() {
        return dbLayer.getAllCustomer().stream()
                .max(Comparator.comparingInt(c -> c.getPhoneNumbers().size()));
    }

    // Example of filtering customers with invalid emails
    public List<Customer> getCustomersWithInvalidEmails() {
        return dbLayer.getAllCustomer().stream()
                .filter(customer -> customer.getEmail() == null || !customer.getEmail().contains("@"))
                .collect(Collectors.toList());
    }

    // Example of grouping employees by salary ranges
    public Map<String, List<Employees>> getEmployeesBySalaryRange() {
        return dbLayer.getEmployees().stream()
                .collect(Collectors.groupingBy(emp -> {
                    double salary = emp.getSalary();
                    if (salary < 30000) return "Low";
                    else if (salary < 70000) return "Medium";
                    else return "High";
                }));
    }

    // Example of finding the average salary of all employees
    public double getAverageSalary() {
        return dbLayer.getEmployees().stream()
                .mapToDouble(Employees::getSalary)
                .average()
                .orElse(0.0);
    }

    // Example of finding the employee with the highest salary in each grade
    public Map<String, Optional<Employees>> getHighestPaidEmployeeByGrade() {
        return dbLayer.getEmployees().stream()
                .collect(Collectors.groupingBy(
                        Employees::getGrade,
                        Collectors.maxBy(Comparator.comparing(Employees::getSalary))
                ));
    }

    // Example of finding the employee with the lowest salary in a specific grade
    public Optional<Employees> getLowestPaidEmployeeInGrade(String grade) {
        return dbLayer.getEmployees().stream()
                .filter(emp -> emp.getGrade().equals(grade))
                .min(Comparator.comparing(Employees::getSalary));
    }

    // Example of finding the total number of phone numbers for all customers
    public long getTotalPhoneNumbers() {
        return dbLayer.getAllCustomer().stream()
                .flatMap(customer -> customer.getPhoneNumbers().stream())
                .count();
    }

    // Example of finding the customer with the longest name
    public Optional<Customer> getCustomerWithLongestName() {
        return dbLayer.getAllCustomer().stream()
                .max(Comparator.comparingInt(c -> c.getName().length()));
    }

    // Example of finding the employee with the shortest name
    public Optional<Employees> getEmployeeWithShortestName() {
        return dbLayer.getEmployees().stream()
                .min(Comparator.comparingInt(emp -> emp.getName().length()));
    }

    // Example of finding the most common grade among employees
    public Optional<String> getMostCommonGrade() {
        return dbLayer.getEmployees().stream()
                .collect(Collectors.groupingBy(
                        Employees::getGrade,
                        Collectors.counting()
                ))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);
    }

    // Example of finding the least common grade among employees
    public Optional<String> getLeastCommonGrade() {
        return dbLayer.getEmployees().stream()
                .collect(Collectors.groupingBy(
                        Employees::getGrade,
                        Collectors.counting()
                ))
                .entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);
    }

    // Example of finding the employee with the highest salary difference from the average
    public Optional<Employees> getEmployeeWithHighestSalaryDifference() {
        double averageSalary = getAverageSalary();
        return dbLayer.getEmployees().stream()
                .max(Comparator.comparingDouble(emp -> Math.abs(emp.getSalary() - averageSalary)));
    }

    // Example of finding the employee with the lowest salary difference from the average
    public Optional<Employees> getEmployeeWithLowestSalaryDifference() {
        double averageSalary = getAverageSalary();
        return dbLayer.getEmployees().stream()
                .min(Comparator.comparingDouble(emp -> Math.abs(emp.getSalary() - averageSalary)));
    }

    // Example of finding the customer with the most unique phone numbers
    public Optional<Customer> getCustomerWithMostUniquePhoneNumbers() {
        return dbLayer.getAllCustomer().stream()
                .max(Comparator.comparingInt(c -> c.getPhoneNumbers().stream()
                        .collect(Collectors.toSet())
                        .size()));
    }

    // Example of finding the customer with the least unique phone numbers
    public Optional<Customer> getCustomerWithLeastUniquePhoneNumbers() {
        return dbLayer.getAllCustomer().stream()
                .min(Comparator.comparingInt(c -> c.getPhoneNumbers().stream()
                        .collect(Collectors.toSet())
                        .size()));
    }

    // Example of finding the customer with the most phone numbers in a specific category
    public Optional<Customer> getCustomerWithMostPhoneNumbersInCategory(String category) {
        Map<Customer, String> customerMap = dbLayer.getAllCustomerAsMap();
        return customerMap.entrySet().stream()
                .filter(entry -> entry.getValue().equals(category))
                .map(Map.Entry::getKey)
                .max(Comparator.comparingInt(c -> c.getPhoneNumbers().size()));
    }

    // Example of finding the customer with the least phone numbers in a specific category
    public Optional<Customer> getCustomerWithLeastPhoneNumbersInCategory(String category) {
        Map<Customer, String> customerMap = dbLayer.getAllCustomerAsMap();
        return customerMap.entrySet().stream()
                .filter(entry -> entry.getValue().equals(category))
                .map(Map.Entry::getKey)
                .min(Comparator.comparingInt(c -> c.getPhoneNumbers().size()));
    }

    // Example of finding the customer with the most unique phone numbers in a specific category
    public Optional<Customer> getCustomerWithMostUniquePhoneNumbersInCategory(String category) {
        Map<Customer, String> customerMap = dbLayer.getAllCustomerAsMap();
        return customerMap.entrySet().stream()
                .filter(entry -> entry.getValue().equals(category))
                .map(Map.Entry::getKey)
                .max(Comparator.comparingInt(c -> c.getPhoneNumbers().stream()
                        .collect(Collectors.toSet())
                        .size()));
    }

    // Example of finding the customer with the least unique phone numbers in a specific category
    public Optional<Customer> getCustomerWithLeastUniquePhoneNumbersInCategory(String category) {
        Map<Customer, String> customerMap = dbLayer.getAllCustomerAsMap();
        return customerMap.entrySet().stream()
                .filter(entry -> entry.getValue().equals(category))
                .map(Map.Entry::getKey)
                .min(Comparator.comparingInt(c -> c.getPhoneNumbers().stream()
                        .collect(Collectors.toSet())
                        .size()));
    }
}