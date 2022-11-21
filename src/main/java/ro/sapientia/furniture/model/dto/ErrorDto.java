package ro.sapientia.furniture.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDto {

    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private Map<String, String> params;

    public ErrorDto(final int status, final String error, final String message, final Map<String, String> params) {
        this(LocalDateTime.now(), status, error, message, params);
    }

    public LocalDateTime timestamp() {
        return timestamp;
    }

    public int status() {
        return status;
    }

    public String error() {
        return error;
    }

    public String message() {
        return message;
    }

    public Map<String, String> params() {
        return params;
    }
}
