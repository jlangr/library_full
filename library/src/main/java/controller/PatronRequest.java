package controller;

import java.util.Date;
import domain.core.Patron;

public class PatronRequest {
   private String name;
   private String id;
   private int fineBalance;
   private Date birthDate;
   private int age;

   public PatronRequest() {
   }

   public PatronRequest(Patron patron) {
      this.id = patron.getId();
      this.name = patron.getName();
      this.birthDate = patron.getBirthDate();
      this.fineBalance = patron.fineBalance();
      this.age = patron.getAge();
   }

   public void setName(String name) { this.name = name; }
   public String getName() { return name; }

   public void setId(String id) { this.id = id; }
   public String getId() { return id; }

   public void setFineBalance(Integer fineBalance) { this.fineBalance = fineBalance; }
   public int getFineBalance() { return fineBalance; }

   public void setBirthDate(Date birthDate) { this.birthDate = birthDate; }
   public Date getBirthDate() { return birthDate; }

   public void setAge(int age) { this.age = age; }
   public int getAge() { return age; }
}
