package bookstore.service;

import bookstore.dto.SaleDto;
import bookstore.model.Book;
import bookstore.model.Sale;
import bookstore.model.User;
import bookstore.repository.BookRepository;
import bookstore.repository.SaleRepository;
import bookstore.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SaleService {

    private final SaleRepository saleRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public List<SaleDto> getAllSales() {
        return saleRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public SaleDto getSaleById(Long id) {
        return saleRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new EntityNotFoundException("Продажа с ID " + id + " не найдена"));
    }

    public List<SaleDto> getUserSales(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("Пользователь с ID " + userId + " не найден");
        }
        return saleRepository.findByUserId(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public SaleDto createSale(SaleDto saleDto) {
        User user = userRepository.findById(saleDto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с ID " + saleDto.getUserId() + " не найден"));
        Book book = bookRepository.findById(saleDto.getBookId())
                .orElseThrow(() -> new EntityNotFoundException("Книга с ID " + saleDto.getBookId() + " не найдена"));

        Sale sale = new Sale();
        sale.setUser(user);
        sale.setBook(book);
        sale.setDate(new Date());

        Sale savedSale = saleRepository.save(sale);
        return convertToDto(savedSale);
    }

    public SaleDto updateSale(Long id, SaleDto saleDto) {
        Sale existingSale = saleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Продажа с ID " + id + " не найдена"));

        User user = userRepository.findById(saleDto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с ID " + saleDto.getUserId() + " не найден"));
        Book book = bookRepository.findById(saleDto.getBookId())
                .orElseThrow(() -> new EntityNotFoundException("Книга с ID " + saleDto.getBookId() + " не найдена"));

        existingSale.setUser(user);
        existingSale.setBook(book);
        existingSale.setDate(saleDto.getDate() != null ? saleDto.getDate() : new Date());

        Sale updatedSale = saleRepository.save(existingSale);
        return convertToDto(updatedSale);
    }

    public void deleteSale(Long id) {
        if (!saleRepository.existsById(id)) {
            throw new EntityNotFoundException("Продажа с ID " + id + " не найдена");
        }
        saleRepository.deleteById(id);
    }

    private SaleDto convertToDto(Sale sale) {
        SaleDto dto = new SaleDto();
        dto.setId(sale.getId());
        dto.setUserId(sale.getUser().getId());
        dto.setBookId(sale.getBook().getId());
        dto.setDate(sale.getDate());
        return dto;
    }
} 