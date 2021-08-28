Feature: Create student

  @wip
  Scenario: Create student a teacher and verify status code 201
    Given I logged Bookit api using "teacherilsamnickel@gmail.com" and "samnickel"
    When I send POST request to "/api/students/student" endpoint with following information
      | first-name      | harold              |
      | last-name       | finch               |
      | email           | harold5@gmail.com   |
      | password        | abc123              |
      | role            | student-team-leader |
      | campus-location | VA                  |
      | batch-number    | 8                   |
      | team-name       | Nukes               |
    Then status code should be 201
    And I delete previously added student


