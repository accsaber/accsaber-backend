package de.ixsen.accsaber.business.exceptions;

public class FetchScoreException extends RuntimeException {

    public FetchScoreException() {
    }

    public FetchScoreException(String message) {
        super(message);
    }

    public FetchScoreException(String message, Throwable cause) {
        super(message, cause);
    }

    public FetchScoreException(Throwable cause) {
        super(cause);
    }

    public FetchScoreException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
