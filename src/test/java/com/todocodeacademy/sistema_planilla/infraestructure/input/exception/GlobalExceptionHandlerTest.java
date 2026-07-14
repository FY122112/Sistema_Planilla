package com.todocodeacademy.sistema_planilla.infraestructure.input.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Antes de este handler, todas estas excepciones terminaban como HTTP 500 genérico
 * (comportamiento por defecto de Spring Boot sin @ControllerAdvice). Se verifica que
 * cada tipo de excepción de negocio se traduzca al código HTTP correcto.
 */
@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @Mock
    private HttpServletRequest request;

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void handleIllegalArgument_debeResponder400() {
        when(request.getRequestURI()).thenReturn("/api/empleados");

        ResponseEntity<ErrorResponse> response =
                handler.handleIllegalArgument(new IllegalArgumentException("DNI inválido"), request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().message()).isEqualTo("DNI inválido");
    }

    @Test
    void handleIllegalState_debeResponder409() {
        when(request.getRequestURI()).thenReturn("/api/planillas/1");

        ResponseEntity<ErrorResponse> response =
                handler.handleIllegalState(new IllegalStateException("La planilla está cerrada"), request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody().message()).isEqualTo("La planilla está cerrada");
    }

    @Test
    void handleRuntime_debeResponder404() {
        when(request.getRequestURI()).thenReturn("/api/planillas/99");

        ResponseEntity<ErrorResponse> response =
                handler.handleRuntime(new RuntimeException("Planilla no encontrada"), request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().message()).isEqualTo("Planilla no encontrada");
    }

    @Test
    void handleAuthentication_debeResponder401() {
        when(request.getRequestURI()).thenReturn("/auth/login");

        ResponseEntity<ErrorResponse> response =
                handler.handleAuthentication(new BadCredentialsException("Credenciales inválidas"), request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void handleGeneric_debeResponder500_sinExponerElMensajeInterno() {
        when(request.getRequestURI()).thenReturn("/api/empleados");

        ResponseEntity<ErrorResponse> response =
                handler.handleGeneric(new RuntimeException("NullPointerException en línea 42"), request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody().message()).isEqualTo("Error interno del servidor");
    }

    @Test
    void handleValidation_debeResponder400_conDetallePorCampo() {
        when(request.getRequestURI()).thenReturn("/api/empleados");

        BeanPropertyBindingResult bindingResult =
                new BeanPropertyBindingResult(new Object(), "createEmpleadoRequest");
        bindingResult.addError(new FieldError(
                "createEmpleadoRequest", "numeroDocumento", "no puede estar vacío"
        ));

        MethodArgumentNotValidException ex =
                new MethodArgumentNotValidException(mock(MethodParameter.class), bindingResult);

        ResponseEntity<ErrorResponse> response = handler.handleValidation(ex, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().details())
                .containsExactly("numeroDocumento: no puede estar vacío");
    }
}
