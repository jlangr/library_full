package library;

import static java.util.Arrays.asList;
import java.util.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.*;
import controller.*;

public class LibraryClient {
   private RestTemplate template = RestTemplateFactory.create();
   private Map<String, String> holdingBarcodes = new HashMap<>();
   private Map<String, String> branchScanCodes = new HashMap<>();
   private List<BranchRequest> retrievedBranches = new ArrayList<>();
   private List<PatronRequest> retrievedPatrons = new ArrayList<>();
   private String patronId;

   public static final String SERVER = "http://localhost:3003";

   private String url(String doc) {
      return String.format(SERVER + doc);
   }

   // -- branches --

   public List<BranchRequest> retrievedBranches() {
      return retrievedBranches;
   }

   private String branchScanCode(String name) {
      return branchScanCodes.get(name);
   }

   public void addBranch(String name) {
      branchScanCodes.put(name, postBranch(name));
   }

   public String postBranch(String name) {
      BranchRequest request = new BranchRequest();
      request.setName(name);
      ResponseEntity<String> response = template.postForEntity(url("/branches"), request, String.class);
      return response.getBody();
   }

   public void retrieveBranches(String user) {
      ResponseEntity<BranchRequest[]> response = template.getForEntity(url("/branches"),
         BranchRequest[].class);
      retrievedBranches = asList(response.getBody());
   }

   // -- patrons --

   public void addPatron(String name) {
      PatronRequest request = new PatronRequest();
      // careful--something can't handle an overloaded single-arg ctor
      request.setName(name);
      ResponseEntity<String> response = template.postForEntity(url("/patrons"), request, String.class);
      patronId = response.getBody();
   }

   public List<PatronRequest> retrievedPatrons() {
      return retrievedPatrons;
   }

   public void retrievePatrons() {
      ResponseEntity<PatronRequest[]> response = template.getForEntity(url("/patrons"),
         PatronRequest[].class);
      retrievedPatrons = asList(response.getBody());
   }

   public PatronRequest currentPatron() {
      return retrievePatron(patronId);
   }

   public PatronRequest retrievePatron(String patronId) {
      return template.getForEntity(url("/patrons/" + patronId), PatronRequest.class).getBody();
   }

   // -- library back door --
   public void clear() {
      template.postForEntity(url("/clear"), null, null);
   }

   public void useLocalClassificationService() {
      template.postForEntity(url("/use_local_classification"), null, null);
   }

   public void addBooks(List<MaterialRequest> books) {
      books.stream().forEach(book -> addBook(book));
   }

   public void addBook(MaterialRequest book) {
      template.postForEntity(url("/materials"), book, null);
   }

   public void resetCurrentDate() {
      template.postForEntity(url("/reset_current_date"), null, null);
   }

   public void setCurrentDate(Date date) {
      template.postForEntity(url("/current_date"), date, null);
   }

   // -- holdings --

   public void addHoldingsWithTitles(List<String> titles, String branchName) {
      titles.stream().map(title -> createBook(title)).forEach(book -> {
         addBook(book);
         addHolding(book.getSourceId(), book.getTitle(), branchName);
      });
   }

   public void addHolding(String sourceId, String title, String branchName) {
      holdingBarcodes.put(title, postHolding(sourceId, branchScanCode(branchName)));
   }

   public String postHolding(String sourceId, String branchScanCode) {
      AddHoldingRequest request = new AddHoldingRequest();
      request.setBranchScanCode(branchScanCode);
      request.setSourceId(sourceId);
      return template.postForEntity(url("/holdings"), request, String.class).getBody();
   }

   public int checkOut(String title, Date date) {
      return postCheckOut(patronId, holdingBarcode(title), date);
   }

   public int postCheckOut(String patronId, String barcode, Date date) {
      CheckoutRequest request = new CheckoutRequest();
      request.setPatronId(patronId);
      request.setHoldingBarcode(barcode);
      request.setCheckoutDate(date);
      try {
         return template.postForEntity(url("/holdings/checkout"), request, String.class).getStatusCodeValue();
      } catch (HttpStatusCodeException exception) {
         return exception.getRawStatusCode();
      }
   }

   public HoldingResponse retrieveHoldingWithTitle(String title) {
      return getHolding(holdingBarcode(title));
   }

   public HoldingResponse getHolding(String holdingBarcode) {
      return template.getForEntity(url("/holdings/" + holdingBarcode), HoldingResponse.class).getBody();
   }

   public void checkIn(String title, String branchName, Date date) {
      postCheckIn(holdingBarcode(title), branchScanCode(branchName), date);
   }

   public void checkIn(String title, Date date) {
      String firstBranchName = branchScanCodes.keySet().iterator().next();
      postCheckIn(holdingBarcode(title), branchScanCode(firstBranchName), date);
   }

   public void postCheckIn(String holdingBarcode, String branchScanCode, Date date) {
      CheckinRequest request = new CheckinRequest();
      request.setHoldingBarcode(holdingBarcode);
      request.setBranchScanCode(branchScanCode);
      request.setCheckinDate(date);
      template.postForEntity(url("/holdings/checkin"), request, String.class);
   }

   public List<HoldingResponse> retrieveHoldingsAtBranch(String branchName) {
      ResponseEntity<HoldingResponse[]> response = template.getForEntity(
         url("/holdings?branchScanCode=" + branchScanCode(branchName)), HoldingResponse[].class);
      return asList(response.getBody());
   }

   private String holdingBarcode(String title) {
      return holdingBarcodes.get(title);
   }

   private MaterialRequest createBook(String title) {
      MaterialRequest material = new MaterialRequest();
      material.setTitle(title);
      material.setSourceId(title);
      material.setFormat("Book");
      material.setClassification(title);
      return material;
   }
}