package dev.shantanu.money.tracker.common;

import org.springframework.context.ApplicationEvent;
import org.springframework.web.context.request.async.DeferredResult;

import java.time.Clock;

public class DomainEvent<T, U> extends ApplicationEvent {
    DeferredResult<U> deferredResult;

    private DomainEvent(T source, DeferredResult<U> deferredResult) {
        this.deferredResult = deferredResult;
        super(source);
    }

    private DomainEvent(T source, Clock clock, DeferredResult<U> deferredResult) {
        this.deferredResult = deferredResult;
        super(source, clock);
    }

    public static <T, U> DomainEvent<T, U> build(T t, DeferredResult<U> deferredResult) {
        return new DomainEvent<>(t, deferredResult);
    }

    public static <T, U> DomainEvent<T, U> build(T t, Clock clock, DeferredResult<U> deferredResult) {
        return new DomainEvent<>(t, clock, deferredResult);
    }
}


