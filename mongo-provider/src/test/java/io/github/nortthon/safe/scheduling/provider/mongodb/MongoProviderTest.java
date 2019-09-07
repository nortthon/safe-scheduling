package io.github.nortthon.safe.scheduling.provider.mongodb;

import io.github.nortthon.safe.scheduling.Provider;
import io.github.nortthon.safe.scheduling.SchedulerConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Slf4j
@DataMongoTest
@SpringBootConfiguration
@EnableAutoConfiguration
@RunWith(SpringRunner.class)
public class MongoProviderTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    private Provider provider;

    @Before
    public void setup() {
        this.provider = new MongoProvider(mongoTemplate);
    }

    @Test
    public void testLockingAndRunningTaskScheduled() {
        final SchedulerConfig config = SchedulerConfig.builder().name("task1").lockedFor(5000L).build();

        log.info(config.toString());

        final AtomicBoolean runned = new AtomicBoolean(false);

        provider.execute(() -> runned.set(true), config);

        SchedulerControl schedulers = mongoTemplate.findOne(query(where("_id").is("task1")), SchedulerControl.class);
        log.info(schedulers.toString());
        log.info("{}", new Date().toInstant().toEpochMilli());
        assertNotNull(schedulers);
        assertTrue(schedulers.getLockedAt() <= new Date().toInstant().toEpochMilli());
        assertTrue(schedulers.getLockedUntil() > new Date().toInstant().toEpochMilli());
        assertTrue(runned.get());
    }

    @Test
    public void testLockedTask() {
        final SchedulerControl data = new SchedulerControl();
        data.setId("task2");
        data.setLockedAt(new Date().toInstant().toEpochMilli());
        data.setLockedUntil(new Date().toInstant().plusMillis(5000L).toEpochMilli());
        mongoTemplate.insert(data);

        final SchedulerConfig config = SchedulerConfig.builder().name(data.getId()).lockedFor(5000L).build();

        final AtomicBoolean runned = new AtomicBoolean(false);

        provider.execute(() -> runned.set(true), config);

        assertFalse(runned.get());
    }

    @After
    public void after() {
        mongoTemplate.dropCollection(SchedulerControl.class);
    }
}
