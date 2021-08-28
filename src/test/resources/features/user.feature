@smoke
Feature: User Verification


  Scenario: verify information about logged user
    Given I logged Bookit api using "sbirdbj@fc2.com" and "asenorval"
    When I get the current user information from api
    Then status code should be 200

   @db
  Scenario: verify information about logged user from api and database
    Given I logged Bookit api using "sbirdbj@fc2.com" and "asenorval"
    When I get the current user information from api
    Then the information about current user from api and database should match
  @wip @db
  Scenario: three point verification (UI,API,Database)
    Given user logs in using "sbirdbj@fc2.com" "asenorval"
    And  user is on the my self page
    Given I logged Bookit api using "sbirdbj@fc2.com" and "asenorval"
    When I get the current user information from api
    Then UI,API and Database user information must be match
