package api.library;

import com.loc.material.api.ClassificationApi;
import com.loc.material.api.Material;
import com.loc.material.api.MaterialType;
import domain.core.*;
import org.junit.Before;
import org.junit.Test;
import util.DateUtil;

import java.util.Date;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static util.matchers.HasExactlyItemsInAnyOrder.hasExactlyItemsInAnyOrder;

public class HoldingService_CheckInCheckOutTest {
    private HoldingService service = new HoldingService();
    private PatronService patronService = new PatronService();
    private ClassificationApi classificationApi = mock(ClassificationApi.class);
    private String patronId;
    private String branchScanCode;
    private String bookHoldingBarcode;

    @Before
    public void initialize() {
        LibraryData.deleteAll();
        ClassificationApiFactory.setService(classificationApi);
        branchScanCode = new BranchService().add("a branch name");
        patronId = patronService.add("joe");
        bookHoldingBarcode = addBookHolding();
    }

    private String addBookHolding() {
        Material material = new Material("123", "", "", "", MaterialType.Book, "");
        when(classificationApi.retrieveMaterial("123")).thenReturn(material);
        return service.add("123", branchScanCode);
    }

    @Test
    public void holdingMadeUnavailableOnCheckout() {
        service.checkOut(patronId, bookHoldingBarcode, new Date());

        assertThat(service.isAvailable(bookHoldingBarcode), equalTo(false));
    }

    @Test(expected = HoldingNotFoundException.class)
    public void checkoutThrowsWhenHoldingIdNotFound() {
        service.checkOut(patronId, "999:1", new Date());
    }

    @Test(expected = HoldingAlreadyCheckedOutException.class)
    public void checkoutThrowsWhenUnavailable() {
        service.checkOut(patronId, bookHoldingBarcode, new Date());

        service.checkOut(patronId, bookHoldingBarcode, new Date());
    }

    @Test
    public void updatesPatronWithHoldingOnCheckout() {
        service.checkOut(patronId, bookHoldingBarcode, new Date());

        HoldingMap patronHoldings = patronService.find(patronId).holdingMap();
        assertThat(patronHoldings.holdings(), hasExactlyItemsInAnyOrder(service.find(bookHoldingBarcode)));
    }

    @Test
    public void returnsHoldingToBranchOnCheckIn() {
        service.checkOut(patronId, bookHoldingBarcode, new Date());

        service.checkIn(bookHoldingBarcode, DateUtil.tomorrow(), branchScanCode);

        Holding holding = service.find(bookHoldingBarcode);
        assertTrue(holding.isAvailable());
        assertThat(holding.getBranch().getScanCode(), equalTo(branchScanCode));
    }

    @Test
    public void removesHoldingFromPatronOnCheckIn() {
        service.checkOut(patronId, bookHoldingBarcode, new Date());

        service.checkIn(bookHoldingBarcode, DateUtil.tomorrow(), branchScanCode);

        assertTrue(patronService.find(patronId).holdingMap().isEmpty());
    }

    @Test
    public void answersDueDate() {
        service.checkOut(patronId, bookHoldingBarcode, new Date());

        Date due = service.dateDue(bookHoldingBarcode);

        Holding holding = service.find(bookHoldingBarcode);
        assertThat(due, equalTo(holding.dateDue()));
    }

    @Test
    public void checkinReturnsDaysLate() {
        service.checkOut(patronId, bookHoldingBarcode, new Date());
        Date fiveDaysLate = DateUtil.addDays(service.dateDue(bookHoldingBarcode), 5);

        int daysLate = service.checkIn(bookHoldingBarcode, fiveDaysLate, branchScanCode);

        assertThat(daysLate, equalTo(5));
    }

    @Test
    public void updatesFinesOnLateCheckIn() {
        service.checkOut(patronId, bookHoldingBarcode, new Date());
        Holding holding = service.find(bookHoldingBarcode);
        Date oneDayLate = DateUtil.addDays(holding.dateDue(), 1);

        service.checkIn(bookHoldingBarcode, oneDayLate, branchScanCode);

        assertThat(patronService.find(patronId).fineBalance(), equalTo(MaterialType.Book.getDailyFine()));
    }
}
