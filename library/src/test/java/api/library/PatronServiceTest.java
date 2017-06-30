package api.library;

import com.loc.material.api.Material;
import domain.core.Holding;
import domain.core.Patron;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import persistence.PatronStore;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PatronServiceTest {
    @InjectMocks
    PatronService service;
    @Mock
    private CreditVerifier verifier;

    @Before
    public void initialize() {
        PatronStore.deleteAll();
    }

    @Test
    public void answersGeneratedId() {
        String scanCode = service.add("name");

        assertThat(scanCode, startsWith("p"));
    }

    @Test
    public void allowsAddingPatronWithId() {
        when(verifier.isValid(anyString())).thenReturn(true);
        service.add("p123", "xyz", "goodCC");

        Patron patron = service.find("p123");

        assertThat(patron.getName(), equalTo("xyz"));
    }

    @Test
    public void rejectsAddingPatronWithBadCredit() {
        when(verifier.isValid("badCC")).thenReturn(false);

        service.add("p222", "", "badCC");

        assertThat(service.allPatrons(), is(empty()));
    }

    @Test
    public void addsPatronIfVerifierThrowsException() {
        when(verifier.isValid(anyString())).thenThrow(new RuntimeException());

        service.add("p123", "", "");

        assertThat(service.allPatrons().size(), is(1));
    }
/*
    rejectsAddingPatronWithBadCredit
         create mockito stub --> always returns false
                          when asked "hasGoodCredit" with a CC #
         inject the stub

         attempt to add, with credit card number that is "bad"

         retrieve all and assert that it's empty
         */

    public PatronService getService() {
        return service;
    }

    @Test(expected = InvalidPatronIdException.class)
    public void rejectsPatronIdNotStartingWithP() {
        service.add("234", "");
    }

    @Test(expected = DuplicatePatronException.class)
    public void rejectsAddOfDuplicatePatron() {
        when(verifier.isValid(anyString())).thenReturn(true);
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
