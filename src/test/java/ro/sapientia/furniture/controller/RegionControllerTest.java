package ro.sapientia.furniture.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ro.sapientia.furniture.exception.RecordNotFoundException;
import ro.sapientia.furniture.model.Region;
import ro.sapientia.furniture.model.dto.RegionRequest;
import ro.sapientia.furniture.service.RegionService;

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
import static ro.sapientia.furniture.mocking.RegionDatabaseBuilder.buildTestRegions;

@WebMvcTest(controllers = RegionController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
public class RegionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean(RegionService.class)
    private RegionService regionService;

    @Test
    public void testGetRegionsShouldFail() throws Exception {
        when(regionService.findRegions()).thenThrow(new RecordNotFoundException(Region.class));

        mockMvc.perform(get("/regions"))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("status", is(400)))
            .andExpect(jsonPath("error", is("RECORD_NOT_FOUND")))
            .andExpect(jsonPath("message", is("Region table is empty!")));
    }

    @Test
    public void testGetRegionsShouldSucceed() throws Exception {
        final var region = buildTestRegions();
        when(regionService.findRegions()).thenReturn(region);

        mockMvc.perform(get("/regions"))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].id", is(1)))
            .andExpect(jsonPath("$[0].name", is("Test Region 1")))
            .andExpect(jsonPath("$[1].id", is(2)))
            .andExpect(jsonPath("$[1].name", is("Test Region 2")));
    }

    @Test
    public void testGetRegionByShouldFail() throws Exception {
        when(regionService.findRegionBy(anyLong())).thenThrow(
            new RecordNotFoundException(Region.class, Map.of("id", "3")));

        mockMvc.perform(get("/regions/3"))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("status", is(400)))
            .andExpect(jsonPath("error", is("RECORD_NOT_FOUND")))
            .andExpect(jsonPath("message", is("Record not found in table: Region")))
            .andExpect(jsonPath("params.id", is("3")));
    }

    @Test
    public void testGetRegionByShouldSucceed() throws Exception {
        final var region = buildTestRegions().get(0);
        when(regionService.findRegionBy(anyLong())).thenReturn(region);

        mockMvc.perform(get("/regions/3"))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("id", is(1)))
            .andExpect(jsonPath("name", is("Test Region 1")));
    }

    @Test
    public void testAddRegionShouldSucceed() throws Exception {
        final var region = buildTestRegions().get(0);
        when(regionService.create(any(RegionRequest.class))).thenReturn(region);

        mockMvc.perform(post("/regions/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"name\": \"Test Region 1\"}")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("id", is(1)))
            .andExpect(jsonPath("name", is("Test Region 1")));
    }

    @Test
    public void testUpdateRegionShouldFail() throws Exception {
        doThrow(new RecordNotFoundException(Region.class, Map.of("id", "1")))
            .when(regionService).update(any(Region.class));

        mockMvc.perform(post("/regions/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\": 1, \"name\": \"Test Region 1\"}")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("status", is(400)))
            .andExpect(jsonPath("error", is("RECORD_NOT_FOUND")))
            .andExpect(jsonPath("message", is("Record not found in table: Region")))
            .andExpect(jsonPath("params.id", is("1")));
    }

    @Test
    public void testUpdateRegionShouldSucceed() throws Exception {
        mockMvc.perform(post("/regions/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\": 1, \"name\": \"Test Region 1\"}")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    public void testDeleteRegionByShouldFail() throws Exception {
        doThrow(new RecordNotFoundException(Region.class, Map.of("id", "1")))
            .when(regionService).deleteRegionBy(anyLong());

        mockMvc.perform(get("/regions/delete/1"))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("status", is(400)))
            .andExpect(jsonPath("error", is("RECORD_NOT_FOUND")))
            .andExpect(jsonPath("message", is("Record not found in table: Region")))
            .andExpect(jsonPath("params.id", is("1")));
    }

    @Test
    public void testDeleteRegionByShouldSucceed() throws Exception {
        mockMvc.perform(get("/regions/delete/1"))
            .andExpect(status().isOk());
    }
}
