Feature: checkouts and checkins
   As a librarian, 
   I want to track holdings
   
Background:
   Given a library system with a branch named "East"

Scenario: Errors when attempting to check out book twice
   Given an available book
   And a patron checks out the book on 2017/3/1
   When a patron checks out the book on 2017/3/2
   Then the client is informed of a conflict