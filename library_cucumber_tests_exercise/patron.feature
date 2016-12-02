Feature: Patrons
   As a librarian, 
   I want to add new patrons,
   So that we increase circulation in the library
   
Background:
   Given a clean library system

Scenario: Add new patron
   Given a librarian adds a patron named Anastasius 
   When a librarian requests a list of all patrons
   Then the client shows the following patrons:
   | name | fine balance |
   | Anastasius | 0 |
