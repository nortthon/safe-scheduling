package io.github.nortthon.safe.scheduling;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class SchedulerConfig {
    private final String name;
    private final long lockedFor;
}