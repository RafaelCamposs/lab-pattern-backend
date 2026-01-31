package com.example.application.exception

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import java.time.LocalDateTime

@RestControllerAdvice
class GlobalExceptionHandler {

    private val logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(PromptLoadException::class)
    fun handlePromptLoadException(
        ex: PromptLoadException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        logger.error("Prompt load error: ${ex.message}", ex)

        val errorResponse = ErrorResponse(
            timestamp = LocalDateTime.now(),
            status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            error = "Erro Interno do Servidor",
            message = "Falha ao carregar configuração de prompt",
            path = request.getDescription(false).removePrefix("uri=")
        )

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse)
    }

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNoSuchElementException(
        ex: NoSuchElementException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        logger.warn("Resource not found: ${ex.message}")

        val errorResponse = ErrorResponse(
            timestamp = LocalDateTime.now(),
            status = HttpStatus.NOT_FOUND.value(),
            error = "Não Encontrado",
            message = ex.message ?: "Recurso não encontrado",
            path = request.getDescription(false).removePrefix("uri=")
        )

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(
        ex: IllegalArgumentException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        logger.warn("Invalid argument: ${ex.message}")

        val errorResponse = ErrorResponse(
            timestamp = LocalDateTime.now(),
            status = HttpStatus.BAD_REQUEST.value(),
            error = "Requisição Inválida",
            message = ex.message ?: "Parâmetros de requisição inválidos",
            path = request.getDescription(false).removePrefix("uri=")
        )

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse)
    }

    @ExceptionHandler(IllegalStateException::class)
    fun handleIllegalStateException(
        ex: IllegalStateException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        logger.error("Illegal state: ${ex.message}", ex)

        val errorResponse = ErrorResponse(
            timestamp = LocalDateTime.now(),
            status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            error = "Erro Interno do Servidor",
            message = "Ocorreu um erro inesperado",
            path = request.getDescription(false).removePrefix("uri=")
        )

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse)
    }

    @ExceptionHandler(AuthenticationException::class, BadCredentialsException::class)
    fun handleAuthenticationException(
        ex: Exception,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        logger.warn("Authentication failed: ${ex.message}")

        val errorResponse = ErrorResponse(
            timestamp = LocalDateTime.now(),
            status = HttpStatus.UNAUTHORIZED.value(),
            error = "Não Autorizado",
            message = "Email ou senha inválidos",
            path = request.getDescription(false).removePrefix("uri=")
        )

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(
        ex: MethodArgumentNotValidException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        val errors = ex.bindingResult.fieldErrors.joinToString(", ") { "${it.field}: ${it.defaultMessage}" }

        logger.warn("Validation failed: $errors")

        val errorResponse = ErrorResponse(
            timestamp = LocalDateTime.now(),
            status = HttpStatus.BAD_REQUEST.value(),
            error = "Requisição Inválida",
            message = "Falha na validação: $errors",
            path = request.getDescription(false).removePrefix("uri=")
        )

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse)
    }

    @ExceptionHandler(Exception::class)
    fun handleGlobalException(
        ex: Exception,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        logger.error("Unexpected error: ${ex.message}", ex)

        val errorResponse = ErrorResponse(
            timestamp = LocalDateTime.now(),
            status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            error = "Erro Interno do Servidor",
            message = "Ocorreu um erro inesperado",
            path = request.getDescription(false).removePrefix("uri=")
        )

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse)
    }
}

data class ErrorResponse(
    val timestamp: LocalDateTime,
    val status: Int,
    val error: String,
    val message: String,
    val path: String
)
