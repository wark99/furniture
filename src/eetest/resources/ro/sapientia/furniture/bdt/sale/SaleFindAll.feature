Feature: Check if the find all endpoint works

  Scenario: There are no sales
    Given that we have the following sales:
      | price | timestamp |
    Then I should get "0" items

  Scenario: There are two sales
    Given that we have the following sales:
      | price | timestamp           |
      | 25.0  | 2022-12-11 10:34:23 |
      | 139.9 | 2022-12-11 14:05:05 |
    Then I should get "2" items
    Then I should get the price "25.0" for the position "0"
    Then I should get the price "139.9" for the position "1"
