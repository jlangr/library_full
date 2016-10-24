package cucumber;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static util.DateUtil.create;
import java.util.*;
import com.loc.material.api.*;
import controller.*;
import cucumber.api.DataTable;
import cucumber.api.java.en.*;
import library.LibraryClient;

// TODO use PicoContainer and injection between stepdefs?
public class Stepdefs {
   private LibraryClient libraryClient = new LibraryClient();
   private List<BranchRequest> branches;
   private List<PatronRequest> patrons;
   private Map<String,String> branchScanCodes = new HashMap<>();
   private String holdingBarcode;
   private String patronId;
   private int checkoutResponse;

   @Given("^a clean library system$")
   public void clear() {
      libraryClient.clear();
   }

   @Given("^a library system with a branch named \"(.*)\"$")
   public void clearedSystemWithBranch(String name) {
      libraryClient.clear();
      addBranch(null, name);
   }

   @Given("^a library system with one book$")
   public void addOneBookAtNewLibrary() {
      libraryClient.clear();
      libraryClient.useLocalClassificationService();
      libraryClient.addBook(new MaterialDetails("123", "", "", "123", ""));
      addBranch(null, "branch");
      addBookToBranch("123", "branch");
// TODO dup
   }

   @When("^(.*) adds? a branch named \"(.*)\"")
   public void addBranch(String user, String name) {
      String scanCode = libraryClient.addBranch(name);
      branchScanCodes.put(name, scanCode);
   }

   @When("^(.*) requests a list of all branches")
   public void requestBranches(String user) {
      branches = libraryClient.retrieveBranches(user);
   }

   @Then("^the system returns the following branches:?$")
   public void assertBranches(DataTable expectedBranches) {
      expectedBranches.unorderedDiff(branches);
   }

   @Given("^a book with source id (\\d+) is added at branch \"([^\"]*)\"$")
   public void addBookToBranch(String sourceId, String branchName) {
      holdingBarcode = libraryClient.addHolding(sourceId, branchScanCodes.get(branchName));
   }

   @Given("^a patron checks out the book on (\\d+)/(\\d+)/(\\d+)$")
   public void patronChecksOutHolding(int year, int month, int dayOfMonth) {
      addPatron("");
      checkoutResponse = libraryClient.checkOutHolding(patronId, holdingBarcode, create(year, month, dayOfMonth));
   }

   @Then("^the client is informed of a conflict")
   public void assertConflict() {
      assertThat(checkoutResponse, equalTo(409));
   }

   @Then("^the due date is (\\d+)/(\\d+)/(\\d+)$")
   public void assertDueDate(int year, int month, int dayOfMonth) {
      HoldingResponse holding = libraryClient.retrieveHolding(holdingBarcode);
      assertThat(holding.getDateDue(), equalTo(create(year, month, dayOfMonth)));
   }

   @When("^the book is returned on (\\d+)/(\\d+)/(\\d+)$")
   public void returnHolding(int year, int month, int dayOfMonth) {
      String aBranchScanCode = firstBranchScanCode();
      libraryClient.checkInHolding(holdingBarcode, aBranchScanCode, create(year, month, dayOfMonth));
   }

   private String firstBranchScanCode() {
      return branchScanCodes.values().iterator().next();
   }

   @Then("^the patron's fine balance is (\\d+)$")
   public void assertFineBalance(int expectedFineBalance) {
      PatronRequest patron = libraryClient.retrievePatron(patronId);
      assertThat(expectedFineBalance, equalTo(patron.getFineBalance()));
   }

   @Given("^a librarian adds a patron named (.*)$")
   public void addPatron(String name) {
      patronId = libraryClient.addPatron(name);
   }

   @When("^a librarian requests a list of all patrons$")
   public void requestPatrons() {
      patrons = libraryClient.retrievePatrons();
   }

   @Then("^the client shows the following patrons:$")
   public void assertPatrons(DataTable expectedPatrons) {
      expectedPatrons.unorderedDiff(patrons);
   }

   @Given("^a local classification service with:$")
   public void classificationServiceData(DataTable books) {
      libraryClient.useLocalClassificationService();
      books.asList(MaterialDetails.class)
         .stream()
         .forEach(book -> libraryClient.addBook(book));
   }

   @When("^a librarian adds a book holding with source id (\\d+) at branch \"([^\"]*)\"$")
   public void addBookHolding(String sourceId, String branchName) {
      libraryClient.addHolding(sourceId, branchScanCodes.get(branchName));
   }

   @Then("^the \"([^\"]*)\" branch contains the following holdings:$")
   public void assertBranchContains(String branchName, DataTable holdings) {
      String branchScanCode = branchScanCodes.get(branchName);
      List<HoldingResponse> retrievedHoldings = libraryClient.retrieveHoldingsAtBranch(branchScanCode);
      holdings.unorderedDiff(retrievedHoldings);
   }
}