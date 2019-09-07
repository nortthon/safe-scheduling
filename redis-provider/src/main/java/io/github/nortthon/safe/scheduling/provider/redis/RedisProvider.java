package io.github.nortthon.safe.scheduling.provider.redis;

import io.github.nortthon.safe.scheduling.Provider;
import io.github.nortthon.safe.scheduling.SchedulerConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.Duration;
import java.util.Date;
import java.util.Optional;

@RequiredArgsConstructor
public class RedisProvider implements Provider {

    private final StringRedisTemplate redisTemplate;

    @Override
    public void execute(final Runnable task, final SchedulerConfig schedulerConfig) {
        final Duration duration = Duration.ofMillis(schedulerConfig.getLockedFor());
        final String now = String.valueOf(new Date().getTime());

        Optional.ofNullable(redisTemplate.opsForValue()
                .setIfAbsent(schedulerConfig.getName(), now, duration))
                .filter(s -> s)
                .ifPresent(s -> task.run());
    }
}
