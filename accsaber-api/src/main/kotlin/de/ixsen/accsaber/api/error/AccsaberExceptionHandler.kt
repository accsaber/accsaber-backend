package de.ixsen.accsaber.api.error

import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import de.ixsen.accsaber.business.HasLogger
import de.ixsen.accsaber.business.exceptions.AccsaberOperationException
import de.ixsen.accsaber.business.exceptions.ExceptionType
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class AccsaberExceptionHandler : ResponseEntityExceptionHandler(), HasLogger {
    @ExceptionHandler(TokenExpiredException::class)
    fun handleInvalidToken(exception: JWTVerificationException?): ResponseEntity<ErrorDto> {
        val errorDto = ErrorDto()
        errorDto.errorCode = ExceptionType.AUTH_INVALID.errorCode
        errorDto.message = "Invalid authentication"
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDto)
    }

    @ExceptionHandler(AccsaberOperationException::class)
    fun handleAccsaberOperationException(exception: AccsaberOperationException): ResponseEntity<ErrorDto> {
        logger.warn(exception.message)
        val errorDto = ErrorDto()
        errorDto.errorCode = exception.errorCode
        errorDto.message = exception.message
        return ResponseEntity.status(getStatus(exception.errorCode)).body(errorDto)
    }

    private fun getStatus(errorCode: String): HttpStatus {
        return when (errorCode) {
            "00100", "00101" -> HttpStatus.NOT_FOUND
            else -> HttpStatus.BAD_REQUEST
        }
    }
}