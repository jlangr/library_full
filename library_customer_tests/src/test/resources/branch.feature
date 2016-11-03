Feature: Branch

Background:
   Given a clean library system

Scenario: Add a branch
   Given a librarian adds a branch named "East"
   And they add a branch named "West"
   When a user requests a list of all branches
   Then the system returns the following branches:
      | name |
      | East |
      | West |
      
 # TODO
 Scenario: Attempt to add branch with duplicate name