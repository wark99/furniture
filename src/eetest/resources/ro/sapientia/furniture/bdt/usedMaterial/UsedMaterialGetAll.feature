Feature: check if the get all endpoint works

  Scenario: Multiple element
    Given that we have the following used materials:
      | regionName    | servicePointCityName | materialName | furnitureId | quantity  | price |
      | Transylvania  | Tg. Mures            | Pine         | 1           | 1         | 10.0  |
      | Hungary       | Budapest             | Oak          | 1           | 1         | 11.0  |
    Then I should get "Transylvania" "Tg. Mures" "Pine" "1" "1" "10.0" for get all used materials position "0"
    Then I should get "Hungary" "Budapest" "Oak" "1" "1" "11.0" for get all used materials position "1"