package io.github.nortthon.safe.scheduling;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SchedulerConfig {
    private final String name;
    private final long lockedFor;
}