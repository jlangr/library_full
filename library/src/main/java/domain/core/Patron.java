package domain.core;

import java.util.Date;
import static util.DateUtil.*;

public class Patron {
   private final String name;
   private String id;
   private HoldingMap s = new HoldingMap();
   private int bal = 0;
   private Date birthDate;

   public Patron(String id, String name) {
      this.name = name;
      this.id = id;
   }

   public Patron(String name) {
      this("", name);
   }

   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getName() {
      return name;
   }

   public Date getBirthDate() {
      return birthDate;
   }

   public void setBirthDate(Date birthDate) {
      this.birthDate = birthDate;
   }

   @Override
   public String toString() {
      return "[" + getId() + "] " + getName() + " (" + getBirthDate() + ")";
   }

   @Override
   public boolean equals(Object object) {
      if (object == null)
         return false;
      if ((object.getClass() != this.getClass()))
         return false;
      Patron that = (Patron)object;
      return this.getId().equals(that.getId());
   }

   public HoldingMap holdingMap() {
      return s;
   }

   public void add(Holding holding) {
      s.add(holding);
   }

   public void remove(Holding holding) {
      s.remove(holding);
   }

   public int fineBalance() {
      return bal;
   }

   /**
    * add a fine to the patron's balance
    *
    * @param a
    *           the amount to add to the balance
    */
   public void addFine(int a) {
      bal += a;
   }

   /**
    * add a fine to the patron's balance
    *
    * @param a
    *           the amount to add to the balance
    */
   public void remit(int a) {
      bal -= a;
   }

   public int getAge() {
      return ageInYears(toLocalDate(getBirthDate()), getCurrentLocalDate());
   }
}
