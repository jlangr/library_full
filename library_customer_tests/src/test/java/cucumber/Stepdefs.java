package cucumber;
import static org.hamcrest.CoreMatchers.*;
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
   private List<PatronRequest> retrievedPatrons;
   private String patronId;
   private int checkoutResponse;

   @Given("^a clean library system$")
   public void clear() {
      libraryClient.clear();
   }

   @Given("^a library system with a branch named \"(.*)\"$")
   public void clearedSystemWithBranch(String name) {
      libraryClient.clear();
      libraryClient.addBranch(name);
   }

   @Given("^a library system with one book$")
   public void addOneBookAtNewLibrary() {
      libraryClient.clear();
      libraryClient.useLocalClassificationService();
      libraryClient.addBook(new Material("123", "", "", "123", ""));
      libraryClient.addBranch("branchName");
      libraryClient.addHolding("123", "123title", "branchName");
   }

   @Given("^a branch named \"([^\"]*)\" with the following holdings:$")
   public void createBranchWithHoldings(String branchName, DataTable holdings) {
      addBranch(null, branchName);
      class AddHolding {
         public String title;
         public String sourceId;
      }
      holdings.asList(AddHolding.class).forEach(holding ->
         libraryClient.addHolding(holding.sourceId, holding.title, branchName));
   }

   @When("^(.*) adds? a branch named \"(.*)\"")
   public void addBranch(String user, String name) {
      libraryClient.addBranch(name);
   }

   @When("^(.*) requests a list of all branches")
   public void requestBranches(String user) {
      libraryClient.retrieveBranches(user);
   }

   @Then("^the system returns the following branches:?$")
   public void assertBranches(DataTable expectedBranches) {
      expectedBranches.unorderedDiff(libraryClient.retrievedBranches());
   }

   @Given("^a patron checks out (.*) on (\\d+)/(\\d+)/(\\d+)$")
   public void patronChecksOutHolding(String title, int year, int month, int dayOfMonth) {
      addPatron("");
      checkoutResponse = libraryClient.checkOut(patronId, title, create(year, month, dayOfMonth));
   }

   @Then("^(.*) (is|is not) available")
   public void assertAvailable(String title, String isOrIsNot) {
      assertThat(libraryClient.retrieveHoldingWithTitle(title).getIsAvailable(),
         is(isOrIsNot.equals("is")));
   }

   @Then("^the client is informed of a conflict")
   public void assertConflict() {
      assertThat(checkoutResponse, equalTo(409));
   }

   @Then("^the due date for (.*) is (\\d+)/(\\d+)/(\\d+)$")
   public void assertDueDate(String title, int year, int month, int dayOfMonth) {
      assertThat(libraryClient.retrieveHoldingWithTitle(title).getDateDue(),
         equalTo(create(year, month, dayOfMonth)));
   }

   @When("^(.*) is returned on (\\d+)/(\\d+)/(\\d+) to \"(.*)\"$")
   public void bookReturnedOnTo(String title, int year, int month, int dayOfMonth, String branchName) {
      libraryClient.checkIn(title, branchName, create(year, month, dayOfMonth));
   }

   @When("^(.*) is returned on (\\d+)/(\\d+)/(\\d+)$")
   public void bookReturnedOn(String title, int year, int month, int dayOfMonth) {
      libraryClient.checkIn(title, create(year, month, dayOfMonth));
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
      retrievedPatrons = libraryClient.retrievePatrons();
   }

   @Then("^the client shows the following patrons:$")
   public void assertPatrons(DataTable expectedPatrons) {
      expectedPatrons.unorderedDiff(retrievedPatrons);
   }

   @Given("^a local classification service with:$")
   public void classificationServiceData(DataTable books) {
      libraryClient.useLocalClassificationService();
      libraryClient.addBooks(books.asList(Material.class));
   }

   @When("^a librarian adds a book holding with source id (\\d+) at branch \"([^\"]*)\"$")
   public void addBookHolding(String sourceId, String branchName) {
      libraryClient.addHolding(sourceId, "???", branchName);
   }

   @Then("^the \"([^\"]*)\" branch contains the following holdings:$")
   public void assertBranchContains(String branchName, DataTable holdings) {
      List<HoldingResponse> retrievedHoldings = libraryClient.retrieveHoldingsAtBranch(branchName);
      holdings.unorderedDiff(retrievedHoldings);
   }
}