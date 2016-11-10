package api.library;

import persistence.PatronStore;
import java.util.*;
import domain.core.Patron;

public class PatronService {
   public PatronStore patronAccess = new PatronStore();

   public String add(String name) {
      return save(new Patron(name));
   }

   public String add(String name, Date birthDate) {
      Patron patron = new Patron(name);
      patron.setBirthDate(birthDate);
      return save(patron);
   }

   public String add(String id, String name) {
      if (!id.startsWith("p")) throw new InvalidPatronIdException();
      Patron patron = new Patron(id, name);
      return save(patron);
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
}
