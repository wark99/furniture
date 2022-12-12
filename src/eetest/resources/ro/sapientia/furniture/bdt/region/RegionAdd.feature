Feature: Check if the add endpoint works

  Scenario: No element
    Given that we have the following regions:
      | name |
    Then I should get no error for adding "Madagascar"

  Scenario: Multiple element
    Given that we have the following regions:
      | name      |
      | Vakanda   |
      | Uganda    |
      | Andalusia |
    Then I should get no error for adding "Madagascar"