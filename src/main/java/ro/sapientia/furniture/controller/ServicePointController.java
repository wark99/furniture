package ro.sapientia.furniture.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.sapientia.furniture.model.ServicePoint;
import ro.sapientia.furniture.model.dto.ServicePointRequest;
import ro.sapientia.furniture.service.ServicePointService;

import java.util.List;

@RestController
@RequestMapping("/service_points")
@RequiredArgsConstructor
public class ServicePointController {

    private final ServicePointService servicePointService;

    @GetMapping()
    private ResponseEntity<List<ServicePoint>> getServicePoints() {
        final var servicePoints = servicePointService.findServicePoints();
        return new ResponseEntity<>(servicePoints, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicePoint> getServicePointBy(@PathVariable("id") final Long id) {
        final var servicePoint = servicePointService.findServicePointBy(id);
        return new ResponseEntity<>(servicePoint, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<ServicePoint> addServicePoint(@RequestBody final ServicePointRequest servicePointRequest) {
        final var servicePoint = servicePointService.create(servicePointRequest);
        return new ResponseEntity<>(servicePoint, HttpStatus.CREATED);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateServicePoint(@RequestBody final ServicePointRequest servicePointRequest) {
        servicePointService.update(servicePointRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<?> deleteServicePointBy(@PathVariable("id") final Long id) {
        servicePointService.deleteServicePointBy(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
