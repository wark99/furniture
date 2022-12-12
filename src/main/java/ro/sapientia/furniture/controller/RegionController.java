package ro.sapientia.furniture.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.sapientia.furniture.model.Region;
import ro.sapientia.furniture.model.dto.RegionRequest;
import ro.sapientia.furniture.service.RegionService;

import java.util.List;

@RestController
@RequestMapping("/regions")
@RequiredArgsConstructor
public class RegionController {

    private final RegionService regionService;

    @GetMapping()
    public ResponseEntity<List<Region>> getRegions() {
        final var regions = regionService.findRegions();
        return new ResponseEntity<>(regions, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Region> getRegionBy(@PathVariable("id") final Long id) {
        final var region = regionService.findRegionBy(id);
        return new ResponseEntity<>(region, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Region> addRegion(@RequestBody final RegionRequest regionRequest) {
        final var regionResponse = regionService.create(regionRequest);
        return new ResponseEntity<>(regionResponse, HttpStatus.CREATED);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateRegion(@RequestBody final Region region) {
        regionService.update(region);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<?> deleteRegionBy(@PathVariable("id") final Long id) {
        regionService.deleteRegionBy(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
