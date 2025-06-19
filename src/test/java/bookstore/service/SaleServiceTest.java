package bookstore.service;

import bookstore.dto.SaleDto;
import bookstore.model.Book;
import bookstore.model.Sale;
import bookstore.model.User;
import bookstore.repository.BookRepository;
import bookstore.repository.SaleRepository;
import bookstore.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaleServiceTest {

    @Mock
    private SaleRepository saleRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private SaleService saleService;

    private Sale testSale;
    private SaleDto testSaleDto;
    private User testUser;
    private Book testBook;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testUser");

        testBook = new Book();
        testBook.setId(1L);
        testBook.setTitle("Test Book");

        testSale = new Sale();
        testSale.setId(1L);
        testSale.setUser(testUser);
        testSale.setBook(testBook);
        testSale.setDate(new Date());

        testSaleDto = new SaleDto();
        testSaleDto.setId(1L);
        testSaleDto.setUserId(1L);
        testSaleDto.setBookId(1L);
        testSaleDto.setDate(new Date());
    }

    @Test
    void getAllSales_ShouldReturnListOfSales() {
        // Arrange
        List<Sale> sales = Arrays.asList(testSale);
        when(saleRepository.findAll()).thenReturn(sales);

        // Act
        List<SaleDto> result = saleService.getAllSales();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testSale.getUser().getId(), result.get(0).getUserId());
        assertEquals(testSale.getBook().getId(), result.get(0).getBookId());
        verify(saleRepository, times(1)).findAll();
    }

    @Test
    void getUserSales_ShouldReturnListOfUserSales() {
        // Arrange
        List<Sale> sales = Arrays.asList(testSale);
        when(saleRepository.findByUserId(1L)).thenReturn(sales);

        // Act
        List<SaleDto> result = saleService.getUserSales(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testSale.getUser().getId(), result.get(0).getUserId());
        assertEquals(testSale.getBook().getId(), result.get(0).getBookId());
        verify(saleRepository, times(1)).findByUserId(1L);
    }

    @Test
    void createSale_ShouldReturnCreatedSale() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(saleRepository.save(any(Sale.class))).thenReturn(testSale);

        // Act
        SaleDto result = saleService.createSale(testSaleDto);

        // Assert
        assertNotNull(result);
        assertEquals(testSaleDto.getUserId(), result.getUserId());
        assertEquals(testSaleDto.getBookId(), result.getBookId());
        verify(saleRepository, times(1)).save(any(Sale.class));
    }

    @Test
    void createSale_ShouldThrowException_WhenUserNotFound() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> saleService.createSale(testSaleDto));
        verify(saleRepository, never()).save(any(Sale.class));
    }

    @Test
    void createSale_ShouldThrowException_WhenBookNotFound() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> saleService.createSale(testSaleDto));
        verify(saleRepository, never()).save(any(Sale.class));
    }

    @Test
    void createSale_ShouldMapDtoToEntityCorrectly() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(saleRepository.save(any(Sale.class))).thenAnswer(invocation -> {
            Sale savedSale = invocation.getArgument(0);
            assertEquals(testSaleDto.getUserId(), savedSale.getUser().getId());
            assertEquals(testSaleDto.getBookId(), savedSale.getBook().getId());
            assertNotNull(savedSale.getDate());
            return savedSale;
        });

        // Act
        SaleDto result = saleService.createSale(testSaleDto);

        // Assert
        assertNotNull(result);
        verify(saleRepository, times(1)).save(any(Sale.class));
    }
} 