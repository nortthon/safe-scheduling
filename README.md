# Safe Scheduling
### What is this?
it's a simple way to use the native **Spring Framework Scheduling** into your spring-boot application keeping the tasks locked during the executions process avoiding multiples executions.

### MongoDB Provider
#### Dependences
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-mongodb</artifactId>
</dependency>
<dependency>
    <groupId>io.github.nortthon</groupId>
    <artifactId>safe-scheduling-provider-mongodb</artifactId>
    <version>1.0.0</version>
</dependency>
```

#### TaskScheduler Bean
```java
@Bean
public TaskScheduler taskScheduler(MongoTemplate mongoTemplate) {
    final Provider provider = new MongoProvider(mongoTemplate);
    final LockableTaskScheduler taskScheduler = new LockableTaskScheduler(provider);
    taskScheduler.setPoolSize(1); // Default == 1
    return taskScheduler;
}
```

#### Use like this

```java
@Component
@EnableScheduling
private JavaClass {
    @SafeScheduled(name = "print-message", lockedFor = 1000L, cron = "*/2 * * * * *")
    public void execute() {
        log.info("Message");
    }
}
```