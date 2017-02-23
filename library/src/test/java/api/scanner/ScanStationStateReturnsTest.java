package api.scanner;

import domain.core.Branch;
import domain.core.Patron;
import org.junit.Test;
import util.DateUtil;
import util.TimestampSource;

import java.util.Calendar;
import java.util.Date;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ScanStationStateReturnsTest extends ScanStationStateTestBase {
    public static final String PATRON_JANE_ID = "p222";
    public static final Patron PATRON_JANE = new Patron(PATRON_JANE_ID, "Jane");

    @Override
    protected ScanStationState createStateObject() {
        return new ScanStationStateReturns(scanner);
    }

    @Test
    public void displaysWarningWhenCompletePressed() {
        state.pressComplete();

        assertMessageDisplayed(ScanStationStateReturns.MSG_WAITING_FOR_RETURNS);
        assertStateUnchanged();
    }

    @Test
    public void changesStateToInventoryWhenInventoryCardPressed() {
        state.scanInventoryCard();

        assertCurrentState(ScanStationStateInventory.class);
        assertMessageDisplayed(ScanStationStateReturns.MSG_INVENTORY);
    }

    @Test
    public void changesStateToCheckoutWhenPatronCardPressed() {
        Patron patron = new Patron("p222", "");
        when(patronService.find("p222")).thenReturn(patron);
        state.scanPatron("p222");

        assertCurrentState(ScanStationStateCheckout.class);
        assertThat(scanner.getPatronId(), equalTo("p222"));
    }

    @Test
    public void changesBranchWhenBranchIdScanned() {
        Branch eastBranch = new Branch("b222", "");
        when(branchService.find("b222")).thenReturn(eastBranch);
        scanner.setBranch(new Branch("b9999", ""));

        state.scanBranchId("b222");

        assertStateUnchanged();
        assertThat(scanner.getBranchId(), equalTo(eastBranch.getScanCode()));
    }

    @Test
    public void checksInBookWhenBarcodeScanned() {
        Branch branch = new Branch("b123", "East");
        scanner.setBranch(branch);
        Date checkinDate = DateUtil.create(2017, Calendar.MARCH, 17);
        TimestampSource.queueNextTime(checkinDate);

        state.scanHolding("123:1");

        verify(holdingService).checkIn("123:1", checkinDate, branch.getScanCode());
        assertMessageDisplayed(String.format(ScanStationStateReturns.MSG_CHECKED_IN, "123:1"));
        assertStateUnchanged();
    }
}
