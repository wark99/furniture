Feature: Check if the find by id endpoint works

  Scenario: No element
    Given that we have the following regions:
      | name |
    Then I should get "RECORD_NOT_FOUND" error for find by "3"

  Scenario: Multiple element
    Given that we have the following regions:
      | name      |
      | Vakanda   |
      | Uganda    |
      | Andalusia |
    Then I should get the name "Vakanda" for find by "1"
    Then I should get the name "Uganda" for find by "2"
    Then I should get the name "Andalusia" for find by "3"