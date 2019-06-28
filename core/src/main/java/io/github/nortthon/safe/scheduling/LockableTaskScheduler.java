package io.github.nortthon.safe.scheduling;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.ScheduledMethodRunnable;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.ScheduledFuture;

import static org.springframework.core.annotation.AnnotatedElementUtils.getMergedAnnotation;

@RequiredArgsConstructor
public class LockableTaskScheduler extends ThreadPoolTaskScheduler implements TaskScheduler {

    private final transient Provider provider;

    @Override
    public ScheduledFuture<?> schedule(final Runnable task, final Trigger trigger) {
        return super.schedule(extractor(task), trigger);
    }

    @Override
    public ScheduledFuture<?> schedule(final Runnable task, final Date startTime) {
        return super.schedule(extractor(task), startTime);
    }

    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(final Runnable task, final Date startTime, final long period) {
        return super.scheduleAtFixedRate(extractor(task), startTime, period);
    }

    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(final Runnable task, final long period) {
        return super.scheduleAtFixedRate(extractor(task), period);
    }

    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(final Runnable task, final Date startTime, final long delay) {
        return super.scheduleWithFixedDelay(extractor(task), startTime, delay);
    }

    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(final Runnable task, final long delay) {
        return super.scheduleWithFixedDelay(extractor(task), delay);
    }

    private Runnable extractor(final Runnable task) {
        return findAnnotation(task)
                .map(this::buildSchedulerConfig)
                .map(s -> (Runnable) () -> provider.execute(task, s))
                .orElse(task);
    }

    private SchedulerConfig buildSchedulerConfig(final SafeScheduled safeScheduled) {
        Assert.notNull(safeScheduled.name(), "Scheduled \"name\" must not be null");
        Assert.isTrue(safeScheduled.lockedFor() > 0, "Scheduled \"lockedFor\" must not be less than 0");

        return SchedulerConfig.builder()
                .name(safeScheduled.name())
                .lockedFor(safeScheduled.lockedFor())
                .build();
    }

    private Optional<SafeScheduled> findAnnotation(final Runnable task) {
        return Optional.of(task)
                .filter(t -> t instanceof ScheduledMethodRunnable)
                .map(t -> getMergedAnnotation(((ScheduledMethodRunnable) t).getMethod(), SafeScheduled.class));
    }
}
