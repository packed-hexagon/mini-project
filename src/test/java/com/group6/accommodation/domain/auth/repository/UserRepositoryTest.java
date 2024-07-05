package com.group6.accommodation.domain.auth.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.group6.accommodation.domain.accommodation.service.DatabaseInitializationService;
import com.group6.accommodation.domain.auth.model.entity.UserEntity;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private DatabaseInitializationService databaseInitializationService;

    private UserEntity user;

    @BeforeEach
    public void setUp() {
        user = UserEntity.builder()
            .email("test@example.com")
            .encryptedPassword("test")
            .name("Test User")
            .phoneNumber("123-456-7890")
            .build();

        userRepository.save(user);
    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    public void testExistsByEmail() {
        // Existing email in database
        assertTrue(userRepository.existsByEmail("test@example.com"));
    }

    @Test
    public void testExistsByPhoneNumber() {
        // Existing phone number in database
        assertTrue(userRepository.existsByPhoneNumber("123-456-7890"));

        // Non-existing phone number
        assertFalse(userRepository.existsByPhoneNumber("987-654-3210"));
    }

    @Test
    public void testFindByEmail() {
        // Existing email in database
        Optional<UserEntity> optionalUser = userRepository.findByEmail("test@example.com");
        assertTrue(optionalUser.isPresent());
        assertEquals(user.getEmail(), optionalUser.get().getEmail());

        // Non-existing email
        Optional<UserEntity> nonExistentUser = userRepository.findByEmail("nonexistent@example.com");
        assertFalse(nonExistentUser.isPresent());
    }
}