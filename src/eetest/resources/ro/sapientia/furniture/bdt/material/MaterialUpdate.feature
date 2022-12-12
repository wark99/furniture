Feature: Check if the update endpoint works

  Scenario: No element
    Given that we have the following materials:
      | name |
    Then I should get "RECORD_NOT_FOUND" error for updating material "40" "1" "Test Name" "Test Origin" "Test Unit" "1.0" "1.0" "Test Quality"

  Scenario: Multiple element
    Given that we have the following materials:
      | regionName  | servicePointCityName | name | origin | unit | unitPrice | quantity | quality |
      | Scandinavia | Malmo                | Pine | Sweden | SEK  | 1.0       | 1.0      | A       |
      | Balkan      | Novi Sad             | Oak  | Serbia | DIN  | 0.85      | 10.0     | B       |
    Then I should succeed in updating material "1" "1" "Test Name" "Test Origin" "Test Unit" "1.0" "1.0" "Test Quality"