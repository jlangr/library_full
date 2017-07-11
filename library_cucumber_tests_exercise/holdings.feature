Feature: holdings
   As a ..., 
   
 Scenario: Add holdings to the library system generates incremented barcodes
   Given a library system with a branch named "East"
   And a library system with a branch named "West"
   When a local classification service with:
      | source id | classification | format |
      | 123 | QA-111 | Book |
      | 999 | EF-333 | Book |
   Then a librarian adds a book holding with source id 123 at branch "East"
   And a librarian adds a book holding with source id 123 at branch "East"
   Then the "East" branch contains the following holdings:
      | barcode | 
      | QA-111:1 |
      | QA-111:2 |

Scenario: Transfer by librarian
   Given a library system with a branch named "East"
   And a library system with a branch named "West"
   And a holding with barcode QA-111:1 at branch "East"
   When a librarian clicks on the Transfer menu item
   And the librarian sets the holding classification field to QA-111:1
   And the librarian sets the branch to "West"
   And the librarian submits the page
   And the librarian clicks on the Holdings by Branch menu item
   And the librarian selects "West" from the branch names dropdown
   Then the branch holdings list contains the holding QA-111:1
 
 Scenario: Check in to other branch 
   Given a library system with a branch named "East"
   And a library system with a branch named "West"
   And a librarian adds a book holding with barcode QA-123:1 at branch "East"
   And the library branch "East" is open for business
   And a patron at branch "East" scans the holding with barcode QA-123:1 
   And the patron completes checkout by pressing Complete
   When a librarian switches the scanner at branch "West" to drop-off mode
   And the library scans the dropped-off book with barcode QA-123:1
   And the library presses the Complete button
   When a patron retrieves holding details for barcode Qa-123:1
   Then QA-123:1 shows as being held at the branch named "West"
   And the due date for QA-123:1 is unspecified
   And QA-123:1 is marked as available
