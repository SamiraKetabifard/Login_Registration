package com.example.loginregistration;

import com.example.loginregistration.entity.User;
import com.example.loginregistration.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindByUsernameOrEmail() {
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password");
        entityManager.persist(user);
        entityManager.flush();

        User found = userRepository.findByUsernameOrEmail("testuser", "testuser").orElse(null);
        assertNotNull(found);
        assertEquals("test@example.com", found.getEmail());
    }

    @Test
    public void testExistsByUsername() {
        User user = new User();
        user.setUsername("existinguser");
        user.setEmail("existing@example.com");
        entityManager.persist(user);

        boolean exists = userRepository.existsByUsername("existinguser");
        assertTrue(exists);
    }
}
