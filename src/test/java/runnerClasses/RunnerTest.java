package runnerClasses;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features="src/test/resources/Features",
        glue={"stepDefinitions"},
        plugin={"pretty","html:test-output/cucumber-reports/cucumber.html","json:test-output/cucumber-reports/cucumber.json","com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"},
        tags="@testcoord")

public class RunnerTest {


}