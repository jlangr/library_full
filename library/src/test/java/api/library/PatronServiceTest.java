package api.library;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import java.util.Date;
import org.junit.*;
import domain.core.Patron;
import persistence.PatronStore;

public class PatronServiceTest {
   PatronService service;

   @Before
   public void initialize() {
      PatronStore.deleteAll();
      service = new PatronService();
   }

   @Test
   public void answersGeneratedId() {
      String id = service.add("name");

      assertThat(id, startsWith("p"));
   }

   @Test
   public void allowsAddingPatronWithId() {
      service.add("p123", "xyz");

      Patron patron = service.find("p123");

      assertThat(patron.getName(), equalTo("xyz"));
   }

   @Test
   public void defaultsBirthDateToNull() {
      service.add("p123", "xyz");

      Patron patron = service.find("p123");

      assertThat(patron.getBirthDate(), is(nullValue()));
   }

   @Test
   public void allowsSpecifyingBirthDate() {
      Date birthDate = new Date();
      String id = service.add("xyz", birthDate);

      Patron patron = service.find(id);

      assertThat(patron.getBirthDate(), equalTo(birthDate));
   }

   @Test(expected=InvalidPatronIdException.class)
   public void rejectsPatronIdNotStartingWithP() {
      service.add("234", "");
   }

   @Test(expected=DuplicatePatronException.class)
   public void rejectsAddOfDuplicatePatron() {
      service.add("p556", "");
      service.add("p556", "");
   }

   @Test
   public void answersNullWhenPatronNotFound() {
      assertThat(service.find("nonexistent id"), nullValue());
   }
}
