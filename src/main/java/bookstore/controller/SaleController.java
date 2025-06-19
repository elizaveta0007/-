package bookstore.controller;

import bookstore.dto.SaleDto;
import bookstore.service.SaleService;
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
@RequestMapping("/api/sales")
@RequiredArgsConstructor
@Tag(name = "Sale Controller", description = "API для управления продажами")
@SecurityRequirement(name = "bearerAuth")
public class SaleController {

    private final SaleService saleService;

    @GetMapping
    @Operation(summary = "Получить все продажи", description = "Возвращает список всех продаж в системе")
    @ApiResponse(responseCode = "200", description = "Список продаж успешно получен")
    public ResponseEntity<List<SaleDto>> getAllSales() {
        return ResponseEntity.ok(saleService.getAllSales());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить продажу по ID", description = "Возвращает продажу по указанному идентификатору")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Продажа успешно найдена"),
        @ApiResponse(responseCode = "404", description = "Продажа не найдена")
    })
    public ResponseEntity<SaleDto> getSaleById(
            @Parameter(description = "ID продажи", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(saleService.getSaleById(id));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Получить продажи пользователя", description = "Возвращает список продаж для указанного пользователя")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Список продаж успешно получен"),
        @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    public ResponseEntity<List<SaleDto>> getUserSales(
            @Parameter(description = "ID пользователя", required = true)
            @PathVariable Long userId) {
        return ResponseEntity.ok(saleService.getUserSales(userId));
    }

    @PostMapping
    @Operation(summary = "Создать новую продажу", description = "Создает новую продажу в системе")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Продажа успешно создана"),
        @ApiResponse(responseCode = "400", description = "Некорректные данные продажи"),
        @ApiResponse(responseCode = "404", description = "Книга или пользователь не найдены")
    })
    public ResponseEntity<SaleDto> createSale(
            @Parameter(description = "Данные продажи", required = true)
            @Valid @RequestBody SaleDto saleDto) {
        return new ResponseEntity<>(saleService.createSale(saleDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить продажу", description = "Обновляет существующую продажу по ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Продажа успешно обновлена"),
        @ApiResponse(responseCode = "404", description = "Продажа не найдена"),
        @ApiResponse(responseCode = "400", description = "Некорректные данные продажи")
    })
    public ResponseEntity<SaleDto> updateSale(
            @Parameter(description = "ID продажи", required = true)
            @PathVariable Long id,
            @Parameter(description = "Обновленные данные продажи", required = true)
            @Valid @RequestBody SaleDto saleDto) {
        return ResponseEntity.ok(saleService.updateSale(id, saleDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить продажу", description = "Удаляет продажу по указанному ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Продажа успешно удалена"),
        @ApiResponse(responseCode = "404", description = "Продажа не найдена")
    })
    public ResponseEntity<Void> deleteSale(
            @Parameter(description = "ID продажи", required = true)
            @PathVariable Long id) {
        saleService.deleteSale(id);
        return ResponseEntity.noContent().build();
    }
}
