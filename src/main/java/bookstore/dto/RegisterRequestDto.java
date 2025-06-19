package bookstore.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema(description = "DTO для запроса регистрации")
public class RegisterRequestDto {
    @NotBlank(message = "Имя пользователя не может быть пустым")
    @Schema(description = "Имя пользователя", example = "john_doe")
    private String username;

    @NotBlank(message = "Пароль не может быть пустым")
    @Schema(description = "Пароль пользователя", example = "password123")
    private String password;

    @NotBlank(message = "Роль не может быть пустой")
    @Pattern(regexp = "^(USER|ADMIN|MODERATOR)$", message = "Роль должна быть одной из: USER, ADMIN, MODERATOR")
    @Schema(description = "Роль пользователя", example = "USER", allowableValues = {"USER", "ADMIN", "MODERATOR"})
    private String role;
} 