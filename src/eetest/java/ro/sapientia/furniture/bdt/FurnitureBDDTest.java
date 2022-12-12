package ro.sapientia.furniture.bdt;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = {"src/eetest/resources/ro/sapientia/furniture/bdt/"},
    glue = {"ro.sapientia.furniture.bdt.definition"},
    publish = false,
    dryRun = true)
public class FurnitureBDDTest {

}
