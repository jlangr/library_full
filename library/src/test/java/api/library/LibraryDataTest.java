package api.library;

import com.loc.material.api.ClassificationApi;
import com.loc.material.api.Material;
import domain.core.Branch;
import domain.core.ClassificationApiFactory;
import domain.core.Patron;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LibraryDataTest {
    private PatronService patronService = new PatronService();
    private HoldingService holdingService = new HoldingService();
    private BranchService branchService = new BranchService();
    private ClassificationApi classificationApi;

    @Before
    public void setUpClassificationService() {
        classificationApi = mock(ClassificationApi.class);
        ClassificationApiFactory.setService(classificationApi);
    }

    @Test
    public void deleteAllRemovesAllPatrons() {
        patronService.patronAccess.add(new Patron("", "1"));
        branchService.add("2");
        Material material = new Material("3", "", "", "", "");
        when(classificationApi.retrieveMaterial("3")).thenReturn(material);
        holdingService.add(material.getSourceId(), Branch.CHECKED_OUT.getScanCode());

        LibraryData.deleteAll();

        assertTrue(patronService.allPatrons().isEmpty());
        assertTrue(holdingService.allHoldings().isEmpty());
        assertTrue(branchService.allBranches().isEmpty());
    }
}
