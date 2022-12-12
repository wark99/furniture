Feature: Check if the find by id endpoint works

  Scenario: Get non existing sale
    Given that we have the following sales:
      | price | timestamp |
    Then I should get the response code "404" for find by "3"

  Scenario: Get existing sale
    Given that we have the following sales:
      | price | timestamp           |
      | 25.0  | 2022-12-11 10:34:23 |
      | 139.9 | 2022-12-11 14:05:05 |
    Then I should get the response code "200" for find by "1"
    Then I should get the price "25.0" for find by "1"
    Then I should get the response code "200" for find by "2"
    Then I should get the price "139.9" for find by "2"
