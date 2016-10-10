Feature: checkouts and checkins
   As a librarian, 
   I want to track holdings
   
Background:
   Given a library system with a branch named "East"
   And a local classification service with:
      | source id | classification | format |
      | 123 | QA-8675309 | Book |

Scenario: Errors when attempting to check out book twice
   Given an available book
   And a patron checks out the book on 2017/3/1
   When a patron checks out the book on 2017/3/2
   Then the client is informed of a conflict

 Scenario: Add holdings to the library system generates incremented barcodes
   When a libraran adds a book with source id 123 at branch "East"
   And a librarian adds a book with source id 123 at branch "East"
   Then the "East" branch contains the following holdings:
      | holding barcode | 
      | QA-8675309:1 |
      | QA-8675309:2 |
   
#Scenario: Checked out book added to patron
#   Given an available book named "The Trial"
#   When a patron checks out the book
#   Then 
   