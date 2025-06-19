package bookstore.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.Date;

@Data
@Schema(description = "DTO для продажи")
public class SaleDto {
    @Schema(description = "ID продажи", example = "1")
    private Long id;

    @Schema(description = "ID пользователя", example = "1")
    private Long userId;

    @Schema(description = "ID книги", example = "1")
    private Long bookId;

    @Schema(description = "Дата продажи", example = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date date;
} 