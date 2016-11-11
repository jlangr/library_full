Feature: Fines
   As a librarian, 
   I want to assess fines for books returned late,
   So that we can encourage maximum book availability.
   
Background:
   Given a clean library system
   And a branch named "Rockrimmon" with the following holdings: Catch-22, The Trial

Scenario: Due date for book is 21 days after checkout
   When a patron checks out "Catch-22" on 2017/3/1
   Then the due date for "Catch-22" is 2017/3/22
   
Scenario: Patron incurs no fine when returned on due date
   Given a patron checks out "Catch-22" on 2017/4/1
   When "Catch-22" is returned on 2017/4/22
   Then the patron's fine balance is 0
   
# NB: Outline must be uppercase!
Scenario Outline: Book incurs fine when returned after due date
   Given a patron checks out "The Trial" on <checkoutDate>
   When "The Trial" is returned on <checkinDate>
   Then the patron's fine balance is <expectedBalance>

   Examples:
   | checkoutDate    | checkinDate  | expectedBalance |
   | 2017/05/01      | 2017/05/23   | 10 |
   | 2017/05/01      | 2017/05/24   | 20 |
   | 2017/05/01      | 2017/05/25   | 30 |

Scenario: Late book fine balance is multiple of days
   Given a patron checks out "Catch-22" on 2017/5/1
   When "Catch-22" is returned on 2017/5/25
   Then the patron's fine balance is 30