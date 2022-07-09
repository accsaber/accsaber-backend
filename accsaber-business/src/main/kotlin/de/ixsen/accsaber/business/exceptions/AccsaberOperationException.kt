package de.ixsen.accsaber.business.exceptions

class AccsaberOperationException : RuntimeException {
    val errorCode: String

    constructor(exceptionType: ExceptionType, message: String) : super(message) {
        errorCode = exceptionType.errorCode
    }

    constructor(exceptionType: ExceptionType, message: String, cause: Throwable) : super(message, cause) {
        errorCode = exceptionType.errorCode
    }
}