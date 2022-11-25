package ro.sapientia.furniture.exception;

import lombok.Getter;
import ro.sapientia.furniture.model.enumeration.ResponseStatusCode;

import java.util.Collections;
import java.util.Map;

@Getter
public class AppHttpException extends RuntimeException {

    protected final ResponseStatusCode responseStatusCode;
    private final Class<?> clazz;
    private final Map<String, String> params;

    public AppHttpException(final ResponseStatusCode responseStatusCode, final Class<?> clazz, final Map<String, String> params) {
        super(responseStatusCode.getMessage());
        this.responseStatusCode = responseStatusCode;
        this.clazz = clazz;
        this.params = params;
    }

    public AppHttpException(final ResponseStatusCode responseStatusCode, final Class<?> clazz) {
        super(responseStatusCode.getMessage());
        this.responseStatusCode = responseStatusCode;
        this.clazz = clazz;
        this.params = Collections.emptyMap();
    }
}
