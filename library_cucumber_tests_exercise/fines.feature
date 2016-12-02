Feature: Fines
   As a ..., 
   
Scenario: Due date for book is 21 days after checkout
   Given a clean library system
   And a branch named "Rockrimmon" with the following holdings: Catch-22, The Trial
   When a patron checks out "Catch-22" on 2017/3/1
   Then the due date for "Catch-22" is 2017/3/22
   
Scenario: Patron incurs no fine when returned on due date
   Given a clean library system
   And a branch named "Rockrimmon" with the following holdings: Catch-22, The Trial
   And a patron checks out "Catch-22" on 2017/4/1
   When "Catch-22" is returned on 2017/4/22
   Then the patron's fine balance is 0
   
Scenario Outline: Book incurs fine when returned after due date
   Given a clean library system
   And a branch named "Rockrimmon" with the following holdings: Catch-22, The Trial
   And a patron checks out "The Trial" on <checkoutDate>
   When "The Trial" is returned on <checkinDate>
   When the patron's fine balance is <expectedBalance>

   Examples:
   | checkoutDate    | checkinDate  | expectedBalance |
   | 2017/05/01      | 2017/05/23   | 10 |
   | 2017/05/01      | 2017/05/24   | 20 |
   | 2017/05/01      | 2017/05/25   | 30 |

Scenario: Late book fine bal
   Given a clean library system
   And a branch named "Rockrimmon" with the following holdings: Catch-22, The Trial
   And a patron checks out "Catch-22" on 2017/5/1
   When "Catch-22" is returned on 2017/5/25
   Then the patron's fine balance is 30

Scenario: incurs no fine when movie returned on due date
   Given a clean library system
   And a branch named "Rockrimmon" with the movie "Blade Runner"
   And a patron checks out "Blade Runner" on 2017/4/1
   When "Blade Runner" is returned on 2017/4/8
   Then the patron's fine balance is 100

Scenario: incurs fine when movie returned late
   Given a clean library system
   And a branch named "Rockrimmon" with the movie "Blade Runner"
   And a patron checks out "Blade Runner" on 2017/4/1
   When "Blade Runner" is returned on 2017/4/9
   Then the patron's fine balance is 100

Scenario: incurs fine when movie returned later
   Given a clean library system
   And a branch named "Rockrimmon" with the movie "Blade Runner"
   And a patron checks out "Blade Runner" on 2017/4/1
   When "Blade Runner" is returned on 2017/4/10
   Then the patron's fine balance is 200

Scenario: fine movie even later
   Given a clean library system
   And a branch named "Rockrimmon" with the movie "Blade Runner"
   And a patron checks out "Blade Runner" on 2017/4/1
   When "Blade Runner" is returned on 2017/4/15
   Then the patron's fine balance is 700

Scenario: Fine can be cleared
   Given a clean library system
   And a branch named "Rockrimmon" with the movie "Blade Runner"
   And a patron named Sundeep
   And Sundeep has a fine balance of 20
   When the library waives the fine balance for Sundeep
   Then Sundeep has a fine balance of 0

Scenario: Patron pays off part of fine
   Given a clean library system
   And a branch named "Rockrimmon" with the movie "Blade Runner"
   And a patron named Sundeep
   And Sundeep has a fine balance of 40
   When Sundeep pays 25 toward the fine balance
   Then Sundeep has a fine balance of 15
