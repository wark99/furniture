Feature: Check if the update endpoint works

  Scenario: No element
    Given that we have the following used materials:
      | quantity |
    Then I should get "RECORD_NOT_FOUND" error for updating used material "40" "1" "1" "1" "10.0"

  Scenario: Multiple element
    Given that we have the following used materials:
      | regionName    | servicePointCityName | materialName | furnitureId | quantity  | price |
      | Transylvania  | Tg. Mures            | Pine         | 1           | 1         | 10.0  |
      | Hungary       | Budapest             | Oak          | 1           | 1         | 11.0  |
    Then I should succeed in updating used material "1" "1" "1" "10" "15.0"