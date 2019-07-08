# Safe Scheduling

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Build Status](https://travis-ci.org/nortthon/safe-scheduling.svg?branch=master)](https://travis-ci.org/nortthon/safe-scheduling)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=io.github.nortthon:safe-scheduling&metric=alert_status)](https://sonarcloud.io/dashboard?id=io.github.nortthon:safe-scheduling)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=io.github.nortthon:safe-scheduling&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=io.github.nortthon:safe-scheduling)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=io.github.nortthon:safe-scheduling&metric=coverage)](https://sonarcloud.io/dashboard?id=io.github.nortthon:safe-scheduling)

## What is this?
it's a simple way to use the native **Spring Framework Scheduling** into your spring-boot application keeping the tasks locked during the executions process avoiding multiples executions.

## TaskScheduler Bean
```java
@Configuration
@EnableScheduling
public class SchedulerConfig {
    
    @Bean
    public TaskScheduler taskScheduler(final Provider provider) {
        final LockableTaskScheduler taskScheduler = new LockableTaskScheduler(provider);
        taskScheduler.setPoolSize(1); // Default == 1
        return taskScheduler;
    }
    
    //Provider Bean
}
```

## Use like this

```java
@Component
public class Class {
    @SafeScheduled(name = "scheduler-name", lockedFor = 4000L, cron = "*/5 * * * * *")
    public void execute() {
        //TODO
    }
}
```

## MongoDB Provider
##### Dependences
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-mongodb</artifactId>
</dependency>
<dependency>
    <groupId>io.github.nortthon</groupId>
    <artifactId>safe-scheduling-provider-mongodb</artifactId>
    <version>0.2.0</version>
</dependency>
```

##### Provider Bean
```java
@Bean
public Provider provider(final MongoTemplate mongoTemplate) {
    new MongoProvider(mongoTemplate);
}
```

## Redis Provider
##### Dependences
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
<dependency>
    <groupId>io.github.nortthon</groupId>
    <artifactId>safe-scheduling-provider-redis</artifactId>
    <version>0.2.0</version>
</dependency>
```

##### Provider Bean
```java
@Bean
public Provider provider(final StringRedisTemplate redisTemplate) {
    new RedisProvider(redisTemplate);
}
```
