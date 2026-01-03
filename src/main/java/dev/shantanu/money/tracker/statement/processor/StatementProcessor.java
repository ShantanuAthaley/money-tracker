package dev.shantanu.money.tracker.statement.processor;

import dev.shantanu.bankstatement.common.AccountStatement;
import dev.shantanu.bankstatement.config.StatementType;
import dev.shantanu.bankstatement.parser.AccountStatementParser;
import dev.shantanu.bankstatement.parser.BankStatementParserFactory;
import dev.shantanu.money.tracker.events.AccountStatementUploadedEvent;
import dev.shantanu.money.tracker.statement.AccountStatementPDFParser;
import java.io.File;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
class StatementProcessorService {
  private static final Logger LOGGER = LoggerFactory.getLogger(StatementProcessorService.class);
  private final AccountStatementPDFParser pdfParser;

  StatementProcessorService(AccountStatementPDFParser pdfParser) {
    this.pdfParser = pdfParser;
  }

  @EventListener
  @Async
  void handleStatementUpload(AccountStatementUploadedEvent statementUploadedEvent) {

    LOGGER.info("Processing statement for file: {}", statementUploadedEvent.getOriginalFileName());
    File file = statementUploadedEvent.getFilePath().toFile();
    try {

      AccountStatementParser parser = new BankStatementParserFactory(StatementType.ICICI_BANK_SEARCH_STATEMENT, file).getParser();
      AccountStatement transactionInformation = parser.getTransactionInformation();

      LOGGER.info("Parsing complete for file = {}. Found transaction records = {}", file.getName(), transactionInformation.transactionRecords().size());

    } finally {
      // 4. IMPORTANT: Clean up the uploaded file
      try {
        java.nio.file.Files.deleteIfExists(statementUploadedEvent.getFilePath());
      } catch (IOException _) {
        LOGGER.error("Failed to delete temporary file {} ", statementUploadedEvent.getFilePath());
      }
    }
  }
}
