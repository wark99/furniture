Feature: Check if the update endpoint works

  Scenario: Update non existing sale
    Given that we have the following sales:
      | price | timestamp |
    Then I should not be able to update sale "1", "1", "25.9", "2022-12-11 10:34:23"

  Scenario: Update existing sale
    Given that we have the following sales:
      | price | timestamp           |
      | 25.0  | 2022-12-11 10:34:23 |
      | 139.9 | 2022-12-11 14:05:05 |
    Then I should be able to update sale "1", "1", "25.9", "2022-12-11 10:34:23"
