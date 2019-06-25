package io.github.nortthon.safe.scheduling.provider.mongodb;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Getter
@Setter
@Document(SchedulerControl.COLLECTION)
public class SchedulerControl {

    protected static final String COLLECTION = "schedulerControls";

    @Id
    private String id;

    private long lockedAt;

    private long lockedUntil;

}
