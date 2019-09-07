package io.github.nortthon.safe.scheduling.provider.mongodb;


import io.github.nortthon.safe.scheduling.Provider;
import io.github.nortthon.safe.scheduling.SchedulerConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.time.Instant;
import java.util.Date;

import static java.util.Optional.ofNullable;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Update.update;

@Slf4j
@RequiredArgsConstructor
public class MongoProvider implements Provider {

    private static final String ID = "_id";
    private static final String LOCKED_AT = "lockedAt";
    private static final String LOCKED_UNTIL = "lockedUntil";

    private final MongoTemplate mongoTemplate;

    @Override
    public void execute(final Runnable task, final SchedulerConfig schedulerConfig) {
        final long time = new Date().getTime();
        long until = time + schedulerConfig.getLockedFor();

        final Query query = query(where(ID).is(schedulerConfig.getName()).and(LOCKED_UNTIL).lte(time));
        final Update update = update(LOCKED_AT, time).set(LOCKED_UNTIL, until);

        try {
            ofNullable(mongoTemplate.upsert(query, update, SchedulerControl.class)).ifPresent(u -> task.run());
        } catch (final DuplicateKeyException e) {
            log.trace("The resource named \"{}\" has locked at moment", schedulerConfig.getName(), e);
        }
    }
}
