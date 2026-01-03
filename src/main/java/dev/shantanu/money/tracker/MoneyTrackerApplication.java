package dev.shantanu.money.tracker;

import dev.shantanu.money.tracker.events.AccountStatementUploadedEvent;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@SpringBootApplication
public class MoneyTrackerApplication {

  public static void main(String[] args) {
    SpringApplication.run(MoneyTrackerApplication.class, args);
  }

}

@RestController
@RequestMapping("/api/statements")
class AccountStatementController {

  private final ApplicationEventPublisher eventPublisher;
  private final Path uploadDir = Paths.get("uploaded-statements"); // Define your path

  public AccountStatementController(ApplicationEventPublisher eventPublisher) {
    this.eventPublisher = eventPublisher;
    // Ensure the directory exists (best practice)
    try {
      if (!uploadDir.toFile().exists()) {
        uploadDir.toFile().mkdirs();
      }
    } catch (Exception e) {
      // Log error
      throw new RuntimeException("Could not create upload directory", e);
    }
  }

  @PostMapping("/upload")
  public String uploadStatement(
    @RequestParam("file") MultipartFile file,
    @RequestParam("password") String password) throws Exception {

    if (file.isEmpty()) {
      return "Please select a file to upload";
    }

    // 1. Save the file to a specified folder
    String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
    Path filePath = this.uploadDir.resolve(uniqueFileName);
    file.transferTo(filePath);

    // 2. Generate and publish the ApplicationEvent
    AccountStatementUploadedEvent event = new AccountStatementUploadedEvent(
      this,           // source
      file.getOriginalFilename(), // original file name for logging/reference
      filePath,       // path to the saved file
      password       // password provided by the user
    );

    // This makes the event processing asynchronous, which is ideal for Modulith.
    eventPublisher.publishEvent(event);

    return "File uploaded successfully and processing initiated.";
  }
}
