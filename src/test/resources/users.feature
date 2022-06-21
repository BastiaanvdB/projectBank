Feature: the user controller works

  Scenario: Post request to create new user
    Given I have valid jwt to create new user
    When I call endpoint to create new user
    Then I receive http code 201 created new user
    And I get newly created user

  Scenario: Get request to get all users
    Given I have valid jwt to get all users
    When I call endpoint to get all users
    Then I receive http code 200 for get all users
    And I get list with all users

  Scenario: Get request to get one user
    Given I have valid jwt to get one user
    When I call endpoint to get one user
    Then I receive http code 200 for get one user
    And I get one user

  Scenario: Put request to set the password of user
    Given I have valid jwt to set the password of user
    When I call endpoint to set password of user
    Then I receive http code 202 for setting password

  Scenario: Put request to set the role of user
    Given I have valid jwt to set the role of user
    When I call endpoint to set role of user
    Then I receive http code 202 for setting role

  Scenario: Put request to set the status of user
    Given I have valid jwt to set the status of user
    When I call endpoint to set status of user
    Then I receive http code 202 for setting status

  Scenario: Put request to update user
    Given I have valid jwt to update user
    When I call endpoint to update user
    Then I receive http code 200 for updating user

  Scenario: Post request to login success
    Given I have valid credentials to log in
    When I call endpoint to log into the system
    Then I receive http code 200 for loggin into the system
    And I receive valid jwt token

  Scenario: Post request to login unsuccessful
    Given I have invalid credentials to log in
    When I call endpoint to log into the system with invalid credentials
    Then I receive http code 401 not authorized to log in