Perfect example to understand pipeline/chaining concept brought in by Stream API in Java 8
---
###### **Note**: Below example uses Reactive programming concepts which in turn also uses same concept of stream processing through pipeline or chaining mechanism as non reactive or other stream api based frameworks or libraries. Despite of this, below example is a perfect example to understand pipeline concept and is applicable to any type whether reactive or non-reactive.

---
### **Question:**
I have a microservice that consumes events from a Kafka consumer and then performs validation, mapping, and persistence. I want to separate these layers using Reactor Core (`Mono` and `Flux`) instead of Reactor Bus. How can I do so? Please give a step-by-step detailed explanation along with the Spring Boot Gradle libraries involved.

---
### **Answer:**

To achieve a layered design for your microservice using Reactor Core (`Mono` and `Flux`), you can follow these steps:

---

### **Step 1: Add Required Dependencies**
For a Kafka-based reactive application using Reactor Core and Spring Boot, add the following dependencies to your `build.gradle`:

```gradle
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-webflux' // Reactive programming with Spring WebFlux
    implementation 'org.springframework.kafka:spring-kafka'               // Kafka integration
    implementation 'io.projectreactor.kafka:reactor-kafka:1.3.17'         // Reactor Kafka
    implementation 'org.springframework.boot:spring-boot-starter-data-r2dbc' // For reactive database access
    implementation 'io.r2dbc:r2dbc-postgresql' // Reactive PostgreSQL Driver (replace based on DB)
    implementation 'org.springframework.boot:spring-boot-starter-validation' // For validation
}
```

---

### **Step 2: Define the Kafka Consumer**
Set up a Kafka consumer to receive events and expose it as a **Reactive Flux**. Use `Reactor Kafka` for integrating with Kafka reactively.

#### Kafka Configuration (application.yml):
```yaml
spring:
  kafka:
    bootstrap-servers: localhost:9092
    consumer:
      group-id: microservice-group
      auto-offset-reset: earliest
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.apache.kafka.common.serialization.StringDeserializer
```

#### Kafka Receiver Setup:
```java
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;

import java.util.Collections;

@Component
public class KafkaEventConsumer {

    private final KafkaReceiver<String, String> kafkaReceiver;

    public KafkaEventConsumer() {
        ReceiverOptions<String, String> options = ReceiverOptions.<String, String>create(
                Collections.singletonMap("bootstrap.servers", "localhost:9092")
        ).subscription(Collections.singleton("your-topic"));

        this.kafkaReceiver = KafkaReceiver.create(options);
    }

    public Flux<String> consumeEvents() {
        return kafkaReceiver.receive()
                .map(record -> record.value());
    }
}
```

---

### **Step 3: Define the Service Layers**
Each step in the pipeline (validation, mapping, persistence) can be encapsulated in separate service layers. Use `Mono` and `Flux` for chaining these operations.

#### Validation Layer:
```java
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class EventValidator {

    public Mono<String> validate(String event) {
        // Perform validation logic
        if (event == null || event.isEmpty()) {
            return Mono.error(new IllegalArgumentException("Invalid Event"));
        }
        return Mono.just(event);
    }
}
```

#### Mapping Layer:
```java
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class EventMapper {

    public Mono<MyEntity> mapToEntity(String event) {
        // Convert the event string to an entity
        MyEntity entity = new MyEntity();
        entity.setData(event);
        return Mono.just(entity);
    }
}
```

#### Persistence Layer:
```java
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class EventPersistence {

    @Autowired
    private MyEntityRepository repository; // Reactive repository

    public Mono<MyEntity> persist(MyEntity entity) {
        return repository.save(entity);
    }
}
```

---

### **Step 4: Combine the Layers in a Reactive Pipeline**
Create a coordinator class that consumes events from Kafka, processes them through the layers, and handles the results.

```java
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class EventProcessor {

    private final KafkaEventConsumer kafkaConsumer;
    private final EventValidator validator;
    private final EventMapper mapper;
    private final EventPersistence persistence;

    public EventProcessor(KafkaEventConsumer kafkaConsumer, EventValidator validator, EventMapper mapper, EventPersistence persistence) {
        this.kafkaConsumer = kafkaConsumer;
        this.validator = validator;
        this.mapper = mapper;
        this.persistence = persistence;
    }

    public Flux<MyEntity> processEvents() {
        return kafkaConsumer.consumeEvents()
                .flatMap(validator::validate) // Validate the event
                .flatMap(mapper::mapToEntity) // Map to entity
                .flatMap(persistence::persist) // Persist to database
                .onErrorContinue((e, o) -> System.err.println("Error processing event: " + o + ", Error: " + e));
    }
}
```

---

### **Step 5: Reactive Database Setup**
Set up a reactive database repository using Spring Data R2DBC. Ensure your database schema matches the entity.

#### Entity Class:
```java
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("my_entity")
public class MyEntity {
    @Id
    private Long id;
    private String data;

    // Getters and setters
}
```

#### Repository:
```java
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface MyEntityRepository extends ReactiveCrudRepository<MyEntity, Long> {}
```

---

### **Step 6: Launch the Reactive Pipeline**
In your `@SpringBootApplication` class or a separate service, start the pipeline:

```java
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AppStartupRunner implements CommandLineRunner {

    private final EventProcessor processor;

    public AppStartupRunner(EventProcessor processor) {
        this.processor = processor;
    }

    @Override
    public void run(String... args) {
        processor.processEvents()
                 .subscribe(entity -> System.out.println("Processed and saved entity: " + entity));
    }
}
```

---

### **Advantages of This Approach**
1. **Separation of Concerns**:
    - Each layer (validation, mapping, persistence) is encapsulated in its own service.
2. **Reactive Design**:
    - End-to-end reactive pipeline from Kafka consumption to database persistence.
3. **Error Handling**:
    - The `onErrorContinue` operator ensures the pipeline doesn't stop for individual errors.
4. **Scalability**:
    - Flux-based design ensures non-blocking processing, even under high throughput.

---

### **Summary**
By replacing Reactor Bus with **Project Reactor's Flux and Mono**, you can create a flexible, non-blocking event processing pipeline with clean separation of concerns. This approach leverages modern reactive programming paradigms and integrates well with the Spring ecosystem.

If you observe from step 1 till Step 4, that we haven't connected all the three layers say validattion, mapping and persistence with each other, or in other words we are not calling these methods in one layer say from validation to the method in another layer say mapping which is unlike to the traditional way of implementing.
In [Step 4](#step-4-combine-the-layers-in-a-reactive-pipeline), we are first calling validator, then output of it passing to mapper and then output of mapper passing to persistence. This is the beauty of pipeline or chaining mechanism (whether reactive/non-reactive), where we are chaining the operations and the output of one operation is input to another operation and then output of this ops go to another and so on.

_This approach reduces coupling between the layers and make the code more modular and testable._

```java
public Flux<MyEntity> processEvents() {
        return kafkaConsumer.consumeEvents()
                .flatMap(validator::validate) // Validate the event
                .flatMap(mapper::mapToEntity) // Map to entity
                .flatMap(persistence::persist) // Persist to database
                .onErrorContinue((e, o) -> System.err.println("Error processing event: " + o + ", Error: " + e));
    }
```