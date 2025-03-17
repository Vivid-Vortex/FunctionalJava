SuperSet of this project:
https://github.com/Deepak-Kumar-githb/Java-8-plus

# Functional Java

This project demonstrates functional programming features in Java, from Java 8 through the latest versions (up to Java 21).

## Features

### Java 8 Functional Features (Java8FunctionalService)
- Function and Predicate interfaces
- Stream API (map, filter, reduce, collect)
- Optional for null handling
- Method references
- Lambda expressions
- Collectors (groupingBy, partitioningBy, joining)
- CompletableFuture for async operations

### Modern Java Functional Features (ModernJavaFunctionalFeatures)

#### Java 9
- Collection factory methods (List.of, Map.of)
- Stream improvements (takeWhile, dropWhile)
- Optional improvements (stream(), ifPresentOrElse)

#### Java 10
- Local variable type inference (var)

#### Java 11
- String methods (isBlank, strip)
- Collection to array improvements (toArray with constructor reference)
- Predicate.not

#### Java 12
- Collectors.teeing for combining two collectors

#### Java 14
- Switch expressions

#### Java 15
- Text blocks for multiline strings

#### Java 16
- Stream.toList() shorthand
- Pattern matching for instanceof

#### Java 17
- Sealed classes and interfaces

#### Java 19-21
- Virtual threads for concurrent operations
- SequencedCollection methods (getFirst, getLast)
- Record patterns

## Running the Demo

To run the Java 8 features demo:
```
java com.java.functional.FunctionalJava.streams.Java8FunctionalDemo
```

To run the modern Java features demo:
```
java com.java.functional.FunctionalJava.streams.ModernJavaFeaturesDemo
```

Note: Some features require Java 21 to run. The code includes fallbacks for earlier Java versions.

## Testing

The project includes comprehensive unit tests for all features:
- Java8FunctionalServiceTest
- ModernJavaFunctionalFeaturesTest

Run the tests to verify the functionality of all features.

