Feature: Branch

Scenario: Add a branch
Given an administrator adds a branch with name "East"
And they add a branch with name "West"
When a user requests a list of all branches
Then the system returns the following branches:
   | name |
   | East |
   | West |