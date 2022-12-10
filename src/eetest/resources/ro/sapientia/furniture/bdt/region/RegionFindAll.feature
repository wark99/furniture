Feature: Check if the find all endpoint works

  Scenario: No element
    Given that we have the following regions:
      | name |
    Then I should get "RECORD_NOT_FOUND" error for find all

  Scenario: Multiple element
    Given that we have the following regions:
      | name      |
      | Vakanda   |
      | Uganda    |
      | Andalusia |
    Then I should get the name "Vakanda" for the position "0"
    Then I should get the name "Uganda" for the position "1"
    Then I should get the name "Andalusia" for the position "2"