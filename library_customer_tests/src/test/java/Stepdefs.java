import java.util.*;
import api.library.HoldingService;
import controller.*;
import cucumber.api.DataTable;
import cucumber.api.java.en.*;
import domain.core.*;
import static util.DateUtil.create;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class Stepdefs {
   private Holding holding;
   private LibraryClient libraryClient = new LibraryClient();
   private List<BranchRequest> branches;
   private List<PatronRequest> patrons;
   private Map<String,String> branchesByName = new HashMap<>();
   private String holdingBarcode;
   private String patronId;

   @Given("^a clean library system$")
   public void clear() {
      libraryClient.clear();
   }

   @Given("^a library system with a branch named \"(.*)\"$")
   public void clearedSystemWithBranch(String name) {
      clear(); // TODO bad form to call other steps?
      addBranch(null, name);
   }

   @When("^(.*) adds? a branch named \"(.*)\"")
   public void addBranch(String user, String name) {
      String scanCode = libraryClient.addBranch(name);
      branchesByName.put(name, scanCode);
   }

   @When("^(.*) requests a list of all branches")
   public void requestBranches(String user) {
      branches = libraryClient.retrieveBranches(user);
   }

   @Then("^the system returns the following branches:?$")
   public void assertBranches(DataTable expectedBranches) {
      expectedBranches.unorderedDiff(branches);
   }

   @Given("^an available book$")
   public void createHolding() {
      holdingBarcode = libraryClient.addHolding("123", firstBranchScanCode());
   }

   @Given("^a patron checks out the book on (\\d+)/(\\d+)/(\\d+)$")
   public void patronChecksOutHolding(int year, int month, int dayOfMonth) {
      addPatron("");
      createHolding();
      System.out.println("holding barcode @ client: " + holdingBarcode);
      libraryClient.checkOutHolding(patronId, holdingBarcode, create(year, month, dayOfMonth));
   }

   @Then("^the due date is (\\d+)/(\\d+)/(\\d+)$")
   public void assertDueDate(int year, int month, int dayOfMonth) {
      HoldingResponse holding = libraryClient.retrieveHolding(holdingBarcode);
      System.out.println("holding response:" + holding);
      assertThat(holding.getDateDue(), equalTo(create(year, month, dayOfMonth)));
   }

   @When("^the book is returned on (\\d+)/(\\d+)/(\\d+)$")
   public void returnHolding(int year, int month, int dayOfMonth) {
      String aBranchScanCode = firstBranchScanCode();
//      new HoldingService().checkIn(holding.getBarcode(), create(year, month, dayOfMonth), aBranchScanCode);
   }

   private String firstBranchScanCode() {
      return branchesByName.values().iterator().next();
   }

   @Then("^the patron's fine balance is (\\d+)$")
   public void assertFineBalance(int expectedFineBalance) {
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
}