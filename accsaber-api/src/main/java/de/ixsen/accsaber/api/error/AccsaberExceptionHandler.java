package de.ixsen.accsaber.api.error;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import de.ixsen.accsaber.business.HasLogger;
import de.ixsen.accsaber.business.exceptions.AccsaberOperationException;
import de.ixsen.accsaber.business.exceptions.ExceptionType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class AccsaberExceptionHandler extends ResponseEntityExceptionHandler implements HasLogger {

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ErrorDto> handleInvalidToken(JWTVerificationException exception) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setErrorCode(ExceptionType.AUTH_INVALID.getErrorCode());
        errorDto.setMessage("Invalid authentication");

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDto);
    }

    @ExceptionHandler(AccsaberOperationException.class)
    public ResponseEntity<ErrorDto> handleAccsaberOperationException(AccsaberOperationException exception) {
        this.getLogger().warn(exception.getMessage());
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
