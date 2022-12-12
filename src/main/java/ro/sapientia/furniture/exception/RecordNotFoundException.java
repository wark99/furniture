package ro.sapientia.furniture.exception;

import lombok.extern.log4j.Log4j2;
import ro.sapientia.furniture.model.enumeration.ResponseStatusCode;

import java.util.Map;

@Log4j2
public class RecordNotFoundException extends AppHttpException {

    public RecordNotFoundException(final Class<?> clazz, final Map<String, String> filterValues) {
        super(ResponseStatusCode.RECORD_NOT_FOUND, clazz, filterValues);
        log.warn("Record {} not found in table {}", filterValues, clazz.getSimpleName());
    }

    public RecordNotFoundException(final Class<?> clazz) {
        super(ResponseStatusCode.RECORD_NOT_FOUND, clazz);
        log.warn("{} table is empty!", clazz.getSimpleName());
    }
}
