package bookstore.service;

import bookstore.dto.AuthRequestDto;
import bookstore.dto.AuthResponseDto;
import bookstore.dto.RegisterRequestDto;
import bookstore.model.User;
import bookstore.repository.UserRepository;
import bookstore.util.JwtUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthResponseDto authenticate(AuthRequestDto request) {
        log.info("Attempting to authenticate user: {}", request.getUsername());
        
        try {
            // Проверяем существование пользователя и правильность пароля
            User user = userRepository.findByUsername(request.getUsername());
            if (user == null) {
                log.error("User not found: {}", request.getUsername());
                throw new EntityNotFoundException("Пользователь не найден");
            }

            log.info("Found user in database: {}", user.getUsername());
            log.info("Stored password hash: {}", user.getPassword());
            log.info("Provided password matches: {}", passwordEncoder.matches(request.getPassword(), user.getPassword()));

            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            log.info("Authentication successful for user: {}", request.getUsername());

            String token = jwtUtil.generateToken(user.getUsername());
            log.info("JWT token generated successfully for user: {}", user.getUsername());
            
            return new AuthResponseDto(token);
        } catch (Exception e) {
            log.error("Authentication failed for user: {}", request.getUsername(), e);
            throw e;
        }
    }

    @Transactional
    public AuthResponseDto register(RegisterRequestDto request) {
        log.info("Attempting to register new user: {}", request.getUsername());

        // Проверяем, не существует ли уже пользователь с таким именем
        if (userRepository.findByUsername(request.getUsername()) != null) {
            log.error("User already exists: {}", request.getUsername());
            throw new IllegalArgumentException("Пользователь с таким именем уже существует");
        }

        // Создаем нового пользователя
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        // Сохраняем пользователя
        User savedUser = userRepository.save(user);
        log.info("User registered successfully: {}", savedUser.getUsername());

        // Генерируем токен для автоматической аутентификации
        String token = jwtUtil.generateToken(savedUser.getUsername());
        log.info("JWT token generated for new user: {}", savedUser.getUsername());

        return new AuthResponseDto(token);
    }
} 