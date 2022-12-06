Feature: Check if the add endpoint works

  Scenario: No element
    Given that we have the following regions:
      | name   |
      | Kuwuki |
    Then I should get no error for adding "3" "1" "Uganda2 Country" "Uganda2 County" "Uganda2 city" "82th" "702B" "123222"

  Scenario: Multiple element
    Given that we have the following service points:
      | regionName | country         | county         | city         | street | number | zipCode |
      | Vakanda    | Vakanda Country | Vakanda County | Vakanda city | 7th    | 69A    | 445566  |
      | Uganda     | Uganda Country  | Uganda County  | Uganda city  | 8th    | 70B    | 123212  |
    Then I should get no error for adding "3" "1" "Uganda2 Country" "Uganda2 County" "Uganda2 city" "82th" "702B" "123222"
