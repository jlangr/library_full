package api.library;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import org.junit.*;
import persistence.*;
import domain.core.*;

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
