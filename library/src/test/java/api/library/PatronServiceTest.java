package api.library;

import com.loc.material.api.Material;
import domain.core.Holding;
import domain.core.Patron;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import persistence.PatronStore;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class PatronServiceTest {
    PatronService service;

    @Before
    public void initialize() {
        PatronStore.deleteAll();
        service = new PatronService();
    }

    @Test
    public void answersGeneratedId() {
        String scanCode = service.add("name");

        assertThat(scanCode, startsWith("p"));
    }

    @Test
    public void allowsAddingPatronWithId() {
        service.add("p123", "xyz");

        Patron patron = service.find("p123");

        assertThat(patron.getName(), equalTo("xyz"));
    }

    @Test(expected = InvalidPatronIdException.class)
    public void rejectsPatronIdNotStartingWithP() {
        service.add("234", "");
    }

    @Test(expected = DuplicatePatronException.class)
    public void rejectsAddOfDuplicatePatron() {
        service.add("p556", "");
        service.add("p556", "");
    }

    @Test
    public void answersNullWhenPatronNotFound() {
        assertThat(service.find("nonexistent id"), nullValue());
    }

    @Test
    public void loadPatronReturnsEmptyWhenNoPatronsExist() {
        Holding holding = new Holding(new Material());

        Patron result = service.loadPatron(holding);

        assertThat(result, is(nullValue()));
    }

    @Test
    public void loadPatronReturnsPatronWithMatchingHolding() {
        Material material = new Material();
        material.setClassification("ABC");
        Holding holding = new Holding(material);
//        Patron patron = new Patron("joe");
        String id = service.add("joe");
        Patron patron = service.find(id);
        service.patronAccess.addHoldingToPatron(patron, holding);

        Patron result = service.loadPatron(holding);

        assertThat(result.getName(), is(equalTo("joe")));
    }
}
