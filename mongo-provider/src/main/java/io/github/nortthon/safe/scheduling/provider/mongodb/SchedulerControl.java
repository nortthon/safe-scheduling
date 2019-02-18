package io.github.nortthon.safe.scheduling.provider.mongodb;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import static io.github.nortthon.safe.scheduling.provider.mongodb.SchedulerControl.COLLECTION;

@Getter
@Setter
@Document(COLLECTION)
public class SchedulerControl {
    protected static final String COLLECTION = "schedulerControls";
    @Id private String id;
    private long lockedAt;
    private long lockedUntil;
}
