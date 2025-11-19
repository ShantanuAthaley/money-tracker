package dev.shantanu.money.tracker.events;

import java.nio.file.Path;
import java.time.Clock;
import org.springframework.context.ApplicationEvent;

public class AccountStatementUploadedEvent extends ApplicationEvent {
  private final String originalFileName;
  private final Path filePath;
  private final String password;

  public AccountStatementUploadedEvent(Object source, String originalFileName, Path filePath, String password) {
    Clock clock = Clock.systemDefaultZone();
    super(source, clock);
    this.originalFileName = originalFileName;
    this.filePath = filePath;
    this.password = password;
  }

  public String getOriginalFileName() {
    return originalFileName;
  }

  public Path getFilePath() {
    return filePath;
  }

  public String getPassword() {
    return password;
  }

}
