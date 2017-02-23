package api.library;

import domain.core.Branch;
import org.junit.Before;
import org.junit.Test;
import util.ListUtil;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static util.matchers.HasExactlyItems.hasExactlyItems;

public class BranchServiceTest {
    private BranchService service;

    @Before
    public void initialize() {
        service = new BranchService();
        LibraryData.deleteAll();
    }

    @Test
    public void findsByScanCode() {
        service.add("name", "b2");

        Branch branch = service.find("b2");

        assertThat(branch.getName(), equalTo("name"));
    }

    @Test(expected = DuplicateBranchCodeException.class)
    public void rejectsDuplicateScanCode() {
        service.add("", "b559");
        service.add("", "b559");
    }

    @Test(expected = InvalidBranchCodeException.class)
    public void rejectsScanCodeNotStartingWithB() {
        service.add("", "c2234");
    }

    @Test
    public void answersGeneratedId() {
        String scanCode = service.add("");

        assertTrue(scanCode.startsWith("b"));
    }

    @Test
    public void findsBranchMatchingScanCode() {
        String scanCode = service.add("a branch");

        Branch branch = service.find(scanCode);

        assertThat(branch.getName(), equalTo("a branch"));
        assertThat(branch.getScanCode(), equalTo(scanCode));
    }

    @Test
    public void returnsListOfAllPersistedBranches() {
        String eastScanCode = service.add("e");
        String westScanCode = service.add("w");

        List<Branch> all = service.allBranches();

        List<String> scanCodes = new ListUtil().map(all, "getScanCode", Branch.class, String.class);
        assertThat(scanCodes, hasExactlyItems(eastScanCode, westScanCode));
    }
}
