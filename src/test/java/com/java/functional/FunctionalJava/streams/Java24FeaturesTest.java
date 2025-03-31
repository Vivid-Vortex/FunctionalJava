package com.java.functional.FunctionalJava.streams;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for Java24Features class
 * Aims to cover 100% of the lines in the class
 * 
 * This test class verifies both PREVIEW and FINAL features of Java 24.
 */
class Java24FeaturesTest {

    private Java24Features java24Features;
    private ByteArrayOutputStream outputCaptor;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        java24Features = new Java24Features();
        
        // Setup output capturing
        outputCaptor = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputCaptor));
    }

    @AfterEach
    void tearDown() {
        // Restore original System.out
        System.setOut(originalOut);
    }

    @Test
    void testStringTemplates() {
        // Test with normal input
        String result = java24Features.stringTemplates("John", 30);
        assertEquals("Hello John, you are 30 years old!", result);
        
        // Test with empty name
        result = java24Features.stringTemplates("", 0);
        assertEquals("Hello , you are 0 years old!", result);
        
        // Test with negative age
        result = java24Features.stringTemplates("Alice", -5);
        assertEquals("Hello Alice, you are -5 years old!", result);
    }

    @Test
    void testEnhancedStreamAPI() {
        // Test with regular list
        List<Integer> numbers = List.of(1, 2, 3, 4, 5);
        Map<String, Object> results = java24Features.enhancedStreamAPI(numbers);
        
        assertEquals(15, results.get("sum"));
        assertEquals(3.0, results.get("average"));
        assertEquals(5, results.get("max"));
        
        @SuppressWarnings("unchecked")
        Map<String, List<Integer>> grouped = (Map<String, List<Integer>>) results.get("grouped");
        assertEquals(List.of(1, 3, 5), grouped.get("odd"));
        assertEquals(List.of(2, 4), grouped.get("even"));
        
        // Test with empty list
        results = java24Features.enhancedStreamAPI(Collections.emptyList());
        assertEquals(0, results.get("sum"));
        assertEquals(0.0, results.get("average"));
        assertEquals(0, results.get("max"));
        
        @SuppressWarnings("unchecked")
        Map<String, List<Integer>> emptyGrouped = (Map<String, List<Integer>>) results.get("grouped");
        assertTrue(emptyGrouped.isEmpty());
        
        // Test with negative numbers
        List<Integer> negativeNumbers = List.of(-3, -2, -1);
        results = java24Features.enhancedStreamAPI(negativeNumbers);
        assertEquals(-6, results.get("sum"));
        assertEquals(-2.0, results.get("average"));
        assertEquals(-1, results.get("max"));
    }

    @Test
    void testScopedValues() {
        java24Features.scopedValues();
        String output = outputCaptor.toString();
        assertTrue(output.contains("Scoped values demonstration"));
        assertTrue(output.contains("allows thread inheritance of immutable values"));
        assertTrue(output.contains("(FINAL feature)"));
    }

    @Test
    void testStructuredConcurrency() {
        java24Features.structuredConcurrency();
        String output = outputCaptor.toString();
        assertTrue(output.contains("This is a simulation of structured concurrency"));
        assertTrue(output.contains("(PREVIEW feature)"));
    }

    @Test
    void testEnhancedVirtualThreads() {
        // For this test, we'll verify the reduced number of threads implementation
        java24Features.enhancedVirtualThreads();
        
        // Check that output contains some of the expected text
        String output = outputCaptor.toString();
        assertTrue(output.contains("Task "), "Should contain task identifier");
        assertTrue(output.contains("completed on"), "Should indicate thread completion");
        assertTrue(output.contains("(FINAL feature)"), "Should indicate it's a final feature");
        
        // Check that we don't have more than 10 threads (as specified in the implementation)
        long threadCount = output.lines()
                .filter(line -> line.contains("Task "))
                .count();
        assertTrue(threadCount <= 10, "Should not exceed 10 threads as specified in implementation");
    }

    @ParameterizedTest
    @MethodSource("provideObjectsForPatternMatching")
    void testPatternMatchingForSwitch(Object input, String expected) {
        String result = java24Features.patternMatchingForSwitch(input);
        assertEquals(expected, result);
    }

    static Stream<Arguments> provideObjectsForPatternMatching() {
        return Stream.of(
            Arguments.of("Hello, World!", "Long string: Hello, World!"),
            Arguments.of("Hello", "String: Hello"),
            Arguments.of(42, "Positive number: 42"),
            Arguments.of(-7, "Negative number: -7"),
            Arguments.of(0, "Zero"),
            Arguments.of(List.of(1, 2, 3), "List with 3 elements"),
            Arguments.of(List.of(), "Empty list"),
            Arguments.of(Optional.empty(), "Something else: Optional"),
            Arguments.of(null, "Null value")
        );
    }

    @Test
    void testClassFileApi() {
        java24Features.classFileApi();
        String output = outputCaptor.toString();
        
        assertTrue(output.contains("Class-File API demonstration"));
        assertTrue(output.contains("(PREVIEW feature)"));
        assertTrue(output.contains("Method:"));
        assertTrue(output.contains("Return type:"));
        assertTrue(output.contains("Parameter count:"));
    }

    @Test
    void testForeignFunctionAndMemoryApi() {
        java24Features.foreignFunctionAndMemoryApi();
        String output = outputCaptor.toString();
        
        assertTrue(output.contains("Foreign Function & Memory API demonstration"));
        assertTrue(output.contains("(FINAL feature)"));
        assertTrue(output.contains("Text: Hello"));
        assertTrue(output.contains("Length: 5"));
    }
    
    @Test
    void testUnnamedClassesAndInstanceMainMethods() {
        java24Features.unnamedClassesAndInstanceMainMethods();
        String output = outputCaptor.toString();
        
        assertTrue(output.contains("Unnamed Classes and Instance Main Methods demonstration"));
        assertTrue(output.contains("(PREVIEW feature)"));
        assertTrue(output.contains("simpler code structure"));
        assertTrue(output.contains("instance main methods"));
    }
    
    @Test
    void testVectorApi() {
        java24Features.vectorApi();
        String output = outputCaptor.toString();
        
        assertTrue(output.contains("Vector API demonstration"));
        assertTrue(output.contains("(PREVIEW feature)"));
        assertTrue(output.contains("SIMD"));
        assertTrue(output.contains("Result of simulated vector operation"));
        assertTrue(output.contains("a[5] = 50.0"), "Should show the result of the computation a[5] * b[5] = 5 * 10 = 50");
    }

    @Test
    void testMain() {
        // Reset output capturer
        outputCaptor.reset();
        
        // Call the main method
        Java24Features.main(new String[]{});
        
        // Check the output contains all expected sections with feature status (PREVIEW/FINAL)
        String output = outputCaptor.toString();
        
        assertTrue(output.contains("=== Java 24 Features Demonstration ==="));
        assertTrue(output.contains("1. String Templates (PREVIEW)"));
        assertTrue(output.contains("2. Enhanced Stream API with Gatherers (PREVIEW)"));
        assertTrue(output.contains("3. Scoped Values (FINAL)"));
        assertTrue(output.contains("4. Structured Concurrency (PREVIEW)"));
        assertTrue(output.contains("5. Pattern Matching for switch (PREVIEW)"));
        assertTrue(output.contains("6. Class-File API (PREVIEW)"));
        assertTrue(output.contains("7. Foreign Function & Memory API (FINAL)"));
        assertTrue(output.contains("8. Unnamed Classes and Instance Main Methods (PREVIEW)"));
        assertTrue(output.contains("9. Vector API (PREVIEW)"));
        assertTrue(output.contains("10. Enhanced Virtual Threads (FINAL)"));
        
        // Ensure all the pattern matching cases appear in the output
        assertTrue(output.contains("Long string: Hello, World!"));
        assertTrue(output.contains("Positive number: 42"));
        assertTrue(output.contains("Negative number: -7"));
        assertTrue(output.contains("List with 3 elements"));
        assertTrue(output.contains("Empty list"));
        assertTrue(output.contains("Something else: Optional"));
        
        // Verify Vector API output
        assertTrue(output.contains("Result of simulated vector operation"));
        
        // Verify that all features are properly labeled as PREVIEW or FINAL
        int previewCount = countOccurrences(output, "(PREVIEW feature)");
        int finalCount = countOccurrences(output, "(FINAL feature)");
        assertTrue(previewCount >= 6, "Should have at least 6 preview feature demonstrations");
        assertTrue(finalCount >= 3, "Should have at least 3 final feature demonstrations");
    }
    
    private int countOccurrences(String text, String substring) {
        int count = 0;
        int index = 0;
        while ((index = text.indexOf(substring, index)) != -1) {
            count++;
            index += substring.length();
        }
        return count;
    }
} 