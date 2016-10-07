import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static util.DateUtil.create;
import java.util.*;
import controller.*;
import cucumber.api.DataTable;
import cucumber.api.java.en.*;

// TODO does Cucumber support World like Cucumber for Ruby?
public class Stepdefs {
   private LibraryClient libraryClient = new LibraryClient();
   private List<BranchRequest> branches;
   private List<PatronRequest> patrons;
   private Map<String,String> branchesByName = new HashMap<>();
   private String holdingBarcode;
   private String patronId;
   private int checkoutResponse;

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
      holdingBarcode = createAbitraryHoldingAtFirstBranch();
   }

   private String createAbitraryHoldingAtFirstBranch() {
      return libraryClient.addHolding("123", firstBranchScanCode());
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
      return branchesByName.values().iterator().next();
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
}