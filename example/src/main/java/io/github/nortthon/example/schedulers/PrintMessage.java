package io.github.nortthon.example.schedulers;

import io.github.nortthon.safe.scheduling.SafeScheduled;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PrintMessage {

    @SafeScheduled(name = "print-message", lockedFor = 4000L, cron = "*/5 * * * * *")
    public void execute() {
        log.info("Message here");
    }
}
