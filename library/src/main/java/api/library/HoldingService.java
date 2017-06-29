package api.library;

import com.loc.material.api.Material;
import domain.core.*;
import persistence.PatronStore;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HoldingService {
    private Catalog catalog = new Catalog();

    public String add(String sourceId, String branchId) {
        Branch branch = findBranch(branchId);
        Holding holding = new Holding(retrieveMaterialDetails(sourceId), branch);
        catalog.add(holding);
        return holding.getBarcode();
    }

    private Material retrieveMaterialDetails(String sourceId) {
        Material material =
                ClassificationApiFactory.getService().retrieveMaterial(sourceId);
        if (material == null)
            throw new InvalidSourceIdException("cannot retrieve material with source ID " + sourceId);
        return material;
    }

    private Branch findBranch(String branchId) {
        Branch branch = new BranchService().find(branchId);
        if (branch == null)
            throw new BranchNotFoundException("Branch not found: " + branchId);
        return branch;
    }

    public boolean isAvailable(String barCode) {
        Holding holding = find(barCode);
        if (holding == null)
            throw new HoldingNotFoundException();
        return holding.isAvailable();
    }

    public HoldingMap allHoldings() {
        HoldingMap stack = new HoldingMap();
        for (Holding holding : catalog)
            stack.add(holding);
        return stack;
    }

    public Holding find(String barCode) {
        return catalog.find(barCode);
    }

    public List<Holding> findByBranch(String branchScanCode) {
        return catalog.findByBranch(branchScanCode);
    }

    public void transfer(String barcode, String branchScanCode) {
        Holding holding = find(barcode);
        if (holding == null)
            throw new HoldingNotFoundException();
        Branch branch = new BranchService().find(branchScanCode);
        holding.transfer(branch);
    }

    public Date dateDue(String barCode) {
        Holding holding = find(barCode);
        if (holding == null)
            throw new HoldingNotFoundException();
        return holding.dateDue();
    }

    public void checkOut(String patronId, String barCode, Date date) {
        Holding holding = find(barCode);
        if (holding == null)
            throw new HoldingNotFoundException();
        if (!holding.isAvailable())
            throw new HoldingAlreadyCheckedOutException();
        holding.checkOut(date);

        PatronStore patronAccess = new PatronStore();
        Patron patron = patronAccess.find(patronId);
        patronAccess.addHoldingToPatron(patron, holding);
    }

    @SuppressWarnings("incomplete-switch")
    public int checkIn(String barCode, Date date, String branchScanCode) {
        Holding holding = loadHolding(barCode);
        holding.checkIn(date, loadBranch(branchScanCode));
        Patron foundPatron = new PatronService().loadPatron(holding);

        foundPatron.remove(holding);

        if (isLate(holding))
            applyFine(holding, foundPatron);
        return holding.daysLate();
    }

    private Holding loadHolding(String barCode) {
        Holding holding = find(barCode);
        if (holding == null)
            throw new HoldingNotFoundException();
        return holding;
    }

    private Branch loadBranch(String branchScanCode) {
        return new BranchService().find(branchScanCode);
    }

    private void applyFine(Holding hld, Patron foundPatron) {
        int daysLate = hld.daysLate();
        int fineBasis = hld.getMaterial().getFormat().getDailyFine();
        switch (hld.getMaterial().getFormat()) {
            case Book:
                foundPatron.addFine(fineBasis * daysLate);
                break;
            case DVD:
                int fine = Math.min(1000, 100 + fineBasis * daysLate);
                foundPatron.addFine(fine);
                break;
            case NewReleaseDVD:
                foundPatron.addFine(fineBasis * daysLate);
                break;
        }
    }

    private void adjustForLastDayOfYear(Calendar c, Date dateDue) {
        c.setTime(dateDue);
        int d = Calendar.DAY_OF_YEAR;
        if (c.get(d) > c.getActualMaximum(d)) {
            c.set(d, 1);
            c.set(Calendar.YEAR, c.get(Calendar.YEAR) + 1);
        }
    }

    private boolean isLate(Holding hld) {
        Calendar calendar = Calendar.getInstance();
        adjustForLastDayOfYear(calendar, hld.dateDue());
        return hld.dateLastCheckedIn().after(calendar.getTime());
    }

}