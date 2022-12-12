Feature: Check if the delete by id endpoint works

  Scenario: No element
    Given that we have the following used materials:
      | quantity |
    Then I should get "RECORD_NOT_FOUND" error for deleting used material by id "33"

  Scenario: Multiple element
    Given that we have the following used materials:
      | regionName    | servicePointCityName | materialName | furnitureId | quantity  | price |
      | Transylvania  | Tg. Mures            | Pine         | 1           | 1         | 10.0  |
      | Hungary       | Budapest             | Oak          | 1           | 1         | 11.0  |
    Then I should succeed in deleting used material by id "1"