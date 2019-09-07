package io.github.nortthon.safe.scheduling.provider.redis;

import io.github.nortthon.safe.scheduling.Provider;
import io.github.nortthon.safe.scheduling.SchedulerConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Duration;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.*;

@DataRedisTest
@SpringBootConfiguration
@EnableAutoConfiguration
@RunWith(SpringRunner.class)
public class RedisProviderTest {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private Provider provider;

    @Before
    public void setup() {
        this.provider = new RedisProvider(redisTemplate);
    }

    @Test
    public void testLockingAndRunningTaskScheduled() {
        final SchedulerConfig config = SchedulerConfig.builder().name("task1").lockedFor(1000L).build();

        final AtomicBoolean runned = new AtomicBoolean(false);

        provider.execute(() -> runned.set(true), config);

        assertTrue(runned.get());
        assertEquals(true, redisTemplate.hasKey("task1"));
    }

    @Test
    public void testLockedTask() {
        final SchedulerConfig config = SchedulerConfig.builder().name("task2").lockedFor(1000L).build();

        final String now = String.valueOf(new Date().toInstant().getEpochSecond());
        redisTemplate.opsForValue().set(config.getName(), now, Duration.ofMillis(2000L));

        final AtomicBoolean runned = new AtomicBoolean(false);

        provider.execute(() -> runned.set(true), config);

        assertFalse(runned.get());
        assertEquals(true, redisTemplate.hasKey("task2"));
    }
}
