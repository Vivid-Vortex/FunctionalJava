package com.java.functional.FunctionalJava.streams;

import com.java.functional.FunctionalJava.dto.Customer;
import com.java.functional.FunctionalJava.dto.Employees;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * This class demonstrates various Stream API operations in Java.
 * 
 * INTERMEDIATE OPERATIONS always return a new stream and are lazy in nature.
 * They start producing new stream elements and send them to the next operation.
 * Examples: filter, map, flatMap, sorted, peek, distinct, limit, skip
 *
 * TERMINAL OPERATIONS produce a result or side effect and consume the stream.
 * After a terminal operation is called, the stream cannot be used again.
 * Examples: forEach, toArray, reduce, collect, min, max, count, anyMatch, allMatch, noneMatch, findFirst, findAny
 *
 * SHORT CIRCUITING OPERATIONS:
 * - Intermediate: limit(), skip() - may produce finite stream for an infinite stream
 * - Terminal: anyMatch, allMatch, noneMatch, findFirst, findAny - may terminate in finite time for infinite stream
 */
@SuppressWarnings("unused")
public class StreamApis {

    private final DbLayer dbLayer;

    public StreamApis(DbLayer dbLayer) {
        this.dbLayer = dbLayer;
    }

    /**
     * Demonstrates the map operation for transforming data from one form to another.
     * Examples: lowercase to uppercase, extracting specific attributes from objects.
     * Map is an intermediate operation that returns a stream.
     */
    public void testMap() {
        List<Customer> customers = dbLayer.getCustStrema();

        // List of Customer to List of String (emails) - Data Transformation
        List<String> emails = customers.stream()
                .map(Customer::getEmail)
                .collect(Collectors.toList());
        System.out.println("Emails from customers: " + emails);

        // Extract customer IDs
        List<Integer> ids = customers.stream()
                .map(Customer::getId)
                .collect(Collectors.toList());
        System.out.println("Customer IDs: " + ids);

        // Extract customer names
        List<String> names = customers.stream()
                .map(Customer::getName)
                .collect(Collectors.toList());
        System.out.println("Customer names: " + names);

        // Extract phone numbers (returns List<List<String>>)
        List<List<String>> phoneNumbersList = customers.stream()
                .map(Customer::getPhoneNumbers)
                .collect(Collectors.toList());
        System.out.println("Phone numbers (nested lists): " + phoneNumbersList);
    }

    /**
     * Demonstrates the flatMap operation for flattening nested collections.
     * Unlike map which produces one-to-one mapping, flatMap produces one-to-many mapping.
     */
    public void testFlatMap() {
        List<Customer> customers = dbLayer.getCustStrema();

        // Without flatMap - returns List<List<String>>
        List<List<String>> phoneNumbersList = customers.stream()
                .filter(Objects::nonNull)  // Filter out null customers
                .map(Customer::getPhoneNumbers)
                .collect(Collectors.toList());
        System.out.println("Without flatMap - nested lists: " + phoneNumbersList);

        // With flatMap - returns flattened List<String>
        List<String> phoneNumbers = customers.stream()
                .filter(Objects::nonNull)  // Filter out null customers
                .filter(customer -> customer.getPhoneNumbers() != null)  // Filter out null phone number lists
                .flatMap(customer -> customer.getPhoneNumbers().stream())
                .collect(Collectors.toList());
        System.out.println("With flatMap - flattened list: " + phoneNumbers);
    }

    /**
     * Demonstrates how to convert a List of Lists into a single List using flatMap.
     */
    public void convertListOfListToSingleList() {
        // Example with List<List<String>>
        List<List<String>> strListOfList = new ArrayList<>();
        
        List<String> strList1 = new ArrayList<>();
        strList1.add("Apple");
        strList1.add("Banana");
        strListOfList.add(strList1);

        List<String> strList2 = new ArrayList<>();
        strList2.add("Apple2");
        strList2.add("Banana2");
        strListOfList.add(strList2);

        // Flatten the list of lists to a single list
        List<String> flattenedList = strListOfList.stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
        System.out.println("Flattened string list: " + flattenedList);
        
        // Find a specific element
        Optional<String> foundItem = strListOfList.stream()
                .flatMap(List::stream)
                .filter("Apple2"::equals)
                .findFirst();
        System.out.println("Found item: " + foundItem.orElse("Not found"));

        // Example with List<List<Employees>>
        List<List<Employees>> empListOfList = new ArrayList<>();
        
        List<Employees> empList1 = new ArrayList<>();
        empList1.add(new Employees(1, "John", "A", 50000));
        empList1.add(new Employees(2, "Aman", "B", 60000));
        empListOfList.add(empList1);

        List<Employees> empList2 = new ArrayList<>();
        empList2.add(new Employees(3, "Sarah", "A", 70000));
        empList2.add(new Employees(4, "Mike", "C", 55000));
        empListOfList.add(empList2);

        // Flatten and filter employees
        List<Employees> filteredEmployees = empListOfList.stream()
                .flatMap(List::stream)
                .filter(emp -> emp.getName() != null && "Aman".equals(emp.getName()))
                .collect(Collectors.toList());
        System.out.println("Filtered employees: " + filteredEmployees);
    }

    /**
     * Demonstrates the mapToObj operation for converting primitive streams to object streams.
     * Useful for creating objects from primitive values.
     */
    public void testMapToObj() {
        // Create 50 Customer objects with IDs from 1 to 50
        List<Customer> customers = IntStream.rangeClosed(1, 50)
                .peek(i -> System.out.println("Processing count: " + i))
                .mapToObj(i -> new Customer(i, "Customer" + i, "customer" + i + "@example.com", null))
                .collect(Collectors.toList());
        
        System.out.println("Created " + customers.size() + " customers");
        
        // Example of other primitive stream operations
        long sum = IntStream.rangeClosed(1, 10).sum();
        System.out.println("Sum of numbers 1-10: " + sum);
        
        double average = IntStream.rangeClosed(1, 10).average().orElse(0);
        System.out.println("Average of numbers 1-10: " + average);
    }

    /**
     * Demonstrates map and reduce operations.
     * Map transforms data, while reduce combines stream elements into a single result.
     */
    public void testMapReduce() {
        List<Integer> numbers = Arrays.asList(3, 7, 8, 1, 5, 9);
        List<String> numbersStr = Arrays.asList("3", "7", "8", "1", "5", "9");

        // Sum of numbers using reduce
        int sum = numbers.stream().reduce(0, Integer::sum);
        System.out.println("Sum using reduce: " + sum);
        
        // Sum of numbers after filtering
        int sumFiltered = numbers.stream()
                .filter(n -> n > 5)
                .reduce(0, Integer::sum);
        System.out.println("Sum of numbers > 5: " + sumFiltered);
        
        // Convert strings to integers and sum
        int sumFromStrings = numbersStr.stream()
                .map(Integer::valueOf)
                .reduce(0, Integer::sum);
        System.out.println("Sum from strings: " + sumFromStrings);
        
        // Concatenate strings
        String concatenated = numbersStr.stream()
                .reduce("", (a, b) -> a + b);
        System.out.println("Concatenated string: " + concatenated);
        
        // Find maximum value
        int max = numbers.stream()
                .reduce(Integer.MIN_VALUE, Integer::max);
        System.out.println("Maximum value: " + max);
        
        // Find longest string
        List<String> words = Arrays.asList("coreJava", "Spring", "Hibernate");
        String longest = words.stream()
                .reduce((word1, word2) -> word1.length() > word2.length() ? word1 : word2)
                .orElse("");
        System.out.println("Longest word: " + longest);
        
        // Calculate average salary of grade A employees
        double avgSalary = dbLayer.getEmployees().stream()
                .filter(employee -> "A".equalsIgnoreCase(employee.getGrade()))
                .mapToDouble(Employees::getSalary)
                .average()
                .orElse(0.0);
        System.out.println("Average salary of grade A employees: " + avgSalary);
    }

    /**
     * Demonstrates the use of Optional without streams.
     * Optional is a container that may or may not contain a non-null value.
     */
    public void testPureOptionalWithoutStream() throws FileNotFoundException {
        Customer customerWithNullEmail = dbLayer.getNullEmaillCustomer();
        Customer customerWithEmail = dbLayer.getCustomer();
        
        // Three ways to create Optional objects:
        // 1. empty()
        Optional<Object> emptyOptional = Optional.empty();
        System.out.println("Empty Optional: " + emptyOptional);
        
        // 2. of() - throws NullPointerException if value is null
        try {
            Optional<String> emailOptional = Optional.of(customerWithEmail.getEmail());
            System.out.println("Optional with non-null value: " + emailOptional);
            
            // This will throw NullPointerException
            Optional.of(customerWithNullEmail.getEmail());
        } catch (NullPointerException e) {
            System.out.println("Exception occurred: NullPointerException with Optional.of()");
            writeLogsToLogsFile(e);
        }
        
        // 3. ofNullable() - returns empty Optional if value is null
        Optional<String> nullableEmail = Optional.ofNullable(customerWithNullEmail.getEmail());
        System.out.println("Optional with nullable value: " + nullableEmail);
        
        Optional<String> nonNullableEmail = Optional.ofNullable(customerWithEmail.getEmail());
        System.out.println("Optional with non-null value: " + nonNullableEmail);
        
        // Getting values from Optional
        try {
            // This will throw NoSuchElementException if empty
            String email = nullableEmail.get();
            System.out.println("This won't be printed");
        } catch (NoSuchElementException e) {
            System.out.println("Exception occurred: NoSuchElementException with get()");
            writeLogsToLogsFile(e);
        }
        
        // Safe way to get value
        if (nonNullableEmail.isPresent()) {
            System.out.println("Email: " + nonNullableEmail.get());
        }
        
        // Using orElse to provide default value
        String email1 = nullableEmail.orElse("default@example.com");
        System.out.println("Email with default: " + email1);
        
        String email2 = nonNullableEmail.orElse("default@example.com");
        System.out.println("Email with default (not used): " + email2);
        
        // Using orElseGet with Supplier
        String email3 = nullableEmail.orElseGet(() -> "generated@example.com");
        System.out.println("Email with generated default: " + email3);
        
        // Using map to transform value if present
        String upperCaseEmail = nonNullableEmail
                .map(String::toUpperCase)
                .orElse("NO EMAIL");
        System.out.println("Uppercase email: " + upperCaseEmail);
    }

    /**
     * Demonstrates the use of Optional with streams.
     */
    public void testOptionalWithStream() throws Exception {
        Optional<Customer> customer = dbLayer.getCustomerByEmail("bman@gmail.com");
        customer.ifPresent(c -> System.out.println("Found customer: " + c.getName()));
        
        // Try with non-existent email
        Optional<Customer> nonExistentCustomer = dbLayer.getCustomerByEmail("nonexistent@example.com");
        System.out.println("Customer exists: " + nonExistentCustomer.isPresent());
    }

    /**
     * Demonstrates the orElse method of Optional.
     * Returns the value if present, otherwise returns the provided default value.
     */
    public void orElseExample() {
        Optional<String> presentValue = Optional.of("Hello, World!");
        Optional<String> emptyValue = Optional.empty();

        // Example with present value
        String result1 = presentValue.orElse("Default Value");
        System.out.println("orElse with present value: " + result1); // Prints: Hello, World!

        // Example with empty value
        String result2 = emptyValue.orElse("Default Value");
        System.out.println("orElse with empty value: " + result2); // Prints: Default Value
    }

    /**
     * Demonstrates the orElseGet method of Optional.
     * Returns the value if present, otherwise returns the result produced by the provided Supplier.
     */
    public void orElseGetExample() {
        Optional<String> presentValue = Optional.of("Hello, World!");
        Optional<String> emptyValue = Optional.empty();

        // Example with present value
        String result1 = presentValue.orElseGet(() -> "Generated Default Value");
        System.out.println("orElseGet with present value: " + result1); // Prints: Hello, World!

        // Example with empty value
        String result2 = emptyValue.orElseGet(() -> "Generated Default Value");
        System.out.println("orElseGet with empty value: " + result2); // Prints: Generated Default Value
    }

    /**
     * Demonstrates the orElseThrow method of Optional.
     * Returns the value if present, otherwise throws an exception created by the provided Supplier.
     */
    public void orElseThrowExample() {
        Optional<String> presentValue = Optional.of("Hello, World!");
        Optional<String> emptyValue = Optional.empty();

        // Example with present value
        try {
            String result1 = presentValue.orElseThrow(() -> new IllegalStateException("Value not present"));
            System.out.println("orElseThrow with present value: " + result1); // Prints: Hello, World!
        } catch (IllegalStateException e) {
            System.out.println("Exception occurred: " + e.getMessage());
        }

        // Example with empty value
        try {
            String result2 = emptyValue.orElseThrow(() -> new IllegalStateException("Value not present"));
            System.out.println("This won't be printed");
        } catch (IllegalStateException e) {
            System.out.println("Exception occurred: " + e.getMessage()); // Prints: Exception occurred: Value not present
        }
    }

    /**
     * Utility method to write exception logs to a file.
     */
    public void writeLogsToLogsFile(Exception e) throws FileNotFoundException {
        File file = new File("logs/error.log");
        file.getParentFile().mkdirs(); // Create directories if they don't exist
        
        try (PrintWriter printWriter = new PrintWriter(file)) {
            printWriter.append("Exception occurred: " + e);
        }
    }

    /**
     * Demonstrates sorting a list using Stream API.
     */
    public void sortListUsingStreamApi() {
        List<Customer> customers = dbLayer.getAllCustomer();

        // Sort customers by ID and print
        List<Customer> sortedCustomers = customers.stream()
                .filter(Objects::nonNull)  // Filter out null customers
                .sorted(Comparator.comparing(Customer::getId))
                .collect(Collectors.toList());
        System.out.println("Customers sorted by ID: " + sortedCustomers.size() + " customers");
        
        // Convert list to map
        Map<String, Customer> customersByName = customers.stream()
                .filter(Objects::nonNull)  // Filter out null customers
                .filter(customer -> customer.getName() != null)  // Filter out customers with null names
                .collect(Collectors.toMap(
                        Customer::getName,
                        customer -> customer,
                        (existing, replacement) -> existing // Keep existing in case of duplicate keys
                ));
        System.out.println("Customers by name map size: " + customersByName.size());
        
        // Sort map by key (customer ID)
        Map<Integer, Customer> sortedById = customers.stream()
                .filter(Objects::nonNull)  // Filter out null customers
                .collect(Collectors.toMap(
                        Customer::getId,
                        customer -> customer,
                        (existing, replacement) -> existing  // Handle duplicate keys
                ))
                .entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (o, n) -> o,
                        LinkedHashMap::new // Use LinkedHashMap to maintain insertion order
                ));
        System.out.println("Customers sorted by ID map size: " + sortedById.size());
        
        // Sort map by value (customer name)
        Map<Integer, Customer> sortedByName = customers.stream()
                .filter(Objects::nonNull)  // Filter out null customers
                .filter(customer -> customer.getName() != null)  // Filter out customers with null names
                .collect(Collectors.toMap(
                        Customer::getId,
                        customer -> customer,
                        (existing, replacement) -> existing  // Handle duplicate keys
                ))
                .entrySet().stream()
                .sorted(Map.Entry.comparingByValue(
                        Comparator.comparing(
                            Customer::getName,
                            Comparator.nullsLast(String::compareTo)  // Handle null names
                        )
                ))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (o, n) -> o,
                        LinkedHashMap::new
                ));
        System.out.println("Customers sorted by name map size: " + sortedByName.size());
    }

    /**
     * Demonstrates sorting a list of employees and finding the nth highest salary.
     */
    public void sortingListExampleUsingStreamApi() {
        // Create a list of employees
        List<Employee> employees = Arrays.asList(
                new Employee("John", 50000),
                new Employee("Jane", 60000),
                new Employee("Mary", 55000),
                new Employee("Peter", 70000),
                new Employee("Paul", 65000)
        );

        // Get the 3rd highest employee based on salary
        Optional<Employee> thirdHighestEmployee = employees.stream()
                .sorted(Comparator.comparingDouble(Employee::getSalary).reversed())
                .skip(2)
                .findFirst();

        thirdHighestEmployee.ifPresent(employee ->
                System.out.println("3rd Highest Employee: " + employee));
    }

    /**
     * Demonstrates sorting a map using Stream API.
     */
    public void sortMapUsingStreamApi() {
        // Example with primitive types
        Map<String, Integer> map = new HashMap<>();
        map.put("eight", 8);
        map.put("four", 4);
        map.put("two", 2);
        map.put("six", 6);
        
        // Sort by key
        Map<String, Integer> sortedByKey = map.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (o, n) -> o,
                        LinkedHashMap::new
                ));
        System.out.println("Map sorted by key: " + sortedByKey);
        
        // Sort by value
        Map<String, Integer> sortedByValue = map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (o, n) -> o,
                        LinkedHashMap::new
                ));
        System.out.println("Map sorted by value: " + sortedByValue);
        
        // Example with custom objects
        Map<Employees, Integer> employeeMap = new HashMap<>();
        employeeMap.put(new Employees(176, "Roshan", "A", 60000), 60);
        employeeMap.put(new Employees(177, "Bikash", "B", 90000), 90);
        employeeMap.put(new Employees(180, "Bimal", "E", 50000), 50);
        employeeMap.put(new Employees(178, "Sourav", "C", 40000), 40);
        employeeMap.put(new Employees(179, "Prakash", "D", 70000), 70);
        
        // Sort by employee salary
        Map<Employees, Integer> sortedBySalary = employeeMap.entrySet().stream()
                .sorted(Map.Entry.comparingByKey(Comparator.comparing(Employees::getSalary)))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (o, n) -> o,
                        LinkedHashMap::new
                ));
        System.out.println("Employees sorted by salary: " + sortedBySalary.size() + " entries");
        
        // Sort by employee grade
        Map<Employees, Integer> sortedByGrade = employeeMap.entrySet().stream()
                .sorted(Map.Entry.comparingByKey(Comparator.comparing(Employees::getGrade)))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (o, n) -> o,
                        LinkedHashMap::new
                ));
        System.out.println("Employees sorted by grade: " + sortedByGrade.size() + " entries");
    }

    /**
     * Demonstrates sorting a map using traditional approach.
     */
    public void sortMapUsingTraditionalApproach() {
        Map<Customer, String> customerMap = dbLayer.getAllCustomerAsMap();

        // Convert map entries to a list
        Set<Entry<Customer, String>> entrySet = customerMap.entrySet();
        List<Entry<Customer, String>> entriesList = new ArrayList<>(entrySet);
        
        // Sort by customer ID
        Collections.sort(entriesList, (o1, o2) -> o1.getKey().getId() - o2.getKey().getId());
        System.out.println("Entries sorted by customer ID: " + entriesList.size() + " entries");
        
        // Sort by value (category)
        Collections.sort(entriesList, (o1, o2) -> o1.getValue().compareTo(o2.getValue()));
        System.out.println("Entries sorted by category: " + entriesList.size() + " entries");
        
        // Example with TreeMap and custom comparator
        Map<Employees, Integer> treeMap = new TreeMap<>(
                (o1, o2) -> (int) (o1.getSalary() - o2.getSalary())
        );
        treeMap.put(new Employees(176, "Roshan", "A", 60000), 60);
        treeMap.put(new Employees(177, "Bikash", "B", 90000), 90);
        treeMap.put(new Employees(180, "Bimal", "E", 50000), 50);
        treeMap.put(new Employees(178, "Sourav", "C", 40000), 40);
        treeMap.put(new Employees(179, "Prakash", "D", 70000), 70);
        
        System.out.println("TreeMap sorted by salary: " + treeMap.size() + " entries");
    }

    /**
     * Demonstrates converting a list to a map.
     */
    public void testConvertListToMap() {
        List<Customer> customers = dbLayer.getAllCustomer();
        
        // Convert list to map with name as key and ID as value
        Map<String, Integer> customerMap = customers.stream()
                .collect(Collectors.toMap(
                        Customer::getName,
                        Customer::getId,
                        (existing, replacement) -> existing // Keep existing in case of duplicate keys
                ));
        System.out.println("Customer map size: " + customerMap.size());
        
        // Print entries
        customerMap.forEach((name, id) -> System.out.println(name + ": " + id));
        
        // Convert to list of entries
        List<Map.Entry<String, Integer>> entries = customerMap.entrySet().stream()
                .collect(Collectors.toList());
        System.out.println("Entries list size: " + entries.size());
        
        // Convert list to map with name as key and customer as value
        Map<String, Customer> customersByName = customers.stream()
                .collect(Collectors.toMap(
                        Customer::getName,
                        customer -> customer,
                        (existing, replacement) -> existing
                ));
        System.out.println("Customers by name map size: " + customersByName.size());
    }

    /**
     * Demonstrates converting a list to an array.
     */
    public void testConvertListToArray() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        
        // Convert to int array
        int[] intArray = numbers.stream()
                .mapToInt(Integer::intValue)
                .toArray();
        System.out.println("Int array length: " + intArray.length);
        
        // Convert to Integer array
        Integer[] integerArray = numbers.toArray(Integer[]::new);
        System.out.println("Integer array length: " + integerArray.length);
        
        // Convert list of customers to array
        List<Customer> customers = dbLayer.getAllCustomer();
        Customer[] customerArray = customers.toArray(Customer[]::new);
        System.out.println("Customer array length: " + customerArray.length);
    }

    /**
     * Demonstrates converting a map to a list.
     */
    public void testConvertMapToList() {
        // Create a map with lists as values
        Map<Integer, List<String>> mapWithListValues = new HashMap<>();
        
        List<String> names1 = new ArrayList<>();
        names1.add("Jon");
        names1.add("Johnson");
        
        List<String> names2 = new ArrayList<>();
        names2.add("Peter");
        names2.add("Malone");
        
        mapWithListValues.put(1, names1);
        mapWithListValues.put(2, names2);
        
        // Get all values (lists) as a collection
        Collection<List<String>> allLists = mapWithListValues.values();
        System.out.println("Number of lists: " + allLists.size());
        
        // Flatten all lists into a single list
        List<String> allNames = mapWithListValues.values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
        System.out.println("All names: " + allNames);
        
        // Create a map with lists as keys
        Map<List<String>, Integer> mapWithListKeys = new HashMap<>();
        mapWithListKeys.put(names1, 1);
        mapWithListKeys.put(names2, 2);
        
        // Get all keys as a set
        Set<List<String>> keyLists = mapWithListKeys.keySet();
        System.out.println("Number of key lists: " + keyLists.size());
        
        // Flatten all key lists into a single list
        List<String> allKeyNames = mapWithListKeys.keySet().stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
        System.out.println("All key names: " + allKeyNames);
    }
    
    /**
     * Employee class for demonstration purposes.
     */
    public static class Employee {
        private final String name;
        private final double salary;

        public Employee(String name, double salary) {
            this.name = name;
            this.salary = salary;
        }

        public String getName() {
            return name;
        }

        public double getSalary() {
            return salary;
        }

        @Override
        public String toString() {
            return "Employee{name='" + name + "', salary=" + salary + "}";
        }
    }
}
