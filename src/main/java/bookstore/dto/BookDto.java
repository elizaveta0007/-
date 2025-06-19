package bookstore.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO для книги")
public class BookDto {
    @Schema(description = "ID книги", example = "1")
    private Long id;

    @Schema(description = "Название книги", example = "Война и мир")
    private String title;

    @Schema(description = "Автор книги", example = "Лев Толстой")
    private String author;

    @Schema(description = "Цена книги", example = "999.99")
    private double price;
} 