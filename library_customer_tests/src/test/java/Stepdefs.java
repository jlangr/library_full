import java.util.*;
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

   @Given("^an empty library system$")
   public void clear() {
      libraryClient.clear();
   }

   @When("^(.*) adds? a branch with name \"(.*)\"")
   public void addBranch(String user, String name) {
      libraryClient.addBranch(name);
   }

   @When("^(.*) requests a list of all branches")
   public void requestBranches(String user) {
      branches = libraryClient.retrieveBranches(user);
   }

   @Then("^the system returns the following branches:?$")
   public void assertBranches(DataTable expectedBranches) {
      expectedBranches.unorderedDiff(branches);
   }

   // fines--direct to code!
   @Given("^an available holding of type (.*)$")
   public void createHolding(String type) {
      holding = new HoldingBuilder()
         .atBranch(new Branch("")).create();
   }

   @When("^a patron checks out the holding on (\\d+)/(\\d+)/(\\d+)$")
   public void checkOutHolding(int year, int month, int dayOfMonth) {
      holding.checkOut(create(year, month, dayOfMonth));
   }

   @Then("^the due date is (\\d+)/(\\d+)/(\\d+)$")
   public void assertDueDate(int year, int month, int dayOfMonth) {
      assertThat(holding.dateDue(), equalTo(create(year, month, dayOfMonth)));
   }

   @Given("^a patron checks out the book on (\\d+)/(\\d+)/(\\d+)$")
   public void patronChecksOutHolding(int year, int month, int dayOfMonth) {
   }

   @When("^the book is returned on (\\d+)/(\\d+)/(\\d+)$")
   public void returnHolding(int year, int month, int dayOfMonth) {
   }

   @Then("^the patron fine balance is (\\d+)$")
   public void assertFineBalance(int expectedFineBalance) {
   }

   @Given("^a librarian adds a patron named (.*)$")
   public void addPatron(String name) {
      libraryClient.addPatron(name);
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