Feature: Check if the find by id endpoint works

  Scenario: No element
    Given that we have the following service points:
      | name |
    Then I should get "RECORD_NOT_FOUND" error for find service point by id "70"

  Scenario: Multiple element
    Given that we have the following service points:
      | regionName | country         | county         | city         | street | number | zipCode |
      | Vakanda    | Vakanda Country | Vakanda County | Vakanda city | 7th    | 69A    | 445566  |
      | Uganda     | Uganda Country  | Uganda County  | Uganda city  | 8th    | 70B    | 123212  |
    Then I should get "Uganda" "Uganda Country" "Uganda County" "Uganda city" "8th" "70B" "123212" for find service point by id "2"