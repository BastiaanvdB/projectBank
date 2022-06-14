Feature: the account controller works

  Scenario: I can get an list of accounts
    Given I have valid jwt to get all accounts
    When I call endpoint to get all accounts
    Then I receive a status of success of 200
    And I get a list of accounts back