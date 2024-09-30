package quotopia.randomquoteservice.controllers.error;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.method.ParameterErrors;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class ControllerAdvice {

    private final MessageSource messageSource;

    public ControllerAdvice(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler({HandlerMethodValidationException.class})
    public ResponseEntity<ResponseError> error(HandlerMethodValidationException exception, WebRequest request) {

        Map<String, String> errorsDetails = new HashMap<>();

        exception.visitResults(new HandlerMethodValidationException.Visitor() {
            @Override
            public void cookieValue(CookieValue cookieValue, ParameterValidationResult result) {

            }

            @Override
            public void matrixVariable(MatrixVariable matrixVariable, ParameterValidationResult result) {

            }

            @Override
            public void modelAttribute(ModelAttribute modelAttribute, ParameterErrors errors) {

            }

            @Override
            public void pathVariable(PathVariable pathVariable, ParameterValidationResult result) {
                result.getResolvableErrors().forEach(error ->
                        errorsDetails.put(pathVariable.name(), messageSource.getMessage(error, request.getLocale()))
                );
            }

            @Override
            public void requestBody(RequestBody requestBody, ParameterErrors errors) {
                errors.getResolvableErrors().forEach(error ->
                        errorsDetails.put("body", messageSource.getMessage(error, request.getLocale()))
                );
            }

            @Override
            public void requestHeader(RequestHeader requestHeader, ParameterValidationResult result) {

            }

            @Override
            public void requestParam(RequestParam requestParam, ParameterValidationResult result) {
                result.getResolvableErrors().forEach(error ->
                        errorsDetails.put(requestParam.name(), messageSource.getMessage(error, request.getLocale()))
                );
            }

            @Override
            public void requestPart(RequestPart requestPart, ParameterErrors errors) {

            }

            @Override
            public void other(ParameterValidationResult result) {

            }
        });


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
}
