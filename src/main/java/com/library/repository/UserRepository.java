package com.library.repository;

import com.library.model.User;
import com.library.model.Role;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class UserRepository {
    private final ConcurrentHashMap<Long, User> users = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserRepository() {
        // Добавляем тестовые данные с захешированным паролем "test123"
        String testPassword = passwordEncoder.encode("test123");
        log.debug("Creating test users with encoded password");

        User admin = User.builder()
                .id(idGenerator.getAndIncrement())
                .username("admin")
                .email("admin@library.com")
                .passwordHash(testPassword)
                .role(Role.ADMIN)
                .createdAt(LocalDateTime.now())
                .build();

        User librarian = User.builder()
                .id(idGenerator.getAndIncrement())
                .username("librarian")
                .email("librarian@library.com")
                .passwordHash(testPassword)
                .role(Role.LIBRARIAN)
                .createdAt(LocalDateTime.now())
                .build();

        User reader = User.builder()
                .id(idGenerator.getAndIncrement())
                .username("reader")
                .email("reader@library.com")
                .passwordHash(testPassword)
                .role(Role.READER)
                .createdAt(LocalDateTime.now())
                .build();

        users.put(admin.getId(), admin);
        users.put(librarian.getId(), librarian);
        users.put(reader.getId(), reader);
        
        log.debug("Test users created. Admin password hash: {}", testPassword);
    }

    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    public Optional<User> findById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    public Optional<User> findByUsername(String username) {
        return users.values().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }

    public User save(User user) {
        if (user.getId() == null) {
            user.setId(idGenerator.getAndIncrement());
        }
        users.put(user.getId(), user);
        return user;
    }

    public boolean existsByUsername(String username) {
        return users.values().stream()
                .anyMatch(user -> user.getUsername().equals(username));
    }

    public boolean existsByEmail(String email) {
        return users.values().stream()
                .anyMatch(user -> user.getEmail().equals(email));
    }

    public List<User> findByRole(Role role) {
        return users.values().stream()
                .filter(user -> user.getRole() == role)
                .collect(Collectors.toList());
    }
} 