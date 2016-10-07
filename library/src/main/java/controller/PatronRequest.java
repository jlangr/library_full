package controller;

import domain.core.Patron;

// TODO serializable?
public class PatronRequest {
   private String name;
   private String id;
   private Integer fineBalance;

   public PatronRequest() {
   }

   public PatronRequest(Patron patron) {
      this.id = patron.getId();
      this.name = patron.getName();
      this.fineBalance = patron.fineBalance();
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getName() {
      return name;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getId() {
      return id;
   }

   // TODO does it have to be Integer?
   public void setFineBalance(Integer fineBalance) { this.fineBalance = fineBalance; }
   public Integer getFineBalance() { return fineBalance; }

   @Override
   public String toString() {
      return "[Patron:] " + name + " (" + id + ")";
   }
}
