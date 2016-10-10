Feature: Fines
   As a librarian, 
   I want to assess fines for books returned late,
   So that we can encourage maximum book availability.
   
Background:
   Given a library system with a branch named "East"
   And a local classification service with:
      | source id | classification | format |
      | 123       | QA-8675309     | Book   |
   And a book with source id 123 is added at branch "East"

Scenario: Due date for book is 21 days after checkout
   When a patron checks out the book on 2017/3/1
   Then the due date is 2017/3/22
   
Scenario: Book incurs no fine when returned on due date
   Given a patron checks out the book on 2017/4/1
   When the book is returned on 2017/4/22
   Then the patron's fine balance is 0
   
Scenario: Book incurs fine when returned after due date
   Given a patron checks out the book on 2017/5/1
   When the book is returned on 2017/5/23
   Then the patron's fine balance is 10

Scenario: Late book fine balance is multiple of days
   Given a patron checks out the book on 2017/5/1
   When the book is returned on 2017/5/25
   Then the patron's fine balance is 30