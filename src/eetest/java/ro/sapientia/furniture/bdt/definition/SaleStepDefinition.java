package ro.sapientia.furniture.bdt.definition;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.empty;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Map;

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
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import ro.sapientia.furniture.model.Region;
import ro.sapientia.furniture.model.Sale;
import ro.sapientia.furniture.model.ServicePoint;
import ro.sapientia.furniture.model.dto.SaleRequest;

@Transactional
@SpringBootTest
@AutoConfigureCache
@AutoConfigureDataJpa
@AutoConfigureMockMvc
@ContextConfiguration
@AutoConfigureTestEntityManager
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:eetest.properties")
public class SaleStepDefinition {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private TestEntityManager entityManager;

    @Given("^that we have the following sales:$")
    public void thatWeHaveTheFollowingSales(final DataTable sales) throws Throwable {
    	Region region = Region.builder().name("Europe").build();
    	region = entityManager.persistAndFlush(region);

    	ServicePoint servicePoint = ServicePoint.builder().region(region).build();
    	servicePoint = entityManager.persistAndFlush(servicePoint);

        for (final Map<String, String> data : sales.asMaps(String.class, String.class)) {
            final Sale sale = Sale.builder()
            		.servicePoint(servicePoint)
            		.totalPrice(new BigDecimal(data.get("price")))
            		.saledDate(Timestamp.valueOf(data.get("timestamp")))
            		.build();
            entityManager.persist(sale);
            System.out.println(sale);
        }
        entityManager.flush();

    }

    @Then("^I should get \"([^\"]*)\" items$")
    public void IShouldGetTheCorrectNumberOfItems(final int expectedSize) throws Throwable {
        mvc.perform(get("/sales/all")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content()
                .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(expectedSize)));
    }

    @Then("^I should get the price \"([^\"]*)\" for the position \"([^\"]*)\"$")
    public void IShouldGetTheCorrectPrice(final BigDecimal expectedResult, final int expectedPosition) throws Throwable {
        mvc.perform(get("/sales/all")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content()
                .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[" + expectedPosition + "].totalPrice", is(expectedResult.doubleValue())));
    }

    @Then("^I should get the response code \"([^\"]*)\" for find by \"([^\"]*)\"$")
    public void IShouldGetTheCorrectResponseCodeForFindById(final int expectedResponseCode, final Long saleId) throws Throwable {
        mvc.perform(get("/sales/get/" + saleId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is(expectedResponseCode));
    }

    @Then("^I should get the price \"([^\"]*)\" for find by \"([^\"]*)\"$")
    public void IShouldGetTheCorrectPriceForFindById(final BigDecimal expectedResult, final Long saleId) throws Throwable {
        mvc.perform(get("/sales/get/" + saleId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content()
                .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.totalPrice", is(expectedResult.doubleValue())));
    }

    @Then("^I should be able to create sale \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\"$")
    public void IShouldCreateANewSale(final Long ServicePointId, final BigDecimal price, final String timestamp) throws Throwable {
    	final ObjectMapper objectMapper = new ObjectMapper();
    	final SaleRequest saleRequest = SaleRequest.builder()
    			.servicePointId(ServicePointId)
    			.totalPrice(price)
    			.saledDate(Timestamp.valueOf(timestamp))
    			.build();

        mvc.perform(post("/sales/create")
        		.content(objectMapper.writeValueAsString(saleRequest))
                .contentType(MediaType.APPLICATION_JSON)
        		.accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content()
                .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.id").exists())
			.andExpect(jsonPath("$.servicePoint.id", is(ServicePointId.intValue())))
			.andExpect(jsonPath("$.totalPrice").exists())
			.andExpect(jsonPath("$.saledDate").exists());
    }

    @Then("^I should be able to update sale \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\"$")
    public void IShouldUpdateASale(final Long id, final Long ServicePointId, final BigDecimal price, final String timestamp) throws Throwable {
    	final ObjectMapper objectMapper = new ObjectMapper();
    	final SaleRequest saleRequest = SaleRequest.builder()
    			.id(id)
    			.servicePointId(ServicePointId)
    			.totalPrice(price)
    			.saledDate(Timestamp.valueOf(timestamp))
    			.build();

        mvc.perform(post("/sales/update")
        		.content(objectMapper.writeValueAsString(saleRequest))
                .contentType(MediaType.APPLICATION_JSON)
        		.accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content()
                .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.id", is(id.intValue())))
			.andExpect(jsonPath("$.servicePoint.id", is(ServicePointId.intValue())))
			.andExpect(jsonPath("$.totalPrice").exists())
			.andExpect(jsonPath("$.saledDate").exists());
    }

    @Then("^I should not be able to update sale \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\"$")
    public void IShouldNotUpdateASale(final Long id, final Long ServicePointId, final BigDecimal price, final String timestamp) throws Throwable {
    	final ObjectMapper objectMapper = new ObjectMapper();
    	final SaleRequest saleRequest = SaleRequest.builder()
    			.id(id)
    			.servicePointId(ServicePointId)
    			.totalPrice(price)
    			.saledDate(Timestamp.valueOf(timestamp))
    			.build();

        mvc.perform(post("/sales/update")
        		.content(objectMapper.writeValueAsString(saleRequest))
                .contentType(MediaType.APPLICATION_JSON)
        		.accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Then("^I should be able to delete sale by id \"([^\"]*)\"$")
    public void iShouldDeleteSaleById(final Long id) throws Exception {
        mvc.perform(delete("/sales/delete/" + id))
            .andExpect(status().isOk());
    }

}
