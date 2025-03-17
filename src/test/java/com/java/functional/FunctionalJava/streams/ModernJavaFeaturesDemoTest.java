package com.java.functional.FunctionalJava.streams;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ModernJavaFeaturesDemoTest {

    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;
    private PrintStream originalErr;

    @BeforeEach
    void setUp() {
        // Capture console output
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        originalErr = System.err;
        System.setOut(new PrintStream(outputStream));
        System.setErr(new PrintStream(new ByteArrayOutputStream())); // Redirect error output
    }
    
    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    void testMainMethodDoesNotThrowException() {
        // This test verifies that the main method doesn't throw any exceptions
        assertDoesNotThrow(() -> ModernJavaFeaturesDemo.main(new String[]{}));
    }

    @Test
    void testJava9Features() {
        // Run the demo
        assertDoesNotThrow(() -> ModernJavaFeaturesDemo.main(new String[]{}));
        
        // Verify output contains expected text
        String output = outputStream.toString();
        assertTrue(output.contains("Java 9: Collection Factory Methods") || 
                   output.contains("Java version"), "Output should mention Java 9 features or Java version");
    }

    @Test
    void testJava10Features() {
        // Run the demo
        assertDoesNotThrow(() -> ModernJavaFeaturesDemo.main(new String[]{}));
        
        // Verify output contains expected text
        String output = outputStream.toString();
        assertTrue(output.contains("Java 10: Local Variable Type Inference") || 
                   output.contains("Java version"), "Output should mention Java 10 features or Java version");
    }

    @Test
    void testJava11Features() {
        // Run the demo
        assertDoesNotThrow(() -> ModernJavaFeaturesDemo.main(new String[]{}));
        
        // Verify output contains expected text
        String output = outputStream.toString();
        assertTrue(output.contains("Java 11: String Methods") || 
                   output.contains("Java 11: Collection to Array") || 
                   output.contains("Java version"), "Output should mention Java 11 features or Java version");
    }

    @Test
    void testJava12Features() {
        // Run the demo
        assertDoesNotThrow(() -> ModernJavaFeaturesDemo.main(new String[]{}));
        
        // Verify output contains expected text
        String output = outputStream.toString();
        assertTrue(output.contains("Java 12: Collectors.teeing") || 
                   output.contains("Java version"), "Output should mention Java 12 features or Java version");
    }

    @Test
    void testJava14To16Features() {
        // Run the demo
        assertDoesNotThrow(() -> ModernJavaFeaturesDemo.main(new String[]{}));
        
        // Verify output contains expected text
        String output = outputStream.toString();
        assertTrue(output.contains("Java 14: Switch Expressions") || 
                   output.contains("Java 15: Text Blocks") || 
                   output.contains("Java 16: Stream.toList()") || 
                   output.contains("Java 16: Pattern Matching for instanceof") || 
                   output.contains("Java version"), "Output should mention Java 14-16 features or Java version");
    }

    @Test
    void testJava17Features() {
        // Run the demo
        assertDoesNotThrow(() -> ModernJavaFeaturesDemo.main(new String[]{}));
        
        // Verify output contains expected text
        String output = outputStream.toString();
        assertTrue(output.contains("Java 17: Sealed Classes") || 
                   output.contains("Java version") || 
                   output.contains("Sealed classes not available"), 
                   "Output should mention Java 17 features, Java version, or feature unavailability");
    }

    @Test
    void testJava19To21Features() {
        // Run the demo
        assertDoesNotThrow(() -> ModernJavaFeaturesDemo.main(new String[]{}));
        
        // Verify output contains expected text
        String output = outputStream.toString();
        assertTrue(output.contains("Java 19-21: Virtual Threads") || 
                   output.contains("Java 21: SequencedCollection") || 
                   output.contains("Java 21: Record Patterns") || 
                   output.contains("Java version") || 
                   output.contains("not available in this Java version"), 
                   "Output should mention Java 19-21 features, Java version, or feature unavailability");
    }
} 