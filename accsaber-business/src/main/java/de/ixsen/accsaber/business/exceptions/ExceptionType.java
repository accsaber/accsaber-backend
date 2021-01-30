package de.ixsen.accsaber.business.exceptions;

public enum ExceptionType {
    PLAYER_ALREADY_EXISTS("00001"),
    RANKED_MAP_ALREADY_EXISTS("00002"),
    PLAYER_NOT_FOUND("00100"),
    RANKED_MAP_NOT_FOUND("00101"),
    AUTH_INVALID("10000");

    private final String errorCode;

    ExceptionType(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return this.errorCode;
    }
}
