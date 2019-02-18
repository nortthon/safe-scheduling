package io.github.nortthon.safe.scheduling;

public interface Provider {
    void execute(Runnable task, SchedulerConfig schedulerConfig);
}
