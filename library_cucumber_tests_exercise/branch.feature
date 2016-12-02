Feature: Branch

Scenario: Add 
   Given a clean library system
   Given a librarian adds a branch named "East"
   And they add a branch named "West"
   When a user requests a list of all branches
   Then the system returns the following branches:
      | name |
      | East |
      | West |
      
 Scenario: Attempt to add branch with duplicate name
   Given a clean library system
    # TODO: flesh out!
