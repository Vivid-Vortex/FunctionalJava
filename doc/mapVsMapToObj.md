In Java, particularly when working with the Stream API, there are different methods to transform elements within a stream. The methods `.map()` and `.mapToObj()` are used to transform elements, but they have different use cases and apply to different types of streams. Here’s a detailed comparison:

### `.map()`

- **Usage**: `.map()` is a method available in `Stream<T>` where `T` is any object type. It is used to transform elements of a stream into another form.
- **Type**: Applies to object streams (`Stream<T>`).
- **Returns**: A `Stream<R>` where `R` is the result of the transformation function.
- **Example**: Converting a list of integers to their string representations.

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
List<String> strings = numbers.stream()
    .map(String::valueOf)
    .collect(Collectors.toList());
```

### `.mapToObj()`

- **Usage**: `.mapToObj()` is specifically for primitive streams like `IntStream`, `LongStream`, and `DoubleStream`. It is used to convert a primitive stream to a stream of objects.
- **Type**: Applies to primitive streams (`IntStream`, `LongStream`, `DoubleStream`).
- **Returns**: A `Stream<U>` where `U` is the result of the transformation function.
- **Example**: Converting an `IntStream` to a `Stream<String>`.

```java
IntStream intStream = IntStream.range(1, 6);
Stream<String> stringStream = intStream
    .mapToObj(String::valueOf)
    .collect(Collectors.toList());
```

### Differences and Use Cases

- **Stream Types**: `.map()` is used on `Stream<T>` (object streams), while `.mapToObj()` is used on primitive streams like `IntStream`, `LongStream`, and `DoubleStream`.
- **Purpose**: `.map()` transforms elements within an object stream. `.mapToObj()` converts elements of a primitive stream to an object stream.
- **Return Types**: `.map()` returns `Stream<R>`, while `.mapToObj()` always returns `Stream<U>`.

### When to Use Which

- **Use `.map()`** when you are working with an object stream and you need to transform each element to another object or the same type.
- **Use `.mapToObj()`** when you are working with a primitive stream and you need to convert the primitive values into objects. This is often necessary because primitive streams (e.g., `IntStream`) do not support certain operations that object streams do, and sometimes you need to work with object types rather than primitive types.

### Examples

#### `.map()` Example

Transforming a list of strings to their lengths:

```java
List<String> words = Arrays.asList("apple", "banana", "cherry");
List<Integer> lengths = words.stream()
    .map(String::length)
    .collect(Collectors.toList());
```

#### `.mapToObj()` Example

Converting a range of integers to their string representations:

```java
IntStream intStream = IntStream.range(1, 4); // [1, 2, 3]
List<String> stringList = intStream
    .mapToObj(Integer::toString)
    .collect(Collectors.toList()); // ["1", "2", "3"]
```

### Conclusion

- Use `.map()` for transforming elements in an object stream.
- Use `.mapToObj()` for converting primitive streams into object streams.
- Choose the appropriate method based on the type of stream you are working with and the transformation required.
-------------------------------------------------------------------------------------------------------------------------------------------------
// mapToObj vs mapToDouble etc. what's the diffenrece and when to use what

In Java's Stream API, methods like `.mapToObj()`, `.mapToDouble()`, `.mapToInt()`, and `.mapToLong()` are used to transform elements within streams and convert them between different types of streams. Here's a detailed comparison of these methods, including their differences and when to use each:

### `.mapToObj()`
- **Usage**: Converts elements of a primitive stream (`IntStream`, `LongStream`, `DoubleStream`) to an object stream (`Stream<U>`).
- **Return Type**: `Stream<U>`.
- **Purpose**: Use this method when you want to transform primitive values into objects.
- **Example**: Converting an `IntStream` to a `Stream<String>`.

```java
IntStream intStream = IntStream.range(1, 4); // [1, 2, 3]
Stream<String> stringStream = intStream.mapToObj(Integer::toString);
```

### `.mapToDouble()`
- **Usage**: Converts elements of an object stream or a primitive stream to a `DoubleStream`.
- **Return Type**: `DoubleStream`.
- **Purpose**: Use this method when you need to convert stream elements to `double` values, especially useful for performing numerical computations or aggregations.
- **Example**: Converting a `Stream<String>` to a `DoubleStream` by parsing the strings as doubles.

```java
Stream<String> stringStream = Stream.of("1.1", "2.2", "3.3");
DoubleStream doubleStream = stringStream.mapToDouble(Double::parseDouble);
```

### `.mapToInt()`
- **Usage**: Converts elements of an object stream or a primitive stream to an `IntStream`.
- **Return Type**: `IntStream`.
- **Purpose**: Use this method when you need to convert stream elements to `int` values, which is useful for integer-based computations.
- **Example**: Converting a `Stream<String>` to an `IntStream` by parsing the strings as integers.

```java
Stream<String> stringStream = Stream.of("1", "2", "3");
IntStream intStream = stringStream.mapToInt(Integer::parseInt);
```

### `.mapToLong()`
- **Usage**: Converts elements of an object stream or a primitive stream to a `LongStream`.
- **Return Type**: `LongStream`.
- **Purpose**: Use this method when you need to convert stream elements to `long` values, which is useful for long integer-based computations.
- **Example**: Converting a `Stream<String>` to a `LongStream` by parsing the strings as longs.

```java
Stream<String> stringStream = Stream.of("10000000000", "20000000000", "30000000000");
LongStream longStream = stringStream.mapToLong(Long::parseLong);
```

### Differences and When to Use

1. **Type of Stream**:
    - `.mapToObj()`: Converts primitive stream to object stream.
    - `.mapToDouble()`: Converts to a `DoubleStream`.
    - `.mapToInt()`: Converts to an `IntStream`.
    - `.mapToLong()`: Converts to a `LongStream`.

2. **Return Type**:
    - `.mapToObj()`: Returns `Stream<U>`.
    - `.mapToDouble()`: Returns `DoubleStream`.
    - `.mapToInt()`: Returns `IntStream`.
    - `.mapToLong()`: Returns `LongStream`.

3. **Purpose**:
    - Use `.mapToObj()` when you need to convert primitive values to objects.
    - Use `.mapToDouble()`, `.mapToInt()`, and `.mapToLong()` when you need to convert elements to their respective primitive types for numerical processing.
      For example, First if  you want to convert any primitive type given in string to it's actual primitive type so as to do the mathematical operation
      second, if you want to convert one primitive type to another such as int to double, double to float etc.

### Summary

- **Use `.mapToObj()`**: When you are working with a primitive stream and need to convert elements to objects.
- **Use `.mapToDouble()`, `.mapToInt()`, `.mapToLong()`**: When you need to perform numerical operations on elements and require a primitive stream (`DoubleStream`, `IntStream`, or `LongStream`).
- In short, first if you want to convert any primitive type given in string to it's actual primitive type so as to do the mathematical operation second, if you want to convert one primitive type to another such as int to double, double to float etc.

Understanding these methods helps in effectively transforming and processing streams in Java, making your code more efficient and easier to read.