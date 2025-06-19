package bookstore.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "DTO для запроса аутентификации")
public class AuthRequestDto {
    @NotBlank(message = "Имя пользователя не может быть пустым")
    @Schema(description = "Имя пользователя", example = "john_doe")
    private String username;

    @NotBlank(message = "Пароль не может быть пустым")
    @Schema(description = "Пароль пользователя", example = "password123")
    private String password;
} 