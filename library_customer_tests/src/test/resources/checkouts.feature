Feature: checkouts and checkins
   As a patron, 
   I want to ...
   ? phrasing on voice 1st person

Background:
   Given a clean library system
   And a local classification service with:
      | source id | classification | format |
      | 123 | QA-8675309 | Book |
      | 999 | EF-3303 | Book |
   And a branch named "Rockrimmon" with the following holdings:
   | source id | title |
   | 123 | Catch-22 |
   | 999 | 1984 |

Scenario: Errors when attempting to check out book twice
   Given a patron checks out Catch-22 on 2017/3/1
   When a patron checks out Catch-22 on 2017/3/2
   Then the client is informed of a conflict

Scenario: Checked out book is not available
   When a patron checks out 1984 on 2017/5/31 
   Then 1984 is not available
   
Scenario: Checked out book added to patron

Scenario: Book is available after checkin
   Given a patron checks out Catch-22 on 2017/4/15
   When Catch-22 is returned on 2017/4/16 to "Rockrimmon"
   Then Catch-22 is available