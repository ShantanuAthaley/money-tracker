package dev.shantanu.money.tracker.statement;

import dev.shantanu.money.tracker.statement.models.AccountTransactionDetails;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBufferedFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class AccountStatementPDFParser {

  public AccountTransactionDetails getTransactionDetails(Path path, String securityPassword) throws IOException {
    List<Document> documents = getDecryptedPdfDocuments(path, securityPassword);

    // TODO: Replace this with your actual logic to extract structured data
    // For demonstration, we'll just extract all text and assume a fixed account number
    String allText = documents
      .stream()
      .filter(document -> Objects.requireNonNull(document.getText())
        .contains("Statement of Transactions in Savings Account"))
      .map(Document::getFormattedContent)
      .collect(Collectors.joining("\n"));

    // *** The real work is here: Parsing 'allText' into AccountTransaction POJOs ***
    // This often involves regex, custom parsers, or even another LLM call if the format varies.

    // Example placeholder result:
    return new AccountTransactionDetails("1234567890", // Placeholder account number (must be extracted from 'allText')
      List.of() // The actual list of transactions
    );
  }

  // Core function to handle decryption and Spring AI Reader
  private List<Document> getDecryptedPdfDocuments(Path path, String securityPassword) throws IOException {
    File file = path.toFile();
    try (RandomAccessReadBufferedFile raInput = new RandomAccessReadBufferedFile(file);
         PDDocument pdDocument = Loader.loadPDF(raInput, securityPassword);
         ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

      pdDocument.setAllSecurityToBeRemoved(true);
      // 1. PDFBox Decrypts/Opens the document
      // If password is wrong, InvalidPasswordException is thrown here.

      // 2. Save the now-open/decrypted document to a byte stream
      pdDocument.save(outputStream);

      // 3. Convert the byte stream into a Spring Resource
      InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
      Resource decryptedResource = new InputStreamResource(inputStream);

      // 4. Use Spring AI's ParagraphPdfDocumentReader on the decrypted Resource
      //ParagraphPdfDocumentReader pdfReader = new ParagraphPdfDocumentReader(decryptedResource);
      PDFParser pdfParser = new PDFParser(raInput, securityPassword);
      PagePdfDocumentReader tikaDocumentReader = new PagePdfDocumentReader(decryptedResource);

      return tikaDocumentReader.get(); // Read all documents (pages/paragraphs)

    } catch (InvalidPasswordException ipe) {
      throw new IOException("Incorrect password provided for the PDF file: " + ipe.getMessage(), ipe);
    }
  }

  private void decryptAndParsePdf(Path path, String securityPassword) {
    File file = path.toFile();
    try (RandomAccessReadBufferedFile raInput = new RandomAccessReadBufferedFile(file)) {
      String[] split = getTextLines(securityPassword, raInput);
      Path parent = path.getParent();
      String fullPath = parent.toUri().getPath().concat(".txt");
      OutputStream outputStream = Files.newOutputStream(Path.of(fullPath));
      outputStream.write(split.toString().getBytes());

    } catch (InvalidPasswordException ipException) {
      //TODO: Handle invalid password
    } catch (IOException ioException) {
      //TODO: Handle other IO exceptions
    }
  }

  private static String[] getTextLines(String securityPassword, RandomAccessReadBufferedFile raInput) throws IOException {
    PDFParser parser = new PDFParser(raInput, securityPassword);
    PDDocument pdDocument = parser.parse();
    pdDocument.setAllSecurityToBeRemoved(true);
    List<String> accountStatmentText = new ArrayList<>();
    PDFTextStripper textStripper = new PDFTextStripper();
    textStripper.setAddMoreFormatting(true);
    textStripper.setSuppressDuplicateOverlappingText(false);
    //String text = textStripper.getText(pdDocument);
    textStripper.setSortByPosition(true);
    String[] split = textStripper.getText(pdDocument)
      .split("\\n");
    return split;
  }
}