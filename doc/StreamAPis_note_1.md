Yes, you can use `Comparator.comparing` instead of `Comparator.comparingDouble` in this case. However, there are a few things to consider:

1. `Comparator.comparing` works with objects, while `Comparator.comparingDouble` is specifically for primitive `double` values.

2. If `Employee::getSalary` returns a primitive `double`, you'll need to use a method reference that returns a `Double` object instead.

Here's how you could modify the code to use `Comparator.comparing`:

```java
public void sortingListExampleUsingStreamApi() {
    List<Employee> employees = Arrays.asList(
        new Employee("John", 50000),
        new Employee("Jane", 60000),
        new Employee("Mary", 55000),
        new Employee("Peter", 70000),
        new Employee("Paul", 65000)
    );

    // Get the 3rd highest employee based on salary
    Optional<Employee> thirdHighestEmployee = employees.stream()
        .sorted(Comparator.comparing(Employee::getSalary, Comparator.reverseOrder()))
        .skip(2)
        .findFirst();

    thirdHighestEmployee.ifPresent(employee -> 
        System.out.println("3rd Highest Employee: " + employee));
}
```

The key changes are:

1. We've replaced `Comparator.comparingDouble(Employee::getSalary).reversed()` with `Comparator.comparing(Employee::getSalary, Comparator.reverseOrder())`.

2. `Comparator.comparing` expects the `Employee::getSalary` method to return a `Comparable` type (like `Double`), not a primitive `double`.

3. We use `Comparator.reverseOrder()` to sort in descending order, which is equivalent to calling `reversed()` on the comparator.

This approach will work if `Employee::getSalary` returns a `Double`. If it returns a primitive `double`, you would need to modify the `Employee` class to return `Double` instead, or use a lambda to box the primitive:

```java
.sorted(Comparator.comparing(e -> Double.valueOf(e.getSalary()), Comparator.reverseOrder()))
```

Both `Comparator.comparing` and `Comparator.comparingDouble` will achieve the same result in this case. The choice between them often comes down to whether you're working with primitive types or their object wrappers, and personal or team coding preferences.