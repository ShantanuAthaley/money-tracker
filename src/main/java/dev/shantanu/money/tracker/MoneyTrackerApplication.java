package dev.shantanu.money.tracker;

import dev.shantanu.money.tracker.statement.StatementEventPublisher;
import dev.shantanu.money.tracker.statement.models.StatementProcessResponse;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;


@SpringBootApplication
public class MoneyTrackerApplication {

  public static void main(String[] args) {
    SpringApplication.run(MoneyTrackerApplication.class, args);
  }

}

@RestController
@RequestMapping("/api/statements")
@EnableAsync
class AccountStatementController {
  private static final Logger LOGGER = LoggerFactory.getLogger(AccountStatementController.class);
  private final StatementEventPublisher eventPublisher;
  private final Path uploadDir = Paths.get("uploaded-statements"); // Define your path

  public AccountStatementController(StatementEventPublisher eventPublisher) {
    this.eventPublisher = eventPublisher;
    // Ensure the directory exists (best practice)
    try {
      if (!uploadDir.toFile().exists()) {
        boolean mkdirs = uploadDir.toFile().mkdirs();
        if (mkdirs) {
          LOGGER.info("Successfully created director for file upload. Directory = {} ", uploadDir.toFile().getName());
        }
      }
    } catch (Exception e) {
      // Log error
      LOGGER.error("Failed to create directory - {}", uploadDir.toFile().getName());
      throw new RuntimeException("Could not create upload to directory", e);
    }
  }

  @PostMapping("/upload")
  public DeferredResult<?> uploadStatement(
    @RequestParam("file") MultipartFile file,
    @RequestParam("password") String password) throws Exception {

    if (file.isEmpty()) {
      var errorResult = new DeferredResult<>();
      errorResult.setResult("No file present in the request, please select account statement file to upload");
    }

    // 1. Save the file to a specified folder
    String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
    Path filePath = this.uploadDir.resolve(uniqueFileName);
    file.transferTo(filePath);

    // 2. Generate and publish the ApplicationEvent
    DeferredResult<StatementProcessResponse> deferredResult = new DeferredResult<>();
    // This makes the event processing asynchronous, which is ideal for Modulith.
    eventPublisher.publishStatementUploadEvent(this, uniqueFileName, filePath, null, deferredResult);

    return deferredResult;
  }
}
