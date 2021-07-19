Feature: TestSuite to test WTIA REST APIs to verify satellite list, info like timestamp, units, coordinates, timezone
  @init @Regression
    Scenario: initialize output stream to store log to extract sysout messages to text file as log
    Given initialize output stream for log

  @Regression @getSatellitesList
  Scenario: Validate whether satellites list is returned with id & name and it should return only one list which is currently available
    Given valid Resource URL for satellites API
    When trigger a GET HTTP request
    Then I Receive Response code as 200
    And returned satellite list should be 1
    And satellite id, name should not be null


  @getSatellitesList @Regression @negativeTest
  Scenario: Validate whether get satellites API is returning 404 as error when endpoint is incorrect
    Given invalid Resource URL for satellites API
    When trigger a GET HTTP request
    Then I Receive Response code as 404
    Then error message should be "Invalid controller specified (satellite)"

  @getSatellitesList @Regression @negativeTest
  Scenario: Validate whether suppress_response_codes funcitonality is working or not for get satellites API
    Given invalid Resource URL for satellites API with "suppress_response_codes"
    When trigger a GET HTTP request
    Then I Receive Response code as 200
    Then error message should be "Invalid controller specified (satellite)"

  @validateSatInfo @Regression
  Scenario Outline: GET satellite info with/without optional parameters units, timestamp
    Given valid Resource URL for satellites API with <satelliteId>
    And units as "<units>", timestamp as <timeStamp>
    When trigger a GET HTTP request
    Then I Receive Response code as <statusCode>
    And satellite id should not be <satelliteId>
    And units should be "<resUnits>"
    Examples:
      | satelliteId |units |timeStamp|statusCode |resUnits|
      | 25544       |miles| 234     |200         |miles   |
      | 25544       |null |  1245   |200         |kilometers|
      | 25544       | |  null   |200         |kilometers|
      | 25544       | |  1245  |200         |kilometers|
      | 25544       |miles|  1245  |200         |miles|

  @validateSatpositions @Regression
  Scenario Outline: validate satellite list with specified coordinates
    Given valid Resource URL for satellites API with <satelliteId>
    And add positions to endpoint with units as "<units>", timestamp as "<timeStamp>"
    When trigger a GET HTTP request
    Then I Receive Response code as <statusCode>
    And returned satellite list should be equal to number in "<timeStamp>" and units should be "<resUnits>" and <satelliteId>> is same
    Examples:
      | satelliteId |units |timeStamp|statusCode |resUnits|
      | 25544       |miles| 21436029892,1436029902    |200         |miles   |
      | 25544       |miles| none    |200         |miles   |
      | 25544       |null |  1436029902   |200         |kilometers|
      | 25544       |null |  21433429892,1436169902   |200         |   kilometers       |
      | 25544       |null |  21433429891,21433429892,21433429893,21433429894,21433429895,21433429896,21433429897,21433429898,21433429899,21433429110,21433429911   |200         |   kilometers       |


  @validateTLE @Regression
  Scenario Outline: Validate whether TLE Data from satellite API returning in JSON/Text format
    Given valid Resource URL for satellites API to fetch tles with <satelliteId>
    And given format query parm as "<format>"
    When trigger a GET HTTP request
    Then I Receive Response code as <statusCode>
    And Response format should be "<resFormat>"
    And verify X-Rate-Limit-Remaining is not 0
    Examples:
      | satelliteId |format|statusCode |resFormat|
      | 25544       | json     |200    |Content-Type=application/json     |
      | 25544       | text   |200      |Content-Type=text/plain     |
      | 25544       |    |200      |Content-Type=application/json     |


  @validatecoord @Regression
  Scenario Outline: Validate whether satellite API returning correct data with co-ordinates
    Given valid Resource URL for satellites API to with <latitude>,<longitude>
    When trigger a GET HTTP request
    Then I Receive Response code as <statusCode>
    And longitude should be "<elongitude>"
    And latitude should be "<elatitude>"
    And countrycode should be "<countrycode>"
    And offset should be "<offset>"
    And timezone_id should be "<timezone_id>"
    And error should be "<error>"
    Examples:
      | latitude |longitude   |statusCode |countrycode|offset|timezone_id|elatitude |elongitude|error |
      | 20.593683 | 78.962883 |200        |IN         |5.5  |Asia/Kolkata|20.593683 | 78.962883 |     |
      | 51.507351 | -0.127758 |200        |GB         |1    |Europe/London|51.507351| -0.127758 |     |
      | 90.593683 | 78.962883 |500        |           |     |             |         |           |application error|
      |-91.507351 | -0.127758 |500        |           |     |             |         |           |application error|
      | 20.593683 | 181.962883 |500       |           |     |             |         |           |application error|
      |-21.507351 | -180.127758 |500      |           |     |             |         |           |application error|

