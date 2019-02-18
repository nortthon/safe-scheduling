package io.github.nortthon.example.config;

import io.github.nortthon.safe.scheduling.LockableTaskScheduler;
import io.github.nortthon.safe.scheduling.Provider;
import io.github.nortthon.safe.scheduling.provider.mongodb.MongoProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class TaskSchedulerConfig {
    @Bean
    public TaskScheduler taskScheduler(Provider provider) {
        LockableTaskScheduler taskScheduler = new LockableTaskScheduler(provider);
        taskScheduler.setPoolSize(1);
        return taskScheduler;
    }

    @Bean
    public Provider provider(MongoTemplate mongoTemplate) {
        return new MongoProvider(mongoTemplate);
    }
}
