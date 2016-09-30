import java.util.*;
import cucumber.api.DataTable;
import cucumber.api.java.en.*;
import domain.core.*;
import util.DateUtil;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class Stepdefs {
   // TODO use http client
   private List<Branch> branches = new ArrayList<>();
   private Holding holding;

   class Branch {
      public Branch(String name) {
         this.name = name;
      }
      public String name;
   }

   @When("^(.*) adds? a branch with name \"(.*)\"")
   public void addBranch(String user, String name) {
      branches.add(new Branch(name));
   }

   @When("^(.*) requests a list of all branches")
   public void requestBranches(String user) {
      this.branches = retrieveBranches();
   }

   private List<Branch> retrieveBranches() {
      return branches;
   }

   @Then("^the system returns the following branches:?$")
   public void assertBranches(DataTable table) {
      table.diff(branches);
   }

   // fines
   @Given("^an available holding of type (.*)$")
   public void createHolding(String type) {
      holding = new HoldingBuilder()
         .atBranch(new domain.core.Branch("")).create();
   }

   @When("^a patron checks out the holding on (\\d+)/(\\d+)/(\\d+)$")
   public void a_patron_checks_out_the_holding_on(int year, int month, int dayOfMonth) {
      holding.checkOut(DateUtil.create(year, month, dayOfMonth));
   }

   @Then("^the due date is (\\d+)/(\\d+)/(\\d+)$")
   public void the_due_date_is(int year, int month, int dayOfMonth) {
      assertThat(holding.dateDue(), equalTo(DateUtil.create(year, month, dayOfMonth)));
   }

}
