Feature: Check if the delete endpoint works

  Scenario: Delete existing sale
    Given that we have the following sales:
      | price | timestamp           |
      | 25.0  | 2022-12-11 10:34:23 |
      | 139.9 | 2022-12-11 14:05:05 |
    Then I should be able to delete sale by id "1"