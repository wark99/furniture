package ro.sapientia.furniture.service;

import ro.sapientia.furniture.model.ServicePoint;
import ro.sapientia.furniture.model.dto.ServicePointRequest;

import java.util.List;

public interface ServicePointService {

    List<ServicePoint> findServicePoints();

    ServicePoint findServicePointBy(final Long id);

    ServicePoint create(final ServicePointRequest servicePointRequest);

    void update(final ServicePointRequest servicePointRequest);

    void deleteServicePointBy(final Long id);
}
