package bookstore.controller;

import bookstore.dto.AuthRequestDto;
import bookstore.dto.AuthResponseDto;
import bookstore.dto.RegisterRequestDto;
import bookstore.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Аутентификация", description = "API для аутентификации и регистрации пользователей")
public class AuthController {
    private final AuthService authService;

    @Operation(
        summary = "Аутентификация пользователя",
        description = "Аутентифицирует пользователя и возвращает JWT токен"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Успешная аутентификация",
            content = @Content(schema = @Schema(implementation = AuthResponseDto.class))
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Неверные учетные данные"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Пользователь не найден"
        )
    })
    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(
        @Parameter(description = "Данные пользователя для аутентификации", required = true)
        @Valid @RequestBody AuthRequestDto request
    ) {
        try {
            log.info("Received authentication request for user: {}", request.getUsername());
            AuthResponseDto response = authService.authenticate(request);
            log.info("Authentication successful for user: {}", request.getUsername());
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            log.error("Invalid credentials for user: {}", request.getUsername(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Неверные учетные данные");
        } catch (AuthenticationException e) {
            log.error("Authentication failed for user: {}", request.getUsername(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Ошибка аутентификации: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error during authentication for user: {}", request.getUsername(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Внутренняя ошибка сервера");
        }
    }

    @Operation(
        summary = "Регистрация нового пользователя",
        description = "Регистрирует нового пользователя и возвращает JWT токен"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Пользователь успешно зарегистрирован",
            content = @Content(schema = @Schema(implementation = AuthResponseDto.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Некорректные данные или пользователь уже существует"
        )
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(
        @Parameter(description = "Данные для регистрации", required = true)
        @Valid @RequestBody RegisterRequestDto request
    ) {
        try {
            log.info("Received registration request for user: {}", request.getUsername());
            AuthResponseDto response = authService.register(request);
            log.info("Registration successful for user: {}", request.getUsername());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            log.error("Registration failed for user: {}", request.getUsername(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error during registration for user: {}", request.getUsername(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Внутренняя ошибка сервера");
        }
    }
}
