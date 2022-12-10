package ro.sapientia.furniture.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ro.sapientia.furniture.exception.RecordNotFoundException;
import ro.sapientia.furniture.model.ServicePoint;
import ro.sapientia.furniture.model.dto.ServicePointRequest;
import ro.sapientia.furniture.service.ServicePointService;

import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ro.sapientia.furniture.mocking.ServicePointDatabaseBuilder.buildTestServicePoints;

@WebMvcTest(controllers = ServicePointController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
public class ServicePointControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean(ServicePointService.class)
    private ServicePointService servicePointService;

    @Test
    public void testGetServicePointsShouldFail() throws Exception {
        when(servicePointService.findServicePoints()).thenThrow(new RecordNotFoundException(ServicePoint.class));

        mockMvc.perform(get("/service_points"))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("status", is(400)))
            .andExpect(jsonPath("error", is("RECORD_NOT_FOUND")))
            .andExpect(jsonPath("message", is("ServicePoint table is empty!")));
    }

    @Test
    public void testGetServicePointsShouldSucceed() throws Exception {
        final var servicePoints = buildTestServicePoints();
        when(servicePointService.findServicePoints()).thenReturn(servicePoints);

        mockMvc.perform(get("/service_points"))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].id", is(1)))
            .andExpect(jsonPath("$[0].region.id", is(1)))
            .andExpect(jsonPath("$[0].region.name", is("Test Region 1")))
            .andExpect(jsonPath("$[0].country", is("Test Country 1")))
            .andExpect(jsonPath("$[0].county", is("Test County 1")))
            .andExpect(jsonPath("$[0].city", is("Test City 1")))
            .andExpect(jsonPath("$[0].street", is("Test Street 1")))
            .andExpect(jsonPath("$[0].number", is("Test Number 1")))
            .andExpect(jsonPath("$[0].zipCode", is("Test Zip Code 1")))
            .andExpect(jsonPath("$[1].id", is(2)))
            .andExpect(jsonPath("$[1].region.id", is(2)))
            .andExpect(jsonPath("$[1].region.name", is("Test Region 2")))
            .andExpect(jsonPath("$[1].country", is("Test Country 2")))
            .andExpect(jsonPath("$[1].county", is("Test County 2")))
            .andExpect(jsonPath("$[1].city", is("Test City 2")))
            .andExpect(jsonPath("$[1].street", is("Test Street 2")))
            .andExpect(jsonPath("$[1].number", is("Test Number 2")))
            .andExpect(jsonPath("$[1].zipCode", is("Test Zip Code 2")));
    }


    @Test
    public void testGetServicePointByShouldFail() throws Exception {
        when(servicePointService.findServicePointBy(anyLong())).thenThrow(
            new RecordNotFoundException(ServicePoint.class, Map.of("id", "3")));

        mockMvc.perform(get("/service_points/3"))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("status", is(400)))
            .andExpect(jsonPath("error", is("RECORD_NOT_FOUND")))
            .andExpect(jsonPath("message", is("Record not found in table: ServicePoint")))
            .andExpect(jsonPath("params.id", is("3")));
    }

    @Test
    public void testGetServicePointByShouldSucceed() throws Exception {
        final var servicePoint = buildTestServicePoints().get(0);
        when(servicePointService.findServicePointBy(anyLong())).thenReturn(servicePoint);

        mockMvc.perform(get("/service_points/3"))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("id", is(1)))
            .andExpect(jsonPath("region.id", is(1)))
            .andExpect(jsonPath("region.name", is("Test Region 1")))
            .andExpect(jsonPath("country", is("Test Country 1")))
            .andExpect(jsonPath("county", is("Test County 1")))
            .andExpect(jsonPath("city", is("Test City 1")))
            .andExpect(jsonPath("street", is("Test Street 1")))
            .andExpect(jsonPath("number", is("Test Number 1")))
            .andExpect(jsonPath("zipCode", is("Test Zip Code 1")));
    }

    @Test
    public void testAddServicePointShouldSucceed() throws Exception {
        final var servicePoint = buildTestServicePoints().get(0);
        when(servicePointService.create(any(ServicePointRequest.class))).thenReturn(servicePoint);

        mockMvc.perform(post("/service_points/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\": 1,\n" +
                    "    \"regionId\": 1,\n" +
                    "    \"country\": \"Asd\",\n" +
                    "    \"county\": \"Qwe\",\n" +
                    "    \"city\": \"Zxc\",\n" +
                    "    \"street\": \"RT\",\n" +
                    "    \"number\": \"11\",\n" +
                    "    \"zipCode\": \"12-S\"\n}")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("id", is(1)))
            .andExpect(jsonPath("region.id", is(1)))
            .andExpect(jsonPath("region.name", is("Test Region 1")))
            .andExpect(jsonPath("country", is("Test Country 1")))
            .andExpect(jsonPath("county", is("Test County 1")))
            .andExpect(jsonPath("city", is("Test City 1")))
            .andExpect(jsonPath("street", is("Test Street 1")))
            .andExpect(jsonPath("number", is("Test Number 1")))
            .andExpect(jsonPath("zipCode", is("Test Zip Code 1")));
    }

    @Test
    public void testUpdateServicePointShouldFail() throws Exception {
        doThrow(new RecordNotFoundException(ServicePoint.class, Map.of("id", "1")))
            .when(servicePointService).update(any(ServicePointRequest.class));

        mockMvc.perform(post("/service_points/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\": 54,\n" +
                    "    \"regionId\": 3,\n" +
                    "    \"country\": \"Asd\",\n" +
                    "    \"county\": \"Qwe\",\n" +
                    "    \"city\": \"Zxc\",\n" +
                    "    \"street\": \"RT\",\n" +
                    "    \"number\": \"11\",\n" +
                    "    \"zipCode\": \"12-S\"}")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("status", is(400)))
            .andExpect(jsonPath("error", is("RECORD_NOT_FOUND")))
            .andExpect(jsonPath("message", is("Record not found in table: ServicePoint")))
            .andExpect(jsonPath("params.id", is("1")));
    }

    @Test
    public void testUpdateServicePointShouldSucceed() throws Exception {
        mockMvc.perform(post("/service_points/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\": 54,\n" +
                    "    \"regionId\": 3,\n" +
                    "    \"country\": \"Asd\",\n" +
                    "    \"county\": \"Qwe\",\n" +
                    "    \"city\": \"Zxc\",\n" +
                    "    \"street\": \"RT\",\n" +
                    "    \"number\": \"11\",\n" +
                    "    \"zipCode\": \"12-S\"}")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    public void testDeleteServicePointByShouldFail() throws Exception {
        doThrow(new RecordNotFoundException(ServicePoint.class, Map.of("id", "1")))
            .when(servicePointService).deleteServicePointBy(anyLong());

        mockMvc.perform(get("/service_points/delete/1"))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("status", is(400)))
            .andExpect(jsonPath("error", is("RECORD_NOT_FOUND")))
            .andExpect(jsonPath("message", is("Record not found in table: ServicePoint")))
            .andExpect(jsonPath("params.id", is("1")));
    }

    @Test
    public void testDeleteServicePointByShouldSucceed() throws Exception {
        mockMvc.perform(get("/service_points/delete/1"))
            .andExpect(status().isOk());
    }
}
