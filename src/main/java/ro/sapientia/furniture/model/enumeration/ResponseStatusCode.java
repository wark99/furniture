package ro.sapientia.furniture.model.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import ro.sapientia.furniture.model.AppMessage;

@Getter
@RequiredArgsConstructor
public enum ResponseStatusCode {

    RECORD_NOT_FOUND(HttpStatus.BAD_REQUEST, AppMessage.RECORD_NOT_FOUND);

    private final HttpStatus httpStatus;
    private final String message;
}
