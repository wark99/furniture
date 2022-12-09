Feature: check if the get all endpoint works

  Scenario: Multiple element
    Given that we have the following materials:
      | regionName  | servicePointCityName | name | origin | unit | unitPrice | quantity | quality |
      | Scandinavia | Malmo                | Pine | Sweden | SEK  | 1.0       | 1.0      | A       |
      | Balkan      | Novi Sad             | Oak  | Serbia | DIN  | 0.85      | 10.0     | B       |
    Then I should get "Scandinavia" "Malmo" "Pine" "Sweden" "SEK" "1" "1" "A" for get all materials position "0"
    Then I should get "Balkan" "Novi Sad" "Oak" "Serbia" "DIN" "0.85" "10.0" "B" for get all materials position "1"