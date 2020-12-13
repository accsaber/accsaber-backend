package de.ixsen.accsaber.business.exceptions;

public class AccsaberOperationException extends RuntimeException {

    private final String errorCode;

    public AccsaberOperationException(ExceptionType exceptionType, String message) {
        super(message);
        this.errorCode = exceptionType.getErrorCode();
    }

    public AccsaberOperationException(ExceptionType exceptionType, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = exceptionType.getErrorCode();
    }

    public String getErrorCode() {
        return errorCode;
    }
}
