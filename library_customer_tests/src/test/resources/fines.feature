Feature: Fines
   As a librarian, 
   I want to assess fines for books returned late,
   So that we can encourage maximum book availability.

# reword the given 
Scenario: Due date for book is 21 days after checkout
   Given an available holding of type book
   When a patron checks out the holding on 2017/3/1
   Then the due date is 2017/3/22
   
#Scenario: Book incurs no fine when returned on due date
#   Given an available holding of type book
#   And Anastasius checks out the book on 2017/4/1
#   When the book is returned on 2017/4/22
#   Then the patron fine balance is 0