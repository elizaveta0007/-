package bookstore.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO для пользователя")
public class UserDto {
    @Schema(description = "ID пользователя", example = "1")
    private Long id;

    @Schema(description = "Имя пользователя", example = "john_doe")
    private String username;

    @Schema(description = "Роль пользователя", example = "USER", allowableValues = {"ADMIN", "MODERATOR", "USER"})
    private String role;
} 