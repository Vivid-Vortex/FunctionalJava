package com.java.functional.FunctionalJava.streams;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * This class demonstrates the new features introduced in Java 24 (JDK 24),
 * released in March 2024.
 * 
 * Key features demonstrated include:
 * 1. Foreign Function & Memory API (FINAL)
 * 2. String Templates (PREVIEW)
 * 3. Unnamed Classes and Instance Main Methods (PREVIEW)
 * 4. Scoped Values (FINAL)
 * 5. Structured Concurrency (PREVIEW)
 * 6. Vector API (PREVIEW)
 * 7. Enhanced Virtual Threads (FINAL)
 * 8. Pattern Matching for switch (PREVIEW)
 * 9. Enhanced Stream API with Gatherers (PREVIEW)
 * 10. Class-File API (PREVIEW)
 * 
 * Each feature is demonstrated with commented examples and explanation
 * of its syntax and potential use cases.
 * 
 * PREVIEW features require the following compiler flags:
 * javac --release 24 --enable-preview YourClass.java
 */
public class Java24Features {

    /**
     * Demonstrates String Templates (PREVIEW Feature in JDK 24)
     * String templates provide a more powerful way to format strings
     * with embedded expressions.
     * 
     * To use this feature, you must compile with:
     * javac --release 24 --enable-preview YourClass.java
     * 
     * @param name Person's name
     * @param age Person's age
     * @return Formatted greeting
     */
    public String stringTemplates(String name, int age) {
        // This code uses the preview feature of string templates
        // In actual code with --enable-preview, you'd write:
        // String greeting = STR."Hello \{name}, you are \{age} years old!";
        
        // For compatibility, we use the standard String.format here
        return String.format("Hello %s, you are %d years old!", name, age);
    }

    /**
     * Demonstrates enhancements to the Stream API with Gatherers in Java 24 (PREVIEW)
     * - New gatherers API for composable reductions
     * 
     * @param numbers List of integers to process
     * @return Different aggregations of the numbers
     */
    public Map<String, Object> enhancedStreamAPI(List<Integer> numbers) {
        Map<String, Object> results = new ConcurrentHashMap<>();
        
        // Stream.gatherer would be used here in actual Java 24 code with preview enabled
        // For compatibility, we use regular stream operations
        
        results.put("sum", numbers.stream().mapToInt(Integer::intValue).sum());
        results.put("average", numbers.stream().mapToInt(Integer::intValue).average().orElse(0));
        results.put("max", numbers.stream().max(Integer::compare).orElse(0));
        results.put("grouped", numbers.stream()
                .collect(Collectors.groupingBy(n -> n % 2 == 0 ? "even" : "odd")));
        
        return results;
    }

    /**
     * Demonstrates Scoped Values (FINAL in JDK 24)
     * ScopedValue provides a way to share immutable data within and across threads
     * without using ThreadLocal or explicit parameter passing.
     * 
     * This feature is fully finalized and available in Java 24 without any special flags.
     */
    public void scopedValues() {
        // In Java 24, you would write:
        // ScopedValue<String> CONTEXT = ScopedValue.newInstance();
        // 
        // ScopedValue.where(CONTEXT, "Main Thread Context").run(() -> {
        //     String value = CONTEXT.get();
        //     System.out.println("Context value: " + value);
        //     
        //     // Child threads inherit the scoped value
        //     CompletableFuture.runAsync(() -> {
        //         String inheritedValue = CONTEXT.get();
        //         System.out.println("Inherited context: " + inheritedValue);
        //     });
        // });
        
        System.out.println("Scoped values demonstration (FINAL feature): allows thread inheritance of immutable values");
    }

    /**
     * Demonstrates Structured Concurrency (PREVIEW in JDK 24)
     * This feature simplifies multithreaded programming by ensuring
     * that tasks executed in a thread pool are bounded by the lifetime
     * of a controlling task.
     * 
     * To use this feature, you must compile with:
     * javac --release 24 --enable-preview YourClass.java
     */
    public void structuredConcurrency() {
        // In Java 24 with preview enabled, you could write:
        // try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
        //     Future<String> user = scope.fork(() -> fetchUser(userId));
        //     Future<List<Order>> orders = scope.fork(() -> fetchOrders(userId));
        //     
        //     scope.join();         // Join both subtasks
        //     scope.throwIfFailed(); // Propagate errors
        //     
        //     // Both subtasks completed successfully
        //     processResults(user.resultNow(), orders.resultNow());
        // }
        
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
                return "This is a simulation of structured concurrency (PREVIEW feature)";
            }, executor);
            
            System.out.println(future.join());
        }
    }

    /**
     * Demonstrates Enhanced Virtual Threads in Java 24 (FINAL)
     * Virtual threads, introduced in Java 21, were enhanced in Java 24 with
     * improved debugging, profiling, and monitoring capabilities.
     * 
     * This feature is fully finalized and available in Java 24 without any special flags.
     */
    public void enhancedVirtualThreads() {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<CompletableFuture<Void>> futures = new ArrayList<>();
            
            // Reduced number of threads for testing purposes
            for (int i = 0; i < 10; i++) {
                final int taskId = i;
                futures.add(CompletableFuture.runAsync(() -> {
                    try {
                        Thread.sleep(Duration.ofMillis(10));
                        System.out.println("Task " + taskId + " completed on " + Thread.currentThread() + " (FINAL feature)");
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }, executor));
            }
            
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        }
    }

    /**
     * Demonstrates Pattern Matching for switch (PREVIEW in JDK 24)
     * Building on the pattern matching for instanceof (Java 16) and 
     * record patterns (Java 21), this feature further enhances 
     * pattern matching capabilities.
     * 
     * To use this feature, you must compile with:
     * javac --release 24 --enable-preview YourClass.java
     * 
     * @param obj Object to be matched
     * @return Description of the object based on its type and properties
     */
    public String patternMatchingForSwitch(Object obj) {
        // In Java 24 with preview enabled, you could write:
        // return switch(obj) {
        //     case String s when s.length() > 10 -> "Long string: " + s;
        //     case String s -> "String: " + s;
        //     case Integer i when i > 0 -> "Positive number: " + i;
        //     case Integer i when i < 0 -> "Negative number: " + i;
        //     case Integer i -> "Zero";
        //     case List<?> l when l.isEmpty() -> "Empty list";
        //     case List<?> l -> "List with " + l.size() + " elements";
        //     case null -> "Null value";
        //     default -> "Something else: " + obj.getClass().getSimpleName();
        // };
        
        // For compatibility, we use if-else statements
        if (obj == null) {
            return "Null value";
        } else if (obj instanceof String s) {
            if (s.length() > 10) {
                return "Long string: " + s;
            } else {
                return "String: " + s;
            }
        } else if (obj instanceof Integer i) {
            if (i > 0) {
                return "Positive number: " + i;
            } else if (i < 0) {
                return "Negative number: " + i;
            } else {
                return "Zero";
            }
        } else if (obj instanceof List<?> l) {
            if (l.isEmpty()) {
                return "Empty list";
            } else {
                return "List with " + l.size() + " elements";
            }
        } else {
            return "Something else: " + obj.getClass().getSimpleName();
        }
    }

    /**
     * Demonstrates the Class-File API (PREVIEW in JDK 24)
     * This API provides tools for parsing, generating, and transforming
     * Java class files.
     * 
     * To use this feature, you must compile with:
     * javac --release 24 --enable-preview YourClass.java
     */
    public void classFileApi() {
        // In Java 24 with preview enabled, you might write:
        // ClassModel classModel = ClassFile.of().parse(getClass().getResourceAsStream("MyClass.class"));
        // 
        // classModel.methods().forEach(method -> {
        //     System.out.println("Method: " + method.name().stringValue());
        //     method.code().ifPresent(code -> {
        //         code.forEach(insn -> {
        //             System.out.println("  " + insn);
        //         });
        //     });
        // });
        
        // For compatibility, we use reflection
        System.out.println("Class-File API demonstration (PREVIEW feature) with reflection fallback:");
        Method[] methods = this.getClass().getDeclaredMethods();
        for (Method method : methods) {
            System.out.println("Method: " + method.getName());
            System.out.println("  Return type: " + method.getReturnType().getSimpleName());
            System.out.println("  Parameter count: " + method.getParameterCount());
        }
    }

    /**
     * Demonstrates the Foreign Function & Memory API (FINAL in JDK 24)
     * This API provides a way to interoperate with code outside the JVM,
     * such as native C libraries.
     * 
     * This feature is fully finalized and available in Java 24 without any special flags.
     */
    public void foreignFunctionAndMemoryApi() {
        // In actual Java 24 code, you might write:
        // try {
        //     // Get access to the C standard library
        //     SymbolLookup stdlib = SymbolLookup.loaderLookup();
        //     MethodHandle strlen = linker.downcallHandle(
        //         stdlib.lookup("strlen").orElseThrow(),
        //         FunctionDescriptor.of(ValueLayout.JAVA_LONG, ValueLayout.ADDRESS)
        //     );
        //     
        //     // Create a string off-heap using the try-with-resources pattern
        //     try (Arena arena = Arena.ofConfined()) {
        //         MemorySegment cString = arena.allocateUtf8String("Hello");
        //         long len = (long) strlen.invoke(cString);
        //         System.out.println("Length: " + len);
        //     }
        // } catch (Throwable t) {
        //     t.printStackTrace();
        // }
        
        System.out.println("Foreign Function & Memory API demonstration (FINAL feature):");
        String text = "Hello";
        System.out.println("Text: " + text);
        System.out.println("Length: " + text.length());
    }

    /**
     * Demonstrates the Unnamed Classes and Instance Main Methods (PREVIEW in JDK 24)
     * This feature allows for simpler code structure with unnamed classes
     * and instance main methods, similar to scripting languages.
     * 
     * To use this feature, you must compile with:
     * javac --release 24 --enable-preview YourClass.java
     * 
     * Example in Java 24 with preview enabled:
     * 
     * // UnnamedExample.java
     * void main() {
     *     System.out.println("Hello from an instance main method!");
     *     int result = calculateSomething();
     *     System.out.println("Result: " + result);
     * }
     * 
     * int calculateSomething() {
     *     return 42;
     * }
     */
    public void unnamedClassesAndInstanceMainMethods() {
        System.out.println("Unnamed Classes and Instance Main Methods demonstration (PREVIEW feature):");
        System.out.println("This feature allows for simpler code structure with unnamed classes");
        System.out.println("and instance main methods, similar to scripting languages.");
    }

    /**
     * Demonstrates the Vector API (PREVIEW in JDK 24)
     * The Vector API enables developers to express vector computations 
     * that compile at runtime to optimal vector hardware instructions.
     * 
     * To use this feature, you must compile with:
     * javac --release 24 --enable-preview YourClass.java
     */
    public void vectorApi() {
        // In Java 24 with preview enabled, you might write:
        // float[] a = new float[1024];
        // float[] b = new float[1024];
        // // Initialize arrays
        // VectorSpecies<Float> species = FloatVector.SPECIES_PREFERRED;
        // 
        // for (int i = 0; i < a.length; i += species.length()) {
        //     VectorMask<Float> mask = species.indexInRange(i, a.length);
        //     FloatVector va = FloatVector.fromArray(species, a, i, mask);
        //     FloatVector vb = FloatVector.fromArray(species, b, i, mask);
        //     FloatVector vc = va.mul(vb);
        //     vc.intoArray(a, i, mask);
        // }
        
        System.out.println("Vector API demonstration (PREVIEW feature):");
        System.out.println("This API enables efficient SIMD (Single Instruction, Multiple Data) operations");
        System.out.println("for better performance in data-parallel computations.");
        
        // Simulate vector operation with regular code
        float[] a = new float[10];
        float[] b = new float[10];
        
        // Initialize arrays
        for (int i = 0; i < a.length; i++) {
            a[i] = i;
            b[i] = i * 2;
        }
        
        // Perform element-wise multiplication (what Vector API would optimize)
        for (int i = 0; i < a.length; i++) {
            a[i] = a[i] * b[i];
        }
        
        System.out.println("Result of simulated vector operation: a[5] = " + a[5]);
    }

    /**
     * Main method to demonstrate each Java 24 feature
     * 
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        Java24Features demo = new Java24Features();
        
        System.out.println("=== Java 24 Features Demonstration ===\n");
        
        System.out.println("1. String Templates (PREVIEW)");
        System.out.println(demo.stringTemplates("Alice", 28));
        System.out.println();
        
        System.out.println("2. Enhanced Stream API with Gatherers (PREVIEW)");
        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        System.out.println(demo.enhancedStreamAPI(numbers));
        System.out.println();
        
        System.out.println("3. Scoped Values (FINAL)");
        demo.scopedValues();
        System.out.println();
        
        System.out.println("4. Structured Concurrency (PREVIEW)");
        demo.structuredConcurrency();
        System.out.println();
        
        System.out.println("5. Pattern Matching for switch (PREVIEW)");
        System.out.println(demo.patternMatchingForSwitch("Hello, World!"));
        System.out.println(demo.patternMatchingForSwitch(42));
        System.out.println(demo.patternMatchingForSwitch(-7));
        System.out.println(demo.patternMatchingForSwitch(List.of(1, 2, 3)));
        System.out.println(demo.patternMatchingForSwitch(List.of()));
        System.out.println(demo.patternMatchingForSwitch(Optional.empty()));
        System.out.println();
        
        System.out.println("6. Class-File API (PREVIEW)");
        demo.classFileApi();
        System.out.println();
        
        System.out.println("7. Foreign Function & Memory API (FINAL)");
        demo.foreignFunctionAndMemoryApi();
        System.out.println();
        
        System.out.println("8. Unnamed Classes and Instance Main Methods (PREVIEW)");
        demo.unnamedClassesAndInstanceMainMethods();
        System.out.println();
        
        System.out.println("9. Vector API (PREVIEW)");
        demo.vectorApi();
        System.out.println();
        
        System.out.println("10. Enhanced Virtual Threads (FINAL)");
        // Using a smaller number of threads for the demonstration
        demo.enhancedVirtualThreads();
        System.out.println();
    }
} 