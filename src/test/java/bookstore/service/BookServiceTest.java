package bookstore.service;

import bookstore.dto.BookDto;
import bookstore.model.Book;
import bookstore.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book testBook;
    private BookDto testBookDto;

    @BeforeEach
    void setUp() {
        testBook = new Book();
        testBook.setId(1L);
        testBook.setTitle("Test Book");
        testBook.setAuthor("Test Author");
        testBook.setPrice(99.99);

        testBookDto = new BookDto();
        testBookDto.setId(1L);
        testBookDto.setTitle("Test Book");
        testBookDto.setAuthor("Test Author");
        testBookDto.setPrice(99.99);
    }

    @Test
    void getAllBooks_ShouldReturnListOfBooks() {
        // Arrange
        List<Book> books = Arrays.asList(testBook);
        when(bookRepository.findAll()).thenReturn(books);

        // Act
        List<BookDto> result = bookService.getAllBooks();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testBook.getTitle(), result.get(0).getTitle());
        assertEquals(testBook.getAuthor(), result.get(0).getAuthor());
        assertEquals(testBook.getPrice(), result.get(0).getPrice());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void createBook_ShouldReturnCreatedBook() {
        // Arrange
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);

        // Act
        BookDto result = bookService.createBook(testBookDto);

        // Assert
        assertNotNull(result);
        assertEquals(testBookDto.getTitle(), result.getTitle());
        assertEquals(testBookDto.getAuthor(), result.getAuthor());
        assertEquals(testBookDto.getPrice(), result.getPrice());
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void createBook_ShouldMapDtoToEntityCorrectly() {
        // Arrange
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> {
            Book savedBook = invocation.getArgument(0);
            assertEquals(testBookDto.getTitle(), savedBook.getTitle());
            assertEquals(testBookDto.getAuthor(), savedBook.getAuthor());
            assertEquals(testBookDto.getPrice(), savedBook.getPrice());
            return savedBook;
        });

        // Act
        BookDto result = bookService.createBook(testBookDto);

        // Assert
        assertNotNull(result);
        verify(bookRepository, times(1)).save(any(Book.class));
    }
} 