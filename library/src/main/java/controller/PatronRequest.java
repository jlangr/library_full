package controller;

import domain.core.Patron;

public class PatronRequest {
   private String name;
   private String id;

   public PatronRequest() {
   }

   public PatronRequest(Patron patron) {
      this.id = patron.getId();
      this.name = patron.getName();
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

   @Override
   public String toString() {
      return "[Patron:] " + name + " (" + id + ")";
   }
}
