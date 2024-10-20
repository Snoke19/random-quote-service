package quotopia.randomquoteservice.controllers.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.method.ParameterErrors;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.MatrixVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import quotopia.randomquoteservice.exceptions.EntityNotFoundException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    private static final String NOT_EMPTY = "NotEmpty";

    private final MessageSource messageSource;

    public ControllerAdvice(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ResponseError> entityNotFoundException(EntityNotFoundException exception, WebRequest request) {

        Map<String, String> errorsDetails = new HashMap<>();
        ResponseError errorResponse = new ResponseError(
                "NOT_FOUND_ENTITY",
                HttpStatus.NOT_FOUND.value(),
                exception.getMessage(),
                errorsDetails,
                request.getDescription(false),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({DataAccessException.class})
    public ResponseEntity<ResponseError> dataAccessException(DataAccessException exception, WebRequest request) {

        log.error("Error: {}", exception.getMessage());

        Map<String, String> errorsDetails = new HashMap<>();
        ResponseError errorResponse = new ResponseError(
                "Server Error",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Something went wrong",
                errorsDetails,
                request.getDescription(false),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({MissingServletRequestParameterException.class})
    public ResponseEntity<ResponseError> error(MissingServletRequestParameterException exception, WebRequest request) {

        Map<String, String> errorsDetails = new HashMap<>();
        ResponseError errorResponse = new ResponseError(
                "Validation",
                HttpStatus.BAD_REQUEST.value(),
                exception.getMessage(),
                errorsDetails,
                request.getDescription(false),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({HandlerMethodValidationException.class})
    public ResponseEntity<ResponseError> error(HandlerMethodValidationException exception, WebRequest request) {

        Map<String, String> errorsDetails = new HashMap<>();
        AtomicReference<String> typeError = new AtomicReference<>("Validation");

        exception.visitResults(new HandlerMethodValidationException.Visitor() {
            @Override
            public void cookieValue(CookieValue cookieValue, ParameterValidationResult result) {
                log.warn("Application does not handle cookies!");
            }

            @Override
            public void matrixVariable(MatrixVariable matrixVariable, ParameterValidationResult result) {
                log.warn("Application does not handle matrix variable!");
            }

            @Override
            public void modelAttribute(ModelAttribute modelAttribute, ParameterErrors errors) {
                log.warn("Application does not handle model attribute!");
            }

            @Override
            public void pathVariable(PathVariable pathVariable, ParameterValidationResult result) {
                log.warn("Application does not handle path variable!");
            }

            @Override
            public void requestBody(RequestBody requestBody, ParameterErrors errors) {
                log.warn("Application does not handle request body!");
            }

            @Override
            public void requestHeader(RequestHeader requestHeader, ParameterValidationResult result) {
                log.warn("Application does not handle request's header!");
            }

            @Override
            public void requestParam(RequestParam requestParam, ParameterValidationResult result) {
                result.getResolvableErrors().forEach(error -> {
                    List<String> codes = Arrays.asList(Objects.requireNonNull(error.getCodes()));
                    if (codes.contains(NOT_EMPTY)) {
                        typeError.set("NOT_EMPTY_VALIDATION");
                    }
                    errorsDetails.put(requestParam.name(), messageSource.getMessage(error, request.getLocale()));
                });
            }

            @Override
            public void requestPart(RequestPart requestPart, ParameterErrors errors) {
                log.warn("Application does not handle request's part!");
            }

            @Override
            public void other(ParameterValidationResult result) {
                log.warn("Application does not handle other!");
            }
        });

        ResponseError errorResponse = new ResponseError(
                typeError.get(),
                HttpStatus.BAD_REQUEST.value(),
                exception.getMessage(),
                errorsDetails,
                request.getDescription(false),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
