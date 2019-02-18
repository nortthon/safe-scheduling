package io.github.nortthon.safe.scheduling;

import org.springframework.core.annotation.AliasFor;
import org.springframework.scheduling.annotation.Scheduled;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Scheduled
public @interface SafeScheduled {

    String name();

    long lockedFor() default -1;

    @AliasFor(annotation = Scheduled.class)
    String cron() default "";

    @AliasFor(annotation = Scheduled.class)
    String zone() default "";

    @AliasFor(annotation = Scheduled.class)
    long fixedDelay() default -1;

    @AliasFor(annotation = Scheduled.class)
    String fixedDelayString() default "";

    @AliasFor(annotation = Scheduled.class)
    long fixedRate() default -1;

    @AliasFor(annotation = Scheduled.class)
    String fixedRateString() default "";

    @AliasFor(annotation = Scheduled.class)
    long initialDelay() default -1;

    @AliasFor(annotation = Scheduled.class)
    String initialDelayString() default "";
}
