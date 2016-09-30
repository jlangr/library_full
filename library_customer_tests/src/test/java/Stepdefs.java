import java.util.*;
import controller.BranchRequest;
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

   @When("^(.*) adds? a branch with name \"(.*)\"")
   public void addBranch(String user, String name) {
      System.out.println("new branch name" + name);
      libraryClient.addBranch(name);
   }

   @When("^(.*) requests a list of all branches")
   public void requestBranches(String user) {
      branches = libraryClient.retrieveBranches(user);
   }

   @Then("^the system returns the following branches:?$")
   public void assertBranches(DataTable table) {
      table.unorderedDiff(branches);
   }

   // fines--direct to code!
   @Given("^an available holding of type (.*)$")
   public void createHolding(String type) {
      holding = new HoldingBuilder()
         .atBranch(new Branch("")).create();
   }

   @When("^a patron checks out the holding on (\\d+)/(\\d+)/(\\d+)$")
   public void a_patron_checks_out_the_holding_on(int year, int month, int dayOfMonth) {
      holding.checkOut(create(year, month, dayOfMonth));
   }

   @Then("^the due date is (\\d+)/(\\d+)/(\\d+)$")
   public void the_due_date_is(int year, int month, int dayOfMonth) {
      assertThat(holding.dateDue(), equalTo(create(year, month, dayOfMonth)));
   }
}