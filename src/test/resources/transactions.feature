Feature: the transaction controller works

#1
  Scenario: Post request to create a new transaction
    Given I have valid jwt to create new transaction
    When I call endpoint with post request to create new transaction
    Then I will receive http code 201 created
    And I receive newly created transaction

#2
  Scenario: Get request to get all transactions from current user
    Given I have valid jwt for user with a account
    When I call endpoint with get request to get all transactions from current user
    Then I receive http code 200 ok
    And I receive list with transactions

#3
  Scenario: Can't transfer money from a savings account to a savings account that not belongs to the same user
    Given I have valid jwt for user with a savings account
    When I do a transfer from my savings account to another savings account that not belongs to me
    Then I receive http code 400 This account does not belong to U

#4
  Scenario: Can't transfer from a savings account to current account that not belongs to the same user
    Given I have valid jwt for user with a savings account
    When I do a transfer from my savings account to another account that not belongs to me
    Then I receive http code 400 This account does not belong to U

#5
  Scenario: At the ATM you can deposit money to your account
    Given I have valid jwt for user with a account
    When I call this endpoint with post request to deposit money on my account
    Then I will receive http code 201 created
    And I receive the deposit transaction

#6
  Scenario: At the ATM you can withdraw money from your account
    Given I have valid jwt for user with a account
    When I call this endpoint with post request to withdraw money from my account
    Then I will receive http code 201 created
    And I receive the withdraw transaction

#7
  Scenario: The account balance is not able to exceed the absolute limit
    Given I have valid jwt for user with a account
    When I do a transaction that exceeds the absolute limit of the account
    Then  I receive http code 400 Not enough money on this account

#8
  Scenario: An User is not able to exceed the day limit
    Given I have valid jwt for user with a account
    When I do a transaction that exceeds the day limit of the user
    Then I receive http code 400 With this transaction day limit will be exceeded

#9
  Scenario: An User is not able to exceed the transaction limit
    Given I have valid jwt for user with a account
    When I do a transaction that exceeds the transaction limit of the user
    Then I receive http code 400 This transaction exceeds the transaction limit

#10
  Scenario: Transactions could be found with parameters
    Given I have valid jwt for user with a account
    When I call endpoint to get specific transactions with parameters in url
    Then I receive http code 200 ok
    And I receive list with transactions