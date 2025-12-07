package dev.shantanu.money.tracker.statement.events;

import dev.shantanu.money.tracker.statement.StatementEventPublisher;
import dev.shantanu.money.tracker.statement.StatementProcessResponse;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

import java.nio.file.Path;

@Component
record StatementEventPublisherImpl(ApplicationEventPublisher publisher) implements StatementEventPublisher {


    @Override
    public void publishStatementUploadEvent(Object publisher, String fileName, Path path, String password,
                                            DeferredResult<StatementProcessResponse> deferredResult) {
        AccountStatementUploadedEvent accountStatementUploadedEvent = new AccountStatementUploadedEvent(publisher, fileName, path, password, deferredResult);
        this.publisher.publishEvent(accountStatementUploadedEvent);
    }
}
