package dev.shantanu.money.tracker.statement;

import dev.shantanu.money.tracker.statement.models.StatementProcessResponse;
import java.nio.file.Path;
import org.springframework.web.context.request.async.DeferredResult;

public interface StatementEventPublisher {
  void publishStatementUploadEvent(
    Object publisher,           // source
    String fileName, // original file name for logging/reference
    Path path,       // path to the saved file
    String password,       // password provided by the user
    DeferredResult<StatementProcessResponse> deferredResult);
}
