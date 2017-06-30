package api.library;

import domain.core.Holding;
import domain.core.HoldingMap;
import domain.core.Patron;
import persistence.PatronStore;

import java.util.Collection;

public class PatronService {
    public PatronStore patronAccess = new PatronStore();
    private CreditVerifier verifier;

    public String add(String name) {
        return save(new Patron(name));
    }

    public String add(String id, String name) {
        return add(id, name, "");
    }

    public String add(String id, String name, String creditCard) {
        if (!id.startsWith("p")) throw new InvalidPatronIdException();
        if (isValidCredit(creditCard))
            return save(new Patron(id, name));
        return "";
    }

    private boolean isValidCredit(String creditCard) {
        boolean valid = true;
        try {
            valid = verifier.isValid(creditCard);
        }
        catch (RuntimeException e) {
        }
        return valid;
    }

    private String save(Patron newPatron) {
        if (patronAccess.find(newPatron.getId()) != null)
            throw new DuplicatePatronException();
        patronAccess.add(newPatron);
        return newPatron.getId();
    }

    public Patron find(String id) {
        return patronAccess.find(id);
    }

    public Collection<Patron> allPatrons() {
        return patronAccess.getAll();
    }

    public Patron loadPatron(Holding holding) {
        // could introduce a patron reference ID in the loadHolding...
        Patron foundPatron = null;
        for (Patron patron : allPatrons()) {
            HoldingMap holdings = patron.holdingMap();
            for (Holding patHld : holdings) {
                if (holding.getBarcode().equals(patHld.getBarcode()))
                    foundPatron = patron;
            }
        }
        return foundPatron;
    }
}
