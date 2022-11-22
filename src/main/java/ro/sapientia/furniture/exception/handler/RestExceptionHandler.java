package ro.sapientia.furniture.exception.handler;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ro.sapientia.furniture.exception.AppHttpException;
import ro.sapientia.furniture.model.dto.ErrorDto;

import java.util.Collections;
import java.util.Objects;

@Log4j2
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String EMPTY_TABLE = " table is empty!";


    @ExceptionHandler({AppHttpException.class})
    public final ResponseEntity<Object> appHttpExceptionHandler(final AppHttpException appHttpException) {
        final ErrorDto errorDto;
        if (appHttpException.getParams().isEmpty()) {
            errorDto = new ErrorDto(
                Objects.nonNull(appHttpException.getResponseStatusCode()) ? appHttpException.getResponseStatusCode().getHttpStatus().value() : 500,
                Objects.nonNull(appHttpException.getResponseStatusCode()) ? appHttpException.getResponseStatusCode().toString() : null,
                appHttpException.getClazz().getSimpleName() + EMPTY_TABLE,
                Collections.emptyMap()
            );
        } else {
            errorDto = new ErrorDto(
                Objects.nonNull(appHttpException.getResponseStatusCode()) ? appHttpException.getResponseStatusCode().getHttpStatus().value() : 500,
                Objects.nonNull(appHttpException.getResponseStatusCode()) ? appHttpException.getResponseStatusCode().toString() : null,
                appHttpException.getResponseStatusCode().getMessage() + appHttpException.getClazz().getSimpleName(),
                appHttpException.getParams()
            );
        }
        final var httpStatusCode = appHttpException.getResponseStatusCode().getHttpStatus();
        log.error(appHttpException.getResponseStatusCode().getMessage(), errorDto, appHttpException);
        return new ResponseEntity<>(errorDto, httpStatusCode);
    }
}
