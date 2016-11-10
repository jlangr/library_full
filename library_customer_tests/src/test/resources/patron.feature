Feature: Patrons
   As a librarian, 
   I want to add new patrons,
   So that we increase circulation in the library
   
Background:
   Given a clean library system

Scenario: Add new patron
   Given a librarian adds a patron named Anastasius with a birthdate of 1980/10/01
   When a librarian requests a list of all patrons
   Then the client shows the following patrons:
   | name       | fine balance | birth date  |
   | Anastasius | 0            | 1980/10/01  | 
   
@ignore
Scenario: Calculate patron age
   Given a librarian adds a patron with a birthday of 1980/10/01
   When today's date is 2017/10/2
   Then the patron is 37 years old