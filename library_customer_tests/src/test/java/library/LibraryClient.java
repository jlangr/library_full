package library;
import static java.util.Arrays.asList;
import java.util.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.*;
import com.loc.material.api.Material;
import controller.*;

public class LibraryClient {
   private RestTemplate template = RestTemplateFactory.create();

   public static final String SERVER = "http://localhost:3003";

   private String url(String doc) {
      return String.format(SERVER + doc);
   }

   // -- branches --

   public String addBranch(String name) {
      BranchRequest request = new BranchRequest();
      request.setName(name);
      ResponseEntity<String> response = template.postForEntity(url("/branches"), request, String.class);
      return response.getBody();
   }

   public List<BranchRequest> retrieveBranches(String user) {
      ResponseEntity<BranchRequest[]> response = template.getForEntity(url("/branches"),
         BranchRequest[].class);
      return asList(response.getBody());
   }

   // -- patrons --

   public String addPatron(String name) {
      PatronRequest request = new PatronRequest();
      // careful--something can't handle an overloaded single-arg ctor
      request.setName(name);
      ResponseEntity<String> response = template.postForEntity(url("/patrons"), request, String.class);
      return response.getBody();
   }

   public List<PatronRequest> retrievePatrons() {
      ResponseEntity<PatronRequest[]> response = template.getForEntity(url("/patrons"),
         PatronRequest[].class);
      return asList(response.getBody());
   }

   public PatronRequest retrievePatron(String patronId) {
      ResponseEntity<PatronRequest> response = template.getForEntity(url("/patrons/" + patronId),
         PatronRequest.class);
      return response.getBody();
   }

   // -- library back door --
   public void clear() {
      template.postForEntity(url("/clear"), null, null);
   }

   public void useLocalClassificationService() {
      template.postForEntity(url("/use_local_classification"), null, null);
   }

   public void addBook(Material book) {
      template.postForEntity(url("/materials"), book, null);
   }

   // -- holdings --

   public String addHolding(String sourceId, String branchScanCode) {
      AddHoldingRequest request = new AddHoldingRequest();
      request.setBranchScanCode(branchScanCode);
      request.setSourceId(sourceId);
      ResponseEntity<String> response = template.postForEntity(url("/holdings"), request, String.class);
      return response.getBody();
   }

   public int checkOutHolding(String patronId, String barcode, Date date) {
      CheckoutRequest request = new CheckoutRequest();
      request.setPatronId(patronId);
      request.setHoldingBarcode(barcode);
      request.setCheckoutDate(date);
      try {
         return template.postForEntity(url("/holdings/checkout"), request, String.class)
            .getStatusCodeValue();
      } catch (HttpStatusCodeException exception) {
         return exception.getRawStatusCode();
      }
   }

   public HoldingResponse retrieveHolding(String holdingBarcode) {
      ResponseEntity<HoldingResponse> response = template.getForEntity(url("/holdings/" + holdingBarcode),
         HoldingResponse.class);
      return response.getBody();
   }

   public void checkInHolding(String holdingBarcode, String branchScanCode, Date date) {
      CheckinRequest request = new CheckinRequest();
      request.setHoldingBarcode(holdingBarcode);
      request.setBranchScanCode(branchScanCode);
      request.setCheckinDate(date);
      template.postForEntity(url("/holdings/checkin"), request, String.class);
   }

   public List<HoldingResponse> retrieveHoldingsAtBranch(String branchScanCode) {
      ResponseEntity<HoldingResponse[]> response = template.getForEntity(url("/holdings?branchScanCode=" + branchScanCode), HoldingResponse[].class);
      return asList(response.getBody());
   }

   public void addBooks(List<Material> books) {
      books.stream().forEach(book -> addBook(book));
   }
}