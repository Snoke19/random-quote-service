package quotopia.randomquoteservice.controllers.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseError {

    private String type;
    private int code;
    private String message;
    private Object details;
    private String path;
    private LocalDateTime timestamp;
}
