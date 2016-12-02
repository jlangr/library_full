Feature: checkouts and checkins
   As a... 

Scenario: Errors when attempting to check out book twice
   Given a clean library system
   And a branch named "Rockrimmon" with the following holdings: Catch-22, 1984
   And a patron checks out "Catch-22" on 2017/3/1
   When a patron checks out "Catch-22" on 2017/3/2
   Then the client is informed of a conflict

Scenario: Checked out book is not available
   Given a clean library system
   And a branch named "Rockrimmon" with the following holdings: Catch-22, 1984
   When a patron checks out "1984" on 2017/5/31 
   Then "1984" is not available
   
Scenario: Checked out book added to patron
    # TODO

Scenario: Checkin -> book avail.
   Given a clean library system
   And a branch named "Rockrimmon" with the following holdings: Catch-22, 1984
   And a patron checks out "Catch-22" on 2017/4/15
   When "Catch-22" is returned on 2017/4/16 to "Rockrimmon"
   Then "Catch-22" is available
