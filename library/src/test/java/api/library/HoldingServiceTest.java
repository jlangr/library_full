package api.library;

import com.loc.material.api.ClassificationApi;
import com.loc.material.api.Material;
import domain.core.BranchNotFoundException;
import domain.core.ClassificationApiFactory;
import domain.core.Holding;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HoldingServiceTest {
    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    private HoldingService service = new HoldingService();
    private ClassificationApi classificationApi = mock(ClassificationApi.class);
    private String branchScanCode;

    @Before
    public void initialize() {
        LibraryData.deleteAll();
        ClassificationApiFactory.setService(classificationApi);
        branchScanCode = new BranchService().add("");
    }

    @Test
    public void usesClassificationServiceToRetrieveBookDetails() {
        String isbn = "9780141439594";
        Material material = new Material(isbn, "", "", "", "");
        when(classificationApi.retrieveMaterial(isbn)).thenReturn(material);
        String barcode = service.add(isbn, branchScanCode);

        Holding holding = service.find(barcode);

        assertThat(holding.getMaterial().getSourceId(), equalTo(isbn));
    }

    @Test
    public void throwsExceptionWhenBranchNotFound() {
        expectedEx.expect(BranchNotFoundException.class);
        expectedEx.expectMessage("Branch not found: badBranchId");

        service.add("", "badBranchId");
    }
}