Feature: checkouts and checkins
   As a patron, 
   I want to ...
   ? phrasing on voice 1st person

Background:
   Given a library system with one book

Scenario: Errors when attempting to check out book twice
   Given a patron checks out the book on 2017/3/1
   When a patron checks out the book on 2017/3/2
   Then the client is informed of a conflict
   
Scenario: Book is available after checkin
   Given a patron checks out the book on 2017/4/15
   When the book is returned on 2017/4/16
   Then the book is available

#Scenario: Checked out book added to patron
#   When a patron checks out the book
#   Then the patron has the book
   