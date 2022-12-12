package ro.sapientia.furniture.exception;

import lombok.extern.slf4j.Slf4j;
import ro.sapientia.furniture.model.enumeration.ResponseStatusCode;

import java.util.Map;

@Slf4j
public class UsedMaterialNotFoundException extends AppHttpException {
    public UsedMaterialNotFoundException(final Class<?> clazz, final Map<String, String> filterValues) {
        super(ResponseStatusCode.RECORD_NOT_FOUND, clazz, filterValues);
        log.warn("Record {} not found in table {}", filterValues, clazz.getSimpleName());
    }

    public UsedMaterialNotFoundException(final Class<?> clazz) {
        super(ResponseStatusCode.RECORD_NOT_FOUND, clazz);
        log.warn("{} table is empty!", clazz.getSimpleName());
    }
}
