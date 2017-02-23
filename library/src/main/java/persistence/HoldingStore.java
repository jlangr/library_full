package persistence;

import domain.core.Holding;
import util.MultiMap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HoldingStore implements Iterable<Holding> {
    private static MultiMap<String, Holding> holdings = new MultiMap<String, Holding>();

    public static void deleteAll() {
        holdings.clear();
    }

    public void save(Holding holding) {
        holdings.put(holding.getMaterial().getClassification(), copy(holding));
    }

    private Holding copy(Holding holding) {
        return new Holding(holding.getMaterial(), holding.getBranch(),
                holding.getCopyNumber());
    }

    public List<Holding> findByClassification(String classification) {
        List<Holding> results = holdings.get(classification);
        if (results == null)
            return new ArrayList<Holding>();
        return results;
    }

    public Holding findByBarcode(String barCode) {
        List<Holding> results = holdings.get(classificationFrom(barCode));
        if (results == null)
            return null;
        for (Holding holding : results)
            if (holding.getBarcode().equals(barCode))
                return holding;
        return null;
    }

    private String classificationFrom(String barCode) {
        int index = barCode.indexOf(Holding.BARCODE_SEPARATOR);
        return barCode.substring(0, index);
    }

    public int size() {
        return holdings.size();
    }

    @Override
    public Iterator<Holding> iterator() {
        return holdings.values().iterator();
    }

    public List<Holding> findByBranch(String branchScanCode) {
        List<Holding> results = new ArrayList<>();
        for (Holding holding : this)
            if (holding.getBranch().getScanCode().equals(branchScanCode))
                results.add(holding);
        return results;
    }
}
