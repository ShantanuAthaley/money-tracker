package dev.shantanu.money.tracker.statement.events;

import dev.shantanu.money.tracker.statement.models.StatementProcessResponse;
import java.nio.file.Path;
import java.time.Clock;
import java.util.concurrent.atomic.AtomicReference;
import org.springframework.context.ApplicationEvent;
import org.springframework.web.context.request.async.DeferredResult;

public class AccountStatementUploadedEvent extends ApplicationEvent {
  private final String originalFileName;
  private final AtomicReference<Path> filePath = new AtomicReference<>();
  private final String password;
  private final DeferredResult<StatementProcessResponse> deferredResult;

  public AccountStatementUploadedEvent(Object source, String originalFileName, Path filePath, String password,
                                       DeferredResult<StatementProcessResponse> deferredResult) {
    Clock clock = Clock.systemDefaultZone();
    super(source, clock);
    this.originalFileName = originalFileName;
    this.filePath.set(filePath);
    this.password = password;
    this.deferredResult = deferredResult;
  }

  public String getOriginalFileName() {
    return originalFileName;
  }

  public Path getFilePath() {
    return filePath.get();
  }

  public String getPassword() {
    return password;
  }

  public DeferredResult<StatementProcessResponse> getDeferredResult() {
    return this.deferredResult;
  }

}
