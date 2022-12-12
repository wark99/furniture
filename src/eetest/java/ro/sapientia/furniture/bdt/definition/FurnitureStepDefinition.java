package ro.sapientia.furniture.bdt.definition;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ro.sapientia.furniture.model.FurnitureBody;

import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@CucumberContextConfiguration
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@AutoConfigureCache
@AutoConfigureDataJpa
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureTestEntityManager
@TestPropertySource(locations = "classpath:eetest.properties")
@ContextConfiguration
public class FurnitureStepDefinition {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private TestEntityManager entityManager;

    @Given("^that we have the following furniture bodies:$")
    public void that_we_have_the_following_furniture_bodies(final DataTable furnitureBodies) throws Throwable {
        for (final Map<String, String> data : furnitureBodies.asMaps(String.class, String.class)) {
            final FurnitureBody body = new FurnitureBody();
            body.setHeigth(Integer.parseInt(data.get("heigth")));
            body.setWidth(Integer.parseInt(data.get("width")));
            body.setDepth(Integer.parseInt(data.get("depth")));
            entityManager.persist(body);
        }
        entityManager.flush();

    }

    @When("^I invoke the furniture all endpoint$")
    public void I_invoke_the_furniture_all_endpoint() throws Throwable {
    }

    @Then("^I should get the heigth \"([^\"]*)\" for the position \"([^\"]*)\"$")
    public void I_should_get_result_in_stories_list(final String expectedResult, final String expectedPosition) throws Throwable {
        mvc.perform(get("/furniture/all")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content()
                .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[" + expectedPosition + "].heigth", is(Integer.parseInt(expectedResult))));
    }

    @After
    public void closeBrowser() {
    }

}
