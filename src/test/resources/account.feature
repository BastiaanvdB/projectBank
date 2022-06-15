Feature: the account controller works

  Scenario: Get request to get all accounts
    Given I have valid jwt to get all accounts
    When I call endpoint to get all accounts
    Then I receive http code 200 ok for all accounts
    And I get list with accounts

  Scenario: Post request to create a new account
    Given I have valid jwt to create new account
    When I call endpoint with post request to create new account
    Then I receive http code 201 created
    And I receive newly created account

  Scenario: Get request to get all accounts for specific user
    Given I have valid jwt to get all accounts for specific user
    When I call endpoint with get request to get all accounts for user
    Then I receive http code 200 ok for all accounts user
    And I receive list with accounts of user

  Scenario: Get request to get one account with specific iban
    Given I have valid jwt to get one account with specific iban
    When I call endpoint with get request to get one account
    Then I receive http code 200 ok for one account
    And I receive one account with the specified iban

  Scenario: Put request to update account status
    Given I have valid jwt to update status
    When I call endpoint with put request to update status
    Then I receive http code 200 ok for updated account
    Then I receive the updated account

  Scenario:  Get request to get all accounts without valid jwt
    Given I have valid jwt for user
    When I call endpoint to get all accounts without employee jwt
    Then I receive http code 500 bad request

