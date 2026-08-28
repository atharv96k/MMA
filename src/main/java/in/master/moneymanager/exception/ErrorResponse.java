package in.master.moneymanager.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Structured JSON body returned for every handled error, instead of a raw
 * stack trace. Keeps the shape consistent across all failure types so the
 * frontend only needs to handle one error format.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    private int status;
    private String error;
    private String message;
    private String path;

    /** Only populated for validation errors: field name -> problem message */
    private Map<String, String> fieldErrors;
}
