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
      libraryClient.addBook(new Material("123", "", "", "123", ""));
      addBranch(null, "branch");
      addBookAtBranch("123", "branch");
// TODO dup
   }

   class AddHolding {
      public String title;
      public String sourceId;
   }

   Map<String,String> holdingBarcodes = new HashMap<>();

   @Given("^a branch named \"([^\"]*)\" with the following holdings:$")
   public void createBranchWithHoldings(String branchName, DataTable holdings) {
      addBranch(null, branchName);
      holdings.asList(AddHolding.class).forEach(holding ->
      // overload instead!
         holdingBarcodes.put(
            holding.title,
            addH(holding, branchName)
            )
         );
   }

   String addH(AddHolding holding, String branchName) {
      String bc = libraryClient.addHolding(holding.sourceId, branchScanCodes.get(branchName));
      System.out.println("added holding " + bc);
         return bc;
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
   public void addBookAtBranch(String sourceId, String branchName) {
      holdingBarcode = libraryClient.addHolding(sourceId, branchScanCodes.get(branchName));
      System.out.printf("Added holding %s at branch %s", holdingBarcode, branchScanCodes.get(branchName));
   }

   @Given("^a patron checks out (.*) on (\\d+)/(\\d+)/(\\d+)$")
   public void patronChecksOutHolding(String title, int year, int month, int dayOfMonth) {
      addPatron("");
      String holdingBarcode = holdingBarcodes.get(title);
      checkoutResponse = libraryClient.checkOutHolding(patronId, holdingBarcode, create(year, month, dayOfMonth));
   }

   @Then("^(.*) (is|is not) available")
   public void assertAvailable(String title, String isOrIsNot) {
      String holdingBarcode = holdingBarcodes.get(title);
      boolean expectedAvailable = isOrIsNot.equals("is");
      HoldingResponse holding = libraryClient.retrieveHolding(holdingBarcode);
      assertThat(holding.getIsAvailable(), is(expectedAvailable));
   }

   @Then("^the client is informed of a conflict")
   public void assertConflict() {
      assertThat(checkoutResponse, equalTo(409));
   }

   @Then("^the due date for (.*) is (\\d+)/(\\d+)/(\\d+)$")
   public void assertDueDate(String title, int year, int month, int dayOfMonth) {
      HoldingResponse holding = libraryClient.retrieveHolding(holdingBarcodes.get(title));
      assertThat(holding.getDateDue(), equalTo(create(year, month, dayOfMonth)));
   }

   @When("^(.*) is returned on (\\d+)/(\\d+)/(\\d+) to \"(.*)\"$")
   public void bookReturnedOnTo(String title, int year, int month, int dayOfMonth, String branchName) {
      libraryClient.checkInHolding(holdingBarcodes.get(title), branchScanCodes.get(branchName), create(year, month, dayOfMonth));
   }

   @When("^(.*) is returned on (\\d+)/(\\d+)/(\\d+)$")
   public void bookReturnedOn(String title, int year, int month, int dayOfMonth) {
      libraryClient.checkInHolding(holdingBarcodes.get(title), firstBranchScanCode(), create(year, month, dayOfMonth));
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
      libraryClient.addBooks(books.asList(Material.class));
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