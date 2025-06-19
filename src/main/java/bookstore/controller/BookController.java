package bookstore.controller;

import bookstore.dto.BookDto;
import bookstore.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
@Tag(name = "Book Controller", description = "API для управления книгами")
@SecurityRequirement(name = "bearerAuth")
public class BookController {

    private final BookService bookService;

    @GetMapping
    @Operation(summary = "Получить все книги", description = "Возвращает список всех книг в системе")
    @ApiResponse(responseCode = "200", description = "Список книг успешно получен")
    public ResponseEntity<List<BookDto>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить книгу по ID", description = "Возвращает книгу по указанному идентификатору")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Книга успешно найдена"),
        @ApiResponse(responseCode = "404", description = "Книга не найдена")
    })
    public ResponseEntity<BookDto> getBookById(
            @Parameter(description = "ID книги", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @PostMapping
    @Operation(summary = "Создать новую книгу", description = "Создает новую книгу в системе")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Книга успешно создана"),
        @ApiResponse(responseCode = "400", description = "Некорректные данные книги")
    })
    public ResponseEntity<BookDto> createBook(
            @Parameter(description = "Данные книги", required = true)
            @Valid @RequestBody BookDto bookDto) {
        return new ResponseEntity<>(bookService.createBook(bookDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить книгу", description = "Обновляет существующую книгу по ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Книга успешно обновлена"),
        @ApiResponse(responseCode = "404", description = "Книга не найдена"),
        @ApiResponse(responseCode = "400", description = "Некорректные данные книги")
    })
    public ResponseEntity<BookDto> updateBook(
            @Parameter(description = "ID книги", required = true)
            @PathVariable Long id,
            @Parameter(description = "Обновленные данные книги", required = true)
            @Valid @RequestBody BookDto bookDto) {
        return ResponseEntity.ok(bookService.updateBook(id, bookDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить книгу", description = "Удаляет книгу по указанному ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Книга успешно удалена"),
        @ApiResponse(responseCode = "404", description = "Книга не найдена")
    })
    public ResponseEntity<Void> deleteBook(
            @Parameter(description = "ID книги", required = true)
            @PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}
