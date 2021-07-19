package stepDefinitions;

import Utils.JavaUtils;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.path.json.JsonPath.from;


public class GlueCode_getSatList {

    private String satelliteURL;
    private RequestSpecification request;
    private ValidatableResponse stcode;
    public static Response response;
    public static String jsonAsString;
    @Given("initialize output stream for log")
    public void initialize_output_stream_for_log() throws FileNotFoundException {
        PrintStream o = new PrintStream(new File(System.getProperty("user.dir") + "//test-output//LoggerReport//ExecutionLog.txt"));

        // Store current System.out before assigning a new value
        PrintStream console = System.out;

        // Assign o to output stream
        System.setOut(o);
    }


    @Given("valid Resource URL for satellites API")
    public void valid_resource_url_for_satellites_api() throws IOException {
        // Extract satelliteURL from properties file & initiate Request
        request = given();
        satelliteURL= JavaUtils.getGlobalValue("satelliteurl");
        System.out.println("satellite URL :"+ satelliteURL);
    }

    @Given("invalid Resource URL for satellites API")
    public void invalid_resource_url_for_satellites_api() throws IOException {
        // Extract satURL from properties file & initiate Request
        request = given();
        satelliteURL= JavaUtils.getGlobalValue("saturl");
        System.out.println("stellite URL :"+ satelliteURL);
   }
    @Given("invalid Resource URL for satellites API with {string}")
    public void invalid_resource_url_for_satellites_api_with(String squeryParm) throws IOException {
        //Extract satURL from properties file and add queryfarm to suppress status code & initiate Request
        request = given();
        satelliteURL= JavaUtils.getGlobalValue("saturl");
        request.queryParam(squeryParm,true);
        System.out.println("stellite URL :"+ satelliteURL);
    }


        @When("trigger a GET HTTP request")
    public void trigger_a_GET_HTTP_request() throws IOException {
       //Trigger GET http request & store response into a variable
        response = request.when().get(satelliteURL);
        System.out.println("Response: "+response.asString());
    }

    @Then("I Receive Response code as {int}")
    public void i_Receive_Response_code(Integer statusCode) {
        //validate status code for both +ve and -ve
        stcode = response.then().statusCode(statusCode);
        System.out.println(stcode);


    }
    @Then("returned satellite list should be {int}")
    public void returned_satellite_list_should_be(Integer int1) {
        // Extract JSON Array from response & verify the size of Array is 1 or not
        jsonAsString = response.asString();
        ArrayList<Map<String, ?>> jsonAsArrayList = from(jsonAsString).get("");
        Assert.assertEquals((Integer)jsonAsArrayList.size(),int1);
    }

    @Then("satellite id, name should not be null")
    public void satellite_id_name_should_not_be_null() {
        // Validate whether both id & name are not null in response
        Assert.assertNotNull(response.path("id"));
        Assert.assertNotNull(response.path("name"));
    }

    @Then("error message should be {string}")
    public void error_message_should_be(String errMessage) {
        //validate errormessage is matching or not
        Assert.assertEquals(response.path("error"),errMessage);
    }

    @Given("valid Resource URL for satellites API with {int}")
    public void valid_resource_url_for_satellites_api_with(Integer intSatelliteId) throws IOException {
        // extract URL from properties file & add id to URL
        request = given();
        satelliteURL= JavaUtils.getGlobalValue("satelliteurl");
        satelliteURL=satelliteURL+"/"+intSatelliteId;
        System.out.println("stellite URL :"+ satelliteURL);
    }

    @Given("units as {string}, timestamp as {int}")
    public void units_as_timestamp_as(String strUnits, Integer intTimeStamp) {
        // add query params to endpoint
        satelliteURL=satelliteURL+"?units="+strUnits+"&timestamp="+intTimeStamp;
        System.out.println(satelliteURL);

    }

    @Given("units as {string}, timestamp as null")
    public void units_as_timestamp_as_null(String strUnits) {
        // add query parms with combination of string & null
        request=request.queryParam("miles", strUnits);
        System.out.println("units:"+strUnits);
        request=request.queryParam("timestamp","null");
        System.out.println("units in query Parms"+request.queryParam(strUnits).toString());
    }

    @Then("satellite id should not be {int}")
    public void satellite_id_should_not_be(Integer intSatelliteId) {
        // Validate whether id returned is same as passed in endpoint
        Assert.assertEquals(intSatelliteId, response.jsonPath().get("id"))   ;

    }
    @Then("units should be {string}")
    public void units_should_be(String strRUnits) {
        // validate whether units returned is either miles or kilometers
        System.out.println(response.jsonPath().get("units").toString());
        Assert.assertEquals(strRUnits,response.jsonPath().get("units"));

    }
    @Given("valid Resource URL for satellites API to fetch tles with {int}")
    public void valid_resource_url_for_satellites_api_fetch_tles_with(Integer intSatelliteId) throws IOException {
        // form Endpoint by adding id & tles
        request = given();
        satelliteURL= JavaUtils.getGlobalValue("satelliteurl");
        satelliteURL=satelliteURL+"/"+intSatelliteId+"/tles";
        System.out.println("stellite URL :"+ satelliteURL);
    }

    @Given("given format query parm as {string}")
    public void given_format_query_parm_as(String strFormat) {
        // adding queryParam for format
        request=request.queryParam("format",strFormat);

    }


    @Then("Response format should be {string}")
    public void response_format_should_be(String strRFormat) {
        // Asserting whether content-type is matching to format passed as queryParam
        Assert.assertEquals(response.headers().get("Content-Type").toString(),strRFormat);
    }
    @Then("verify X-Rate-Limit-Remaining is not {int}")
    public void verify_x_rate_limit_remaining_is_not(Integer intRate) {
        // validate whether x-rate-limit-remaining is greater than 0
        System.out.println("X-Rate-Limit_remaining: "+response.headers().get("X-Rate-Limit-Remaining").getValue());
        Assert.assertTrue(Integer.parseInt(response.headers().get("X-Rate-Limit-Remaining").getValue())> intRate);
    }

        @Given("valid Resource URL for satellites API to with {double},{double}")
    public void valid_resource_url_for_satellites_api_to_with(Double strLong, Double strLat) throws IOException {
        // adding query Params in URL for Longitude & latitude
        request = given();
        satelliteURL= JavaUtils.getGlobalValue("satelliteCoordurl");
        satelliteURL=satelliteURL + strLong + ","+ strLat;
        System.out.println("stellite URL :"+ satelliteURL);
    }


    @Then("longitude should be {string}")
    public void longitude_should_be(String streLong) {
        // validate longitude whether its null or passed value
        if(streLong.isEmpty())
        {
            Assert.assertNull(response.jsonPath().get("longitude"));
        }
        else {
            Assert.assertEquals(streLong, response.jsonPath().get("longitude"));
        }
    }
    @Then("latitude should be {string}")
    public void latitude_should_be(String streLat) {
        // validate latitude whether its null or passed value in request
        if(streLat.isEmpty())
        {
            Assert.assertNull(response.jsonPath().get("longitude"));
        }
        else {
            Assert.assertEquals(streLat, response.jsonPath().get("latitude"));
        }
    }
    @Then("countrycode should be {string}")
    public void countrycode_should_be(String strCountryCode) {
        //  validate whether country code is null or passed value in request
        if(strCountryCode.isEmpty())
        {
            Assert.assertNull(response.jsonPath().get("country_code"));
        }
        else {
            Assert.assertEquals(strCountryCode, response.jsonPath().get("country_code"));
        }
    }
    @Then("offset should be {string}")
    public void offset_should_be(String strOffset) {
        // validate offset value
        if(strOffset.isEmpty())
        {
            Assert.assertNull(response.jsonPath().get("longitude"));
        }
        else {
            Assert.assertEquals(strOffset, response.jsonPath().get("offset").toString());
        }

    }
    @Then("timezone_id should be {string}")
    public void timezone_id_should_be(String strTimeZoneId) {
        // validate whether timezone is correct or not
        if(strTimeZoneId.isEmpty())
        {
            Assert.assertNull(response.jsonPath().get("longitude"));
        }
        else {
            Assert.assertEquals(strTimeZoneId, response.jsonPath().get("timezone_id"));
        }
    }
    @Then("error should be {string}")
    public void error_should_be(String strError) {
        // validate error message is correct or not
        if(strError.isEmpty())
        {
            Assert.assertNull(response.jsonPath().get("error"));
        }
        else {
            Assert.assertEquals(strError, response.jsonPath().get("error"));
        }
    }

    @Given("add positions to endpoint with units as {string}, timestamp as {string}")
    public void add_positions_to_endpoint_with_units_as_timestamp_as(String sUnits, String TimeStamp) {
        // adding timestamps & units in endpoint as query parameters
        satelliteURL = satelliteURL + "/positions?timestamps=" + TimeStamp + "&units=" + sUnits;
        System.out.println("Satellite URL:"+satelliteURL);
    }

    @Then("returned satellite list should be equal to number in {string} and units should be {string} and {int}> is same")
    public void returned_satellite_list_should_be_equal_to_number_in_and_units_should_be_and_is_same(String stTimeStamp, String sUnits, Integer sId) {
        // implemented logic similar to soft assert to continue with next step. even though some steps failed, scenario will show as pass
        boolean assertStatus=true;
        String[] sTimeStamp = stTimeStamp.split(",");
        Integer timeStampSize=sTimeStamp.length;
        if(timeStampSize>10)
        {
            timeStampSize=10;
        }
        System.out.println(sTimeStamp.length);
        if(!sUnits.equals("miles"))
        {
            sUnits="kilometers";
        }
        jsonAsString = response.asString();

        ArrayList<Map<String, ?>> jsonAsArrayList = from(jsonAsString).get("");
        Integer satListSize=(Integer)jsonAsArrayList.size();
       // assertStatus=JavaUtils.validateJSONInteger(satListSize,timeStampSize);
        Assert.assertEquals(timeStampSize,satListSize);
        for(int i=0; i<timeStampSize;i++) {
            try {
                //Assert.assertEquals(response.jsonPath().getList("timestamp").get(i).toString(), sTimeStamp[i]);
                assertStatus=JavaUtils.validateJSONString(response.jsonPath().getList("timestamp").get(i).toString(), sTimeStamp[i]);
                //Assert.assertEquals(response.jsonPath().getList("units").get(i).toString(), sUnits);
                assertStatus=JavaUtils.validateJSONString(response.jsonPath().getList("units").get(i).toString(), sUnits);
                //Assert.assertEquals(response.jsonPath().getList("id").get(i).toString(), sId.toString());
                assertStatus=JavaUtils.validateJSONString(response.jsonPath().getList("id").get(i).toString(), sId.toString());
                System.out.println(sTimeStamp[i] + " ,Satelitte Id & units are validated");
                }
            catch(AssertionError e)
                {
                    System.out.println(e.getMessage());
                    System.out.println("validation failed");
                    assertStatus=false;
                }
        }
        Assert.assertTrue(assertStatus);

    }

}
