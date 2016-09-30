import java.util.*;
import cucumber.api.DataTable;
import cucumber.api.java.en.*;

public class Stepdefs {
   private List<Branch> branches = new ArrayList<>();

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
}
