package com.java.functional.FunctionalJava.streams;

import com.java.functional.FunctionalJava.dto.Customer;
import com.java.functional.FunctionalJava.dto.Employees;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;


//@Component
public class DbLayer {

    // Enhanced sample data with more variety
    public List<Customer> getAllCustomer() {
        List<Customer> custList = new ArrayList<>();

        custList.add(new Customer(1, "Aman", "aman@gmail.com", Arrays.asList("923334444", "923334445")));
        custList.add(new Customer(2, "Bman", "bman@gmail.com", Arrays.asList("923334446", "923334449")));
        custList.add(new Customer(3, "Chaman", "chaman@gmail.com", Arrays.asList("923334447", "923334845")));
        custList.add(new Customer(4, "Gaman", "gaman@gmail.com", Arrays.asList("923334448", "923334495")));
        custList.add(new Customer(5, "Gagan", "gagan@gmail.com", Arrays.asList("923334449", "923334495")));
        custList.add(new Customer(6, "Karan", "karan@gmail.com", Arrays.asList("923334450", "923334451")));
        custList.add(new Customer(7, "Lakshya", "lakshya@gmail.com", Arrays.asList("923334452", "923334453")));
        custList.add(new Customer(8, "Mohan", "mohan@gmail.com", Arrays.asList("923334454", "923334455")));
        custList.add(new Customer(9, "Nikhil", null, Arrays.asList("923334456", "923334457"))); // Null email for testing
        custList.add(new Customer(10, "Pankaj", "pankaj@gmail.com", Arrays.asList("923334458")));

        return custList;
    }

    // Example of using Map with custom objects as keys
    public Map<Customer, String> getAllCustomerAsMap() {
        Map<Customer, String> custMap = new HashMap<>();

        custMap.put(new Customer(1, "Aman", "aman@gmail.com", Arrays.asList("923334444", "923334445")), "Premium");
        custMap.put(new Customer(2, "Bman", "bman@gmail.com", Arrays.asList("923334446", "923334449")), "Standard");
        custMap.put(new Customer(3, "Chaman", "chaman@gmail.com", Arrays.asList("923334447", "923334845")), "Premium");
        custMap.put(new Customer(4, "Gaman", "gaman@gmail.com", Arrays.asList("923334448", "923334495")), "Standard");
        custMap.put(new Customer(5, "Gagan", "gagan@gmail.com", Arrays.asList("923334449", "923334495")), "Basic");
        custMap.put(new Customer(6, "Karan", "karan@gmail.com", Arrays.asList("923334450", "923334451")), "Premium");
        custMap.put(new Customer(7, "Lakshya", "lakshya@gmail.com", Arrays.asList("923334452", "923334453")), "Basic");

        return custMap;
    }

    // Enhanced employee data with more variety in grades and salaries
    public List<Employees> getEmployees() {
        List<Employees> empList = new ArrayList<>();

        empList.add(new Employees(1, "Aman", "A", 60000));
        empList.add(new Employees(2, "Bman", "B", 30000));
        empList.add(new Employees(3, "Chaman", "A", 80000));
        empList.add(new Employees(4, "Gaman", "A", 90000));
        empList.add(new Employees(5, "Gagan", "C", 15000));
        empList.add(new Employees(6, "Karan", "B", 40000));
        empList.add(new Employees(7, "Lakshya", "A", 75000));
        empList.add(new Employees(8, "Mohan", "C", 20000));
        empList.add(new Employees(9, "Nikhil", "B", 35000));
        empList.add(new Employees(10, "Pankaj", "A", 95000));

        return empList;
    }

    // Example of Stream.of() with advanced stream operations
    public List<Customer> getCustStrema() {
        return Stream.of(
                new Customer(1, "Aman", "aman@gmail.com", Arrays.asList("923334444", "923334445")),
                new Customer(2, "Bman", "bman@gmail.com", Arrays.asList("923334446", "923334449")),
                new Customer(3, "Chaman", "chaman@gmail.com", Arrays.asList("923334447", "923334845")),
                new Customer(4, "Gaman", "gaman@gmail.com", Arrays.asList("923334448", "923334495")),
                new Customer(5, "Gagan", "gagan@gmail.com", Arrays.asList("923334449", "923334495")),
                new Customer(6, "Karan", "karan@gmail.com", Arrays.asList("923334450", "923334451"))
        ).collect(Collectors.toList());
    }

    // Example of returning a Stream directly
    public Stream<Customer> getCustStrem() {
        return Stream.of(
                new Customer(1, "Aman", "aman@gmail.com", Arrays.asList("923334444", "923334445")),
                new Customer(2, "Bman", "bman@gmail.com", Arrays.asList("923334446", "923334449")),
                new Customer(3, "Chaman", "chaman@gmail.com", Arrays.asList("923334447", "923334845")),
                new Customer(4, "Gaman", "gaman@gmail.com", Arrays.asList("923334448", "923334495")),
                new Customer(5, "Gagan", "gagan@gmail.com", Arrays.asList("923334449", "923334495"))
        );
    }

    // Example for Optional and null handling
    public Customer getNullEmaillCustomer() {
        return new Customer(101, "John", null, Arrays.asList("923334444", "923334445"));
    }

    public Customer getCustomer() {
        return new Customer(101, "John", "john@gmail.com", Arrays.asList("923334444", "923334445"));
    }

    // Example of using Optional with exception handling
    public Optional<Customer> getCustomerByEmail(String email) throws Exception {
        List<Customer> allCustomers = this.getAllCustomer();

        return allCustomers.stream()
                .filter(customer -> customer.getEmail() != null && customer.getEmail().equals(email))
                .findAny();
    }

    // Example of using TreeMap with custom comparator
    public Map<Employees, Integer> getTreeMap() {
        Map<Employees, Integer> treeMap = new TreeMap<>(Comparator.comparing(Employees::getSalary));
        treeMap.put(new Employees(176, "Roshan", "A", 60000), 60);
        treeMap.put(new Employees(177, "Bikash", "B", 90000), 90);
        treeMap.put(new Employees(180, "Bimal", "E", 50000), 50);
        treeMap.put(new Employees(178, "Sourav", "C", 40000), 40);
        treeMap.put(new Employees(179, "Prakash", "D", 70000), 70);
        return treeMap;
    }
}