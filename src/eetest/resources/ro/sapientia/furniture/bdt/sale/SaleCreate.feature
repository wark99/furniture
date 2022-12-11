Feature: Check if the create endpoint works

  Scenario: Create sale when there are no other sales
    Given that we have the following sales:
      | price | timestamp |
    Then I should be able to create sale "1", "23.9", "2022-12-11 21:45:23"

  Scenario: Create sale when there are other sales
    Given that we have the following sales:
      | price | timestamp           |
      | 25.0  | 2022-12-11 10:34:23 |
      | 139.9 | 2022-12-11 14:05:05 |
    Then I should be able to create sale "1", "23.9", "2022-12-11 21:45:23"
