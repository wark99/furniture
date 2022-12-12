Feature: Check if the get by id endpoint works

  Scenario: No element
    Given that we have the following materials:
      | name |
    Then I should get "RECORD_NOT_FOUND" error for get material by id "70"

  Scenario: Multiple element
    Given that we have the following materials:
      | regionName  | servicePointCityName | name | origin | unit | unitPrice | quantity | quality |
      | Scandinavia | Malmo                | Pine | Sweden | SEK  | 1.0       | 1.0      | A       |
      | Balkan      | Novi Sad             | Oak  | Serbia | DIN  | 0.85      | 10.0     | B       |
    Then I should get "Balkan" "Novi Sad" "Oak" "Serbia" "DIN" "0.85" "10.0" "B" for get material by id "2"
