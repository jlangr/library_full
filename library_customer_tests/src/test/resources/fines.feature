Feature: Fines
   As a librarian, 
   I want to assess fines for books returned late,
   So that we can encourage maximum book availability.
   
Background:
# TODO put this shit in a common place?
   Given a clean library system
   And a local classification service with:
      | source id | classification | format | title |
      | 123 | QA-8675309 | Book | Catch-22 |
      | 999 | EF-3303 | Book | 1984 |
   And a branch named "Rockrimmon" with the following holdings:
   | source id | title |
   | 123 | Catch-22 |
   | 999 | 1984 |
   
# TODO: add holdings table above should just be the titles of the books!
# So: the classification service can be added behind the scenes, using a generated source ID for each
# book listed. The title is part of the Material. So on add of the book, the material may need to be
# retrieved so that the title can be used as the key to store the new holding's classification.
# TODO: books should be strings not single-word titles

Scenario: Due date for book is 21 days after checkout
   When a patron checks out Catch-22 on 2017/3/1
   Then the due date for Catch-22 is 2017/3/22
   
Scenario: Book incurs no fine when returned on due date
   Given a patron checks out Catch-22 on 2017/4/1
   When Catch-22 is returned on 2017/4/22
   Then the patron's fine balance is 0
   
Scenario: Book incurs fine when returned after due date
   Given a patron checks out Catch-22 on 2017/5/1
   When Catch-22 is returned on 2017/5/23
   Then the patron's fine balance is 10

Scenario: Late book fine balance is multiple of days
   Given a patron checks out Catch-22 on 2017/5/1
   When Catch-22 is returned on 2017/5/25
   Then the patron's fine balance is 30