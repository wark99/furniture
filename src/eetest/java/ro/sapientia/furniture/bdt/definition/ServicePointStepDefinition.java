package ro.sapientia.furniture.bdt.definition;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
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
import ro.sapientia.furniture.model.Region;
import ro.sapientia.furniture.model.ServicePoint;

import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureCache
@AutoConfigureDataJpa
@AutoConfigureMockMvc
@ContextConfiguration
@AutoConfigureTestEntityManager
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:eetest.properties")
public class ServicePointStepDefinition {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private TestEntityManager entityManager;

    @Given("^that we have the following service points:$")
    public void that_we_have_the_following_service_point(final DataTable servicePoints) throws Throwable {
        for (final Map<String, String> data : servicePoints.asMaps(String.class, String.class)) {
            final var region = new Region();
            region.setName(data.get("regionName"));
            entityManager.persistAndFlush(region);
            final var servicePoint = new ServicePoint();
            servicePoint.setRegion(region);
            servicePoint.setCountry(data.get("country"));
            servicePoint.setCounty(data.get("county"));
            servicePoint.setCity(data.get("city"));
            servicePoint.setStreet(data.get("street"));
            servicePoint.setNumber(data.get("number"));
            servicePoint.setZipCode(data.get("zipCode"));
            entityManager.persist(servicePoint);
        }
        entityManager.flush();
    }

    @Then("^I should get \"([^\"]*)\" error for find all service points$")
    public void I_should_get_RECORD_NOT_FOUND_error_for_find_all(final String expectedResult) throws Throwable {
        mvc.perform(get("/service_points").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("error", is(expectedResult)));
    }


    @Then("^I should get \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" for find all service points position \"([^\"]*)\"$")
    public void iShouldGetForFindAllServicePointsPosition(final String regionName, final String country, final String county, final String city, final String street, final String number, final String zipCode, final Long position) throws Exception {
        mvc.perform(get("/service_points"))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[" + position + "].region.name", is(regionName)))
            .andExpect(jsonPath("$[" + position + "].country", is(country)))
            .andExpect(jsonPath("$[" + position + "].county", is(county)))
            .andExpect(jsonPath("$[" + position + "].city", is(city)))
            .andExpect(jsonPath("$[" + position + "].street", is(street)))
            .andExpect(jsonPath("$[" + position + "].number", is(number)))
            .andExpect(jsonPath("$[" + position + "].zipCode", is(zipCode)));
    }

    @Then("^I should get \"([^\"]*)\" error for find service point by id \"([^\"]*)\"$")
    public void iShouldGetErrorForFindServicePointById(final String expectedResult, final Long id) throws Exception {
        mvc.perform(get("/service_points/" + id))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("error", is(expectedResult)));
    }

    @Then("^I should get \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" for find service point by id \"([^\"]*)\"$")
    public void iShouldGetForFindServicePointById(final String regionName, final String country, final String county, final String city, final String street, final String number, final String zipCode, final Long id) throws Exception {
        mvc.perform(get("/service_points/" + id))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("region.name", is(regionName)))
            .andExpect(jsonPath("country", is(country)))
            .andExpect(jsonPath("county", is(county)))
            .andExpect(jsonPath("city", is(city)))
            .andExpect(jsonPath("street", is(street)))
            .andExpect(jsonPath("number", is(number)))
            .andExpect(jsonPath("zipCode", is(zipCode)));
    }

    @Then("^I should get no error for adding \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\"$")
    public void iShouldGetNoErrorForAdding(final Long id, final Long regionId, final String country, final String county, final String city, final String street, final String number, final String zipCode) throws Exception {
        mvc.perform(post("/service_points/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\": " + id + ",\n" +
                    "    \"regionId\": " + regionId + ",\n" +
                    "    \"country\": \"" + country + "\",\n" +
                    "    \"county\": \"" + county + "\",\n" +
                    "    \"city\": \"" + city + "\",\n" +
                    "    \"street\": \"" + street + "\",\n" +
                    "    \"number\": \"" + number + "\",\n" +
                    "    \"zipCode\": \"" + zipCode + "\"\n}")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("country", is(country)))
            .andExpect(jsonPath("county", is(county)))
            .andExpect(jsonPath("city", is(city)))
            .andExpect(jsonPath("street", is(street)))
            .andExpect(jsonPath("number", is(number)))
            .andExpect(jsonPath("zipCode", is(zipCode)));
    }

    @Then("^I should get \"([^\"]*)\" error for updating \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\"$")
    public void iShouldGetErrorForUpdating(final String expectedError, final Long id, final Long regionId, final String country, final String county, final String city, final String street, final String number, final String zipCode) throws Exception {
        mvc.perform(post("/service_points/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\": " + id + ",\n" +
                    "    \"regionId\": " + regionId + ",\n" +
                    "    \"country\": \"" + country + "\",\n" +
                    "    \"county\": \"" + county + "\",\n" +
                    "    \"city\": \"" + city + "\",\n" +
                    "    \"street\": \"" + street + "\",\n" +
                    "    \"number\": \"" + number + "\",\n" +
                    "    \"zipCode\": \"" + zipCode + "\"\n}")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("error", is(expectedError)));
    }

    @Then("^I should get no error for updating \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\" \"([^\"]*)\"$")
    public void iShouldGetNoErrorForUpdating(final Long id, final Long regionId, final String country, final String county, final String city, final String street, final String number, final String zipCode) throws Exception {
        mvc.perform(post("/service_points/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\": " + id + ",\n" +
                    "    \"regionId\": " + regionId + ",\n" +
                    "    \"country\": \"" + country + "\",\n" +
                    "    \"county\": \"" + county + "\",\n" +
                    "    \"city\": \"" + city + "\",\n" +
                    "    \"street\": \"" + street + "\",\n" +
                    "    \"number\": \"" + number + "\",\n" +
                    "    \"zipCode\": \"" + zipCode + "\"\n}")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Then("^I should get \"([^\"]*)\" error for deleting service point by id \"([^\"]*)\"$")
    public void iShouldGetErrorForDeletingServicePointById(final String expectedResult, final Long id) throws Exception {
        mvc.perform(get("/service_points/delete/" + id))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("error", is(expectedResult)));
    }

    @Then("^I should get no error for deleting service point by id \"([^\"]*)\"$")
    public void iShouldGetNoErrorForDeletingServicePointById(final Long id) throws Exception {
        mvc.perform(get("/service_points/delete/" + id))
            .andExpect(status().isOk());
    }
}