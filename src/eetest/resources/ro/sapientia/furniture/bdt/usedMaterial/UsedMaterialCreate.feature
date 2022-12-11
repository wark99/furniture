Feature: Check if the create endpoint works

  Scenario: Multiple element
    Given that we have the following used materials:
      | regionName    | servicePointCityName | materialName | furnitureId | quantity  | price |
      | Transylvania  | Tg. Mures            | Pine         | 1           | 1         | 10.0  |
      | Hungary       | Budapest             | Oak          | 1           | 1         | 11.0  |
    Then I should succeed in creating "1" "1" "2" "15.0"