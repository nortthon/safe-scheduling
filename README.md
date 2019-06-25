# Safe Scheduling

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Build Status](https://travis-ci.org/nortthon/safe-scheduling.svg?branch=master)](https://travis-ci.org/nortthon/safe-scheduling)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=nortthon_safe-scheduling&metric=alert_status)](https://sonarcloud.io/dashboard?id=nortthon_safe-scheduling)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=nortthon_safe-scheduling&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=nortthon_safe-scheduling)


### What is this?
it's a simple way to use the native **Spring Framework Scheduling** into your spring-boot application keeping the tasks locked during the executions process avoiding multiples executions.

### MongoDB Provider
#### 1 Dependences
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

#### 2 TaskScheduler Bean
```java
@Bean
public TaskScheduler taskScheduler(MongoTemplate mongoTemplate) {
    final Provider provider = new MongoProvider(mongoTemplate);
    final LockableTaskScheduler taskScheduler = new LockableTaskScheduler(provider);
    taskScheduler.setPoolSize(1); // Default == 1
    return taskScheduler;
}
```

#### 3 Use like this

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
