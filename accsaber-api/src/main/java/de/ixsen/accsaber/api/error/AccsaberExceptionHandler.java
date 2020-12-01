package de.ixsen.accsaber.api.error;

import de.ixsen.accsaber.business.exceptions.AccsaberOperationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class AccsaberExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AccsaberOperationException.class)
    public ResponseEntity<ErrorDto> handlePlayerAlreadySignedUp(AccsaberOperationException exception) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setErrorCode(exception.getErrorCode());
        errorDto.setMessage(exception.getMessage());

        return ResponseEntity.status(this.getStatus(exception.getErrorCode())).body(errorDto);
    }

    private HttpStatus getStatus(String errorCode) {
        switch (errorCode) {
            case "00100":
            case "00101":
                return HttpStatus.NOT_FOUND;
            default:
                return HttpStatus.BAD_REQUEST;
        }
    }

}
