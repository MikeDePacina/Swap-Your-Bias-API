package com.swapyourbias.repository;

import com.swapyourbias.model.User;
import com.swapyourbias.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;


    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setUsername("C");
        user.setEmail("grimes@example.com");
        user.setPassword("Genesis");
        userRepository.save(user);
    }


    @Test
    public void testFindByUsername_Found() {
        Optional<User> result = userRepository.findByUsername("C");

        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo("C");
        assertThat(result.get().getEmail()).isEqualTo("grimes@example.com");
        assertThat(result.get().getPassword()).isEqualTo("Genesis");
    }

    @Test
    public void testFindByUsername_NotFound(){
        Optional<User> result = userRepository.findByUsername("eheads");

        assertThat(result).isEmpty();
    }

    @Test
    public void testFindByEmail_Found() {
        Optional<User> result = userRepository.findByEmail("grimes@example.com");

        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo("C");
        assertThat(result.get().getEmail()).isEqualTo("grimes@example.com");
        assertThat(result.get().getPassword()).isEqualTo("Genesis");
    }

    @Test
    public void testFindByEmail_NotFound(){
        Optional<User> result = userRepository.findByUsername("eheads@gmail.com");

        assertThat(result).isEmpty();
    }
    @Test
    public void testFindByUsernameOrEmail_WithUsernameMatch() {
        Optional<User> result = userRepository.findByUsernameOrEmail("C", "wrong@example.com");

        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo("C");
        assertThat(result.get().getEmail()).isEqualTo("grimes@example.com");
        assertThat(result.get().getPassword()).isEqualTo("Genesis");
    }

    @Test
    public void testFindByUsernameOrEmail_WithEmailMatch() {
        Optional<User> result = userRepository.findByUsernameOrEmail("wrong", "grimes@example.com");

        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo("C");
        assertThat(result.get().getEmail()).isEqualTo("grimes@example.com");
        assertThat(result.get().getPassword()).isEqualTo("Genesis");
    }

    @Test
    public void testFindByUsernameOrEmail_NotFound(){
        Optional<User> result = userRepository.findByUsernameOrEmail("magasin", "eheads@example.com");

        assertThat(result).isEmpty();
    }
    @Test
    public void testExistsByEmail_Exists() {
        boolean result = userRepository.existsByEmail("grimes@example.com");

        assertThat(result).isTrue();
    }

    @Test
    public void testExistsByEmail_NotExists() {
        boolean result = userRepository.existsByEmail("wrong@example.com");

        assertThat(result).isFalse();
    }

    @Test
    public void testExistsByUsername_Exists() {
        boolean result = userRepository.existsByUsername("C");

        assertThat(result).isTrue();
    }

    @Test
    public void testExistsByUsername_NotExists() {
        boolean result = userRepository.existsByUsername("wrong");

        assertThat(result).isFalse();
    }
}

