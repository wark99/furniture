Feature: Check if the delete by id endpoint works

  Scenario: No element
    Given that we have the following regions:
      | name |
    Then I should get "RECORD_NOT_FOUND" error for deleting element at position "69"

  Scenario: Multiple element
    Given that we have the following regions:
      | name      |
      | Vakanda   |
      | Uganda    |
      | Andalusia |
    Then I should get no error for deleting element at position "3"