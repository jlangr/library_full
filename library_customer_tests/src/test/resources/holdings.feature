Feature: holdings
   As a librarian, 
   I want to track holdings
   
Background:
   Given a library system with a branch named "East"
   And a local classification service with:
      | source id | classification | format |
      | 123 | QA-8675309 | Book |
      | 999 | EF-3303 | Book |

 Scenario: Add holdings to the library system generates incremented barcodes
   When a libraran adds a book with source id 123 at branch "East"
   And a librarian adds a book with source id 123 at branch "East"
   Then the "East" branch contains the following holdings:
      | holding barcode | 
      | QA-8675309:1 |
      | QA-8675309:2 |