package api.library;

import com.loc.material.api.ClassificationApi;
import com.loc.material.api.Material;
import domain.core.ClassificationApiFactory;
import domain.core.Holding;
import domain.core.HoldingMap;
import domain.core.HoldingNotFoundException;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HoldingService_WithBranchCreatedTest {
    private HoldingService service = new HoldingService();
    private ClassificationApi classificationApi = mock(ClassificationApi.class);
    private String branchScanCode;

    @Before
    public void initialize() {
        LibraryData.deleteAll();
        ClassificationApiFactory.setService(classificationApi);
        branchScanCode = new BranchService().add("a branch name");
    }

    private String addHolding() {
        Material material = new Material("123", "", "", "", "");
        when(classificationApi.retrieveMaterial("123")).thenReturn(material);
        return service.add("123", branchScanCode);
    }

    @Test
    public void returnsEntireInventoryOfHoldings() {
        for (int i = 0; i < 3; i++)
            addHolding();

        HoldingMap holdings = service.allHoldings();

        assertEquals(3, holdings.size());
    }

    @Test
    public void storesNewHoldingAtBranch() {
        String barcode = addHolding();

        assertThat(service.find(barcode).getBranch().getScanCode(), equalTo(branchScanCode));
    }

    @Test
    public void findByBarCodeReturnsNullWhenNotFound() {
        assertThat(service.find("999:1"), nullValue());
    }

    @Test
    public void updatesBranchOnHoldingTransfer() {
        String barcode = addHolding();

        service.transfer(barcode, branchScanCode);

        Holding holding = service.find(barcode);
        assertThat(holding.getBranch().getScanCode(), equalTo(branchScanCode));
    }

    @Test(expected = HoldingNotFoundException.class)
    public void throwsOnTransferOfNonexistentHolding() {
        service.transfer("XXX:1", branchScanCode);
    }

    @Test
    public void holdingIsAvailableWhenNotCheckedOut() {
        String barcode = addHolding();

        assertTrue(service.isAvailable(barcode));
    }

    @Test(expected = HoldingNotFoundException.class)
    public void availabilityCheckThrowsWhenHoldingNotFound() {
        service.isAvailable("345:1");
    }

    @Test(expected = HoldingNotFoundException.class)
    public void checkinThrowsWhenHoldingIdNotFound() {
        service.checkIn("999:1", new Date(), branchScanCode);
    }
}