package cucumber;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static util.DateUtil.create;
import java.util.List;
import controller.MaterialRequest;
import cucumber.api.DataTable;
import cucumber.api.java.en.*;
import library.LibraryClient;

// TODO [x] use PicoContainer and injection between stepdefs?
public class Stepdefs {
   private LibraryClient libraryClient = new LibraryClient();
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

   @Given("^a branch named \"([^\"]*)\" with the following holdings:$")
   public void createBranchWithHoldings(String branchName, List<String> titles) {
      libraryClient.addBranch(branchName);
      libraryClient.useLocalClassificationService();
      libraryClient.addHoldingsWithTitles(titles, branchName);
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

   @Given("^a patron checks out \"(.*)\" on (\\d+)/(\\d+)/(\\d+)$")
   public void patronChecksOutHolding(String title, int year, int month, int dayOfMonth) {
      libraryClient.addPatron("");
      checkoutResponse = libraryClient.checkOut(title, create(year, month, dayOfMonth));
   }

   @Then("^\"(.*)\" (is|is not) available")
   public void assertAvailable(String title, String isOrIsNot) {
      assertThat(libraryClient.retrieveHoldingWithTitle(title).getIsAvailable(), is(isOrIsNot.equals("is")));
   }

   @Then("^the client is informed of a conflict")
   public void assertConflict() {
      assertThat(checkoutResponse, equalTo(409));
   }

   @Then("^the due date for \"(.*)\" is (\\d+)/(\\d+)/(\\d+)$")
   public void assertDueDate(String title, int year, int month, int dayOfMonth) {
      assertThat(libraryClient.retrieveHoldingWithTitle(title).getDateDue(),
         equalTo(create(year, month, dayOfMonth)));
   }

   @When("^\"(.*)\" is returned on (\\d+)/(\\d+)/(\\d+) to \"(.*)\"$")
   public void bookReturnedOnTo(String title, int year, int month, int dayOfMonth, String branchName) {
      libraryClient.checkIn(title, branchName, create(year, month, dayOfMonth));
   }

   @When("^\"(.*)\" is returned on (\\d+)/(\\d+)/(\\d+)$")
   public void bookReturnedOn(String title, int year, int month, int dayOfMonth) {
      libraryClient.checkIn(title, create(year, month, dayOfMonth));
   }

   @Then("^the patron's fine balance is (\\d+)$")
   public void assertFineBalance(int expectedFineBalance) {
      assertThat(expectedFineBalance, equalTo(libraryClient.currentPatron().getFineBalance()));
   }

   @Given("^a librarian adds a patron named (.*)$")
   public void addPatron(String name) {
      libraryClient.addPatron(name);
   }

   @When("^a librarian requests a list of all patrons$")
   public void requestPatrons() {
      libraryClient.retrievePatrons();
   }

   @Then("^the client shows the following patrons:$")
   public void assertPatrons(DataTable expectedPatrons) {
      expectedPatrons.unorderedDiff(libraryClient.retrievedPatrons());
   }

   @Given("^a local classification service with:$")
   public void classificationServiceData(DataTable books) {
      libraryClient.useLocalClassificationService();
      libraryClient.addBooks(books.asList(MaterialRequest.class));
   }

   @When("^a librarian adds a book holding with source id (\\d+) at branch \"([^\"]*)\"$")
   public void addBookHolding(String sourceId, String branchName) {
      libraryClient.addHolding(sourceId, "???", branchName);
   }

   @Then("^the \"([^\"]*)\" branch contains the following holdings:$")
   public void assertBranchContains(String branchName, DataTable holdings) {
      holdings.unorderedDiff(libraryClient.retrieveHoldingsAtBranch(branchName));
   }
}