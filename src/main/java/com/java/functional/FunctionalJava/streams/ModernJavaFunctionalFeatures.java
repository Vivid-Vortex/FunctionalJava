package com.java.functional.FunctionalJava.streams;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class demonstrates modern Java functional programming features
 * from recent Java versions (Java 9 through Java 21)
 * 
 * Each method showcases specific features introduced in different Java versions,
 * highlighting the evolution of functional programming capabilities in the language.
 * Methods are organized by the Java version in which each feature was introduced.
 */
public class ModernJavaFunctionalFeatures {

    private final DbLayer dbLayer;

    public ModernJavaFunctionalFeatures(DbLayer dbLayer) {
        this.dbLayer = dbLayer;
    }

    //==========================================================================
    // Java 9 Features (Released September 2017)
    //==========================================================================

    /**
     * Java 9: Collection Factory Methods - List.of()
     * 
     * Prior to Java 9, creating immutable collections required multiple steps:
     * 1. Create a mutable collection
     * 2. Add elements
     * 3. Wrap with Collections.unmodifiableList()
     * 
     * With Java 9, we can create immutable collections in a single step.
     * The resulting list is:
     * - Immutable (throws UnsupportedOperationException if modified)
     * - Null-hostile (throws NullPointerException if null elements are provided)
     * - Value-based (not identity-sensitive)
     * - Serializable
     */
    public List<String> getImmutableList() {
        return List.of("Java", "Kotlin", "Scala", "Groovy");
    }

    /**
     * Java 9: Collection Factory Methods - Map.of()
     * 
     * Similar to List.of(), Map.of() creates immutable maps in a single step.
     * Limited to 10 key-value pairs (use Map.ofEntries() for more).
     * 
     * The resulting map is:
     * - Immutable (throws UnsupportedOperationException if modified)
     * - Null-hostile (throws NullPointerException if null keys/values are provided)
     * - Value-based (not identity-sensitive)
     * - Serializable
     * - Maintains insertion order when iterated (unlike HashMap)
     */
    public Map<String, Integer> getImmutableMap() {
        return Map.of(
                "Java", 1995,
                "Kotlin", 2011,
                "Scala", 2004,
                "Groovy", 2003
        );
    }

    /**
     * Java 9: Stream improvements - takeWhile()
     * 
     * The takeWhile() operation takes elements from the stream while the predicate is true,
     * and stops once the predicate becomes false for an element.
     * 
     * This is different from filter() which processes the entire stream and only keeps
     * elements that match the predicate.
     * 
     * In this example:
     * 1. We create an infinite stream starting from 1 and incrementing by 10
     * 2. We take elements while they're <= 100
     * 3. This gives us [1, 11, 21, 31, 41, 51, 61, 71, 81, 91]
     */
    public List<Integer> getNumbersUntilGreaterThan100() {
        return Stream.iterate(1, n -> n + 10)
                .takeWhile(n -> n <= 100)
                .collect(Collectors.toList());
    }

    /**
     * Java 9: Stream improvements - dropWhile()
     * 
     * The dropWhile() operation is the complement of takeWhile().
     * It drops elements from the stream while the predicate is true,
     * and starts including elements once the predicate becomes false.
     * 
     * In this example:
     * 1. We create an infinite stream starting from 1 and incrementing by 10
     * 2. We drop elements while they're < 50
     * 3. We limit to 5 elements (otherwise it would be infinite)
     * 4. This gives us [51, 61, 71, 81, 91]
     */
    public List<Integer> getNumbersAfterReaching50() {
        return Stream.iterate(1, n -> n + 10)
                .dropWhile(n -> n < 50)
                .limit(5)
                .collect(Collectors.toList());
    }

    /**
     * Java 9: Optional improvements - orElseThrow() without parameters
     * 
     * Prior to Java 9, orElseThrow() required a supplier of exception.
     * Java 9 added a no-arg version that throws NoSuchElementException.
     * 
     * This example uses the parameterized version for demonstration,
     * throwing a custom RuntimeException with a message.
     * 
     * This is useful for cases where an Optional is expected to have a value,
     * and its absence indicates an error condition.
     */
    public String getDefaultIfEmpty(Optional<String> optional) {
        return optional.orElseThrow(() -> new RuntimeException("Value not present"));
    }

    /**
     * Java 9: Optional improvements - stream()
     * 
     * The stream() method converts an Optional into a Stream with either:
     * - Zero elements (if the Optional is empty)
     * - One element (if the Optional has a value)
     * 
     * This is particularly useful when working with streams of Optionals,
     * as it allows flatMapping them into a stream of present values.
     * 
     * Example use case: List<Optional<String>> -> List<String> (non-null values only)
     */
    public Stream<String> getOptionalAsStream(Optional<String> optional) {
        return optional.stream();
    }

    //==========================================================================
    // Java 10 Features (Released March 2018)
    //==========================================================================

    /**
     * Java 10: Local variable type inference with 'var'
     * 
     * The 'var' keyword allows the compiler to infer the type of local variables
     * from their initializer, reducing verbosity while maintaining type safety.
     * 
     * Benefits:
     * - Reduces boilerplate for complex generic types
     * - Improves readability when the type is obvious from context
     * - Makes variable declarations more consistent in length
     * 
     * Limitations:
     * - Only works for local variables, not fields, method parameters, or return types
     * - Requires initialization at declaration
     * - Cannot be used with diamond operator in anonymous classes
     */
    public List<String> processWithVar() {
        var customers = dbLayer.getAllCustomer();  // Instead of List<Customer> customers
        var filteredCustomers = customers.stream()
                .filter(c -> c.getEmail() != null)
                .collect(Collectors.toList());  // Instead of List<Customer> filteredCustomers
        
        return filteredCustomers.stream()
                .map(c -> c.getName() + ": " + c.getEmail())
                .collect(Collectors.toList());
    }

    //==========================================================================
    // Java 11 Features (Released September 2018)
    //==========================================================================

    /**
     * Java 11: String methods and Predicate.not()
     * 
     * Java 11 added several useful String methods:
     * - isBlank(): Checks if string is empty or contains only whitespace
     * - strip(), stripLeading(), stripTrailing(): Unicode-aware trim
     * - lines(): Stream of lines from a multi-line string
     * - repeat(n): Repeats the string n times
     * 
     * Java 11 also added Predicate.not() for negating predicates more clearly.
     * Before: .filter(s -> !s.isBlank())
     * After:  .filter(Predicate.not(String::isBlank))
     * 
     * This example:
     * 1. Filters out blank strings using Predicate.not()
     * 2. Trims whitespace using strip()
     */
    public List<String> processStrings(List<String> strings) {
        return strings.stream()
                .filter(Predicate.not(String::isBlank))  // Instead of s -> !s.isBlank()
                .map(String::strip)  // Unicode-aware trim
                .collect(Collectors.toList());
    }

    /**
     * Java 11: Collection to array improvements
     * 
     * Prior to Java 11, converting a collection to an array required:
     * - Creating an empty array of the correct size
     * - Passing it to toArray()
     * 
     * Java 11 added a new toArray method that accepts a constructor reference:
     * - No need to create an empty array
     * - More concise and readable
     * - Avoids array size miscalculations
     * 
     * Before: list.toArray(new String[list.size()])
     * After:  list.toArray(String[]::new)
     */
    public String[] convertToArray(List<String> list) {
        return list.toArray(String[]::new);  // Instead of new String[list.size()]
    }

    //==========================================================================
    // Java 12 Features (Released March 2019)
    //==========================================================================

    /**
     * Java 12: Collectors.teeing()
     * 
     * The teeing collector allows combining two independent collectors
     * and then merging their results with a BiFunction.
     * 
     * This is useful when you need to perform two different aggregations
     * on the same stream and combine their results.
     * 
     * In this example:
     * 1. We find the employee with minimum salary
     * 2. We find the employee with maximum salary
     * 3. We calculate the average salary
     * 4. We combine these three values into a SalaryStats record
     * 
     * Without teeing, we would need multiple passes over the stream.
     */
    public record SalaryStats(double min, double max, double average) {}

    public SalaryStats getSalaryStats() {
        return dbLayer.getEmployees().stream()
                .collect(Collectors.teeing(
                        // First collector: find minimum salary
                        Collectors.minBy(java.util.Comparator.comparing(e -> e.getSalary())),
                        // Second collector: find maximum salary
                        Collectors.maxBy(java.util.Comparator.comparing(e -> e.getSalary())),
                        // Merge function: combine min, max, and calculate average
                        (min, max) -> {
                            double avgSalary = dbLayer.getEmployees().stream()
                                    .mapToDouble(e -> e.getSalary())
                                    .average()
                                    .orElse(0.0);
                            return new SalaryStats(
                                    min.map(e -> e.getSalary()).orElse(0.0),
                                    max.map(e -> e.getSalary()).orElse(0.0),
                                    avgSalary
                            );
                        }
                ));
    }

    //==========================================================================
    // Java 14 Features (Released March 2020)
    //==========================================================================

    /**
     * Java 14: Switch expressions
     * 
     * Java 14 enhanced switch to be an expression that returns a value,
     * with several improvements:
     * 
     * 1. Arrow syntax (->): More concise than traditional case/break
     * 2. Multiple case labels: case "A", "B" -> ...
     * 3. No fall-through: Each case handles one execution path
     * 4. Expression form: Can be assigned to a variable
     * 5. Exhaustiveness checking: Must cover all possible values or have default
     * 
     * This makes switch more functional and less error-prone.
     */
    public String getGradeDescription(String grade) {
        return switch (grade) {
            case "A" -> "Excellent";       // Arrow syntax, no break needed
            case "B" -> "Good";
            case "C" -> "Average";
            case "D" -> "Below Average";
            case "F" -> "Failing";
            default -> "Unknown Grade";    // Required for exhaustiveness
        };
    }

    //==========================================================================
    // Java 15 Features (Released September 2020)
    //==========================================================================

    /**
     * Java 15: Text blocks
     * 
     * Text blocks provide a way to define multi-line string literals
     * with improved readability and less escaping.
     * 
     * Benefits:
     * 1. Preserves formatting without escape sequences
     * 2. No need for string concatenation or + operators
     * 3. No need to escape quotes within the text
     * 4. Can be combined with String.formatted() for dynamic content
     * 
     * This example creates a formatted employee report using text blocks
     * and the formatted() method to insert dynamic values.
     */
    public String getEmployeeReport() {
        var employees = dbLayer.getEmployees();
        var sb = new StringBuilder();
        
        sb.append("""
                Employee Report
                ===============
                Total Employees: %d
                Average Salary: $%.2f
                
                Employee Details:
                """.formatted(  // String.formatted() for dynamic values
                        employees.size(),
                        employees.stream().mapToDouble(e -> e.getSalary()).average().orElse(0.0)
                ));
        
        employees.forEach(emp -> 
            sb.append("""
                    ID: %d, Name: %s, Grade: %s, Salary: $%.2f
                    """.formatted(emp.getId(), emp.getName(), emp.getGrade(), emp.getSalary()))
        );
        
        return sb.toString();
    }

    //==========================================================================
    // Java 16 Features (Released March 2021)
    //==========================================================================

    /**
     * Java 16: Stream.toList() shorthand
     * 
     * Java 16 added a toList() method to Stream as a shorthand for
     * collect(Collectors.toList()).
     * 
     * Benefits:
     * 1. More concise code
     * 2. Improved readability
     * 3. Less nesting of method calls
     * 
     * The returned list is unmodifiable, unlike collect(Collectors.toList())
     * which returns a modifiable ArrayList.
     */
    public List<String> getEmployeeNames() {
        return dbLayer.getEmployees().stream()
                .map(e -> e.getName())
                .toList();  // Instead of collect(Collectors.toList())
    }

    /**
     * Java 16: Pattern matching for instanceof
     * 
     * Pattern matching for instanceof combines type checking and casting
     * into a single operation, making code more concise and safer.
     * 
     * Before:
     *   if (obj instanceof String) {
     *       String s = (String) obj;
     *       // use s
     *   }
     * 
     * After:
     *   if (obj instanceof String s) {
     *       // use s directly
     *   }
     * 
     * Benefits:
     * 1. Eliminates explicit casting
     * 2. Reduces boilerplate
     * 3. Prevents errors from mismatched types
     * 4. The pattern variable is scoped to the if block
     */
    public String processObject(Object obj) {
        if (obj instanceof String s) {  // Pattern variable 's' is bound if obj is a String
            return "String of length " + s.length();
        } else if (obj instanceof Integer i) {  // Pattern variable 'i' is bound if obj is an Integer
            return "Integer with value " + i;
        } else if (obj instanceof List<?> list) {  // Works with generics too
            return "List with " + list.size() + " elements";
        } else {
            return "Unknown object type";
        }
    }

    //==========================================================================
    // Java 17 Features (Released September 2021)
    //==========================================================================

    /**
     * Java 17: Sealed classes and pattern matching for switch
     * 
     * Sealed classes restrict which other classes can extend or implement them,
     * providing a form of sum types from functional programming.
     * 
     * Benefits:
     * 1. Defines a closed set of subtypes
     * 2. Enables exhaustiveness checking in switch expressions
     * 3. Makes class hierarchies more maintainable
     * 4. Supports algebraic data types (ADTs)
     * 
     * This example uses pattern matching in switch to handle different
     * subtypes of the sealed Person interface.
     */
    public String describePerson(Person person) {
        return switch (person) {
            // Pattern matching in switch - extracts fields from records
            case Employee e -> "Employee with salary $" + e.salary();
            case Manager m -> "Manager with " + m.directReports() + " direct reports";
            case Contractor c -> "Contractor with daily rate $" + c.dailyRate();
            // No default needed - compiler knows these are all possible cases
        };
    }

    //==========================================================================
    // Java 19-21 Features (Released September 2022 - September 2023)
    //==========================================================================

    /**
     * Java 19-21: Virtual threads for concurrent operations
     * 
     * Virtual threads (Project Loom) are lightweight threads that don't
     * map 1:1 to OS threads, allowing for millions of concurrent threads.
     * 
     * Benefits:
     * 1. Dramatically increased concurrency
     * 2. Simplified concurrent programming model
     * 3. Reduced memory footprint
     * 4. Better CPU utilization
     * 
     * This example uses CompletableFuture which can leverage virtual threads
     * when running on Java 19+ with virtual threads enabled.
     */
    public CompletableFuture<List<String>> processDataConcurrently(List<String> data) {
        return CompletableFuture.supplyAsync(() -> 
            data.stream()
                .parallel()
                .map(String::toUpperCase)
                .toList()
        );
    }

    /**
     * Java 21: SequencedCollection interface
     * 
     * Java 21 introduced the SequencedCollection interface and related interfaces
     * (SequencedSet, SequencedMap) that add methods for working with collections
     * that have a defined encounter order.
     * 
     * New methods include:
     * - getFirst()/getLast(): Get first/last element
     * - addFirst()/addLast(): Add at beginning/end
     * - reversed(): Get a view with reversed order
     * 
     * This example uses getFirst() and getLast() to access the first and last
     * customers in the list.
     * 
     * Note: This will throw an exception on Java versions < 21.
     */
    public String getFirstAndLastCustomer() {
        var customers = dbLayer.getAllCustomer();
        var first = customers.getFirst().getName();  // Instead of get(0)
        var last = customers.getLast().getName();    // Instead of get(size()-1)
        return "First customer: " + first + ", Last customer: " + last;
    }

    /**
     * Java 21: Record patterns
     * 
     * Record patterns extend pattern matching to allow destructuring records
     * in instanceof expressions and switch cases.
     * 
     * Benefits:
     * 1. Directly access record components without getter methods
     * 2. Nested pattern matching for complex data structures
     * 3. More declarative code style
     * 
     * This example checks if an object is a Point record and extracts
     * its x and y coordinates in a single step.
     * 
     * Note: This will throw an exception on Java versions < 21.
     */
    public String processPoint(Object obj) {
        if (obj instanceof Point(int x, int y)) {  // Destructures Point into x and y components
            return "Point at coordinates (%d, %d)".formatted(x, y);
        }
        return "Not a point";
    }
}

/**
 * Java 17: Sealed class hierarchy
 * 
 * A sealed interface with three permitted implementations as records.
 * The 'sealed' keyword restricts which classes can implement this interface.
 * The 'permits' clause explicitly lists allowed implementations.
 * 
 * Each permitted class must use one of:
 * - final: Cannot be extended further
 * - sealed: Restricted subclasses
 * - non-sealed: Can be freely extended
 * 
 * Records are implicitly final, so they satisfy this requirement.
 */
sealed interface Person permits Employee, Manager, Contractor {}
record Employee(String name, double salary) implements Person {}
record Manager(String name, double salary, int directReports) implements Person {}
record Contractor(String name, double dailyRate) implements Person {}

/**
 * Java 16: Record for pattern matching
 * 
 * Records provide a compact syntax for classes that are transparent holders
 * for shallowly immutable data.
 * 
 * Benefits:
 * 1. Automatic getters (component accessors)
 * 2. Automatic equals(), hashCode(), and toString()
 * 3. Automatic constructor
 * 4. Immutable by default
 * 
 * This record is used for demonstrating record patterns in Java 21.
 */
record Point(int x, int y) {} 