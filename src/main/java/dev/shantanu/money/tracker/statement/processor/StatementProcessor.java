package dev.shantanu.money.tracker.statement.processor;

import dev.shantanu.bankstatement.common.AccountStatement;
import dev.shantanu.bankstatement.common.TransactionRecord;
import dev.shantanu.bankstatement.config.StatementType;
import dev.shantanu.bankstatement.parser.AccountStatementParser;
import dev.shantanu.bankstatement.parser.BankStatementParserFactory;
import dev.shantanu.bankstatement.parser.model.TransactionInfo;
import dev.shantanu.money.tracker.statement.AccountStatementPDFParser;
import dev.shantanu.money.tracker.statement.events.AccountStatementUploadedEvent;
import dev.shantanu.money.tracker.statement.models.StatementProcessResponse;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

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

      TransactionInfo transactionInfo = transactionInformation.transactionInfo();
      String accountNumber = transactionInfo.accountNumber();
      LocalDate from = transactionInfo.transactionFrom();
      LocalDate to = transactionInfo.transactionTo();

      DateRange txDateRangeInPeriod = getDateRangeFromActualTransactions(transactionInformation);
      int transactionCount = transactionInformation.transactionRecords().size();
      int numberOfTransactions = transactionCount;
      StatementProcessResponse statementProcessResponse = new StatementProcessResponse(statementUploadedEvent.getOriginalFileName(), from, to, numberOfTransactions);

      LOGGER.info("Parsing complete for file = {}. Found transaction records = {}", file.getName(), transactionCount);
      DeferredResult<StatementProcessResponse> deferredResult = statementUploadedEvent.getDeferredResult();

      deferredResult.setResult(statementProcessResponse);

    } finally {
      // 4. IMPORTANT: Clean up the uploaded file
      try {
        java.nio.file.Files.deleteIfExists(statementUploadedEvent.getFilePath());
      } catch (IOException _) {
        LOGGER.error("Failed to delete temporary file {} ", statementUploadedEvent.getFilePath());
      }
    }
  }

  private static DateRange getDateRangeFromActualTransactions(AccountStatement transactionInformation) {
    return transactionInformation.transactionRecords()
      .stream()
      .map(TransactionRecord::transactionDate)
      .collect(
        Collectors.teeing(
          Collectors.minBy(Comparator.naturalOrder()),
          Collectors.maxBy(Comparator.naturalOrder()),
          (BiFunction<Optional<LocalDate>, Optional<LocalDate>, DateRange>) (min, max)
            -> new DateRange(min.orElse(null), max.orElse(null))
        ));
  }


  private void setErrorResponse(Throwable throwable, String name) {
    LOGGER.error("Exception during processing of account transaction statement {}. Exception is = {}, caused by = {}",
      name, throwable.getMessage(), throwable.getCause().getMessage());
  }
}

record DateRange(LocalDate min, LocalDate max) {
}
