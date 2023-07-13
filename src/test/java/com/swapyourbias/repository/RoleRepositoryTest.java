package com.swapyourbias.repository;

import com.swapyourbias.repository.RoleRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import com.swapyourbias.model.Role;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class RoleRepositoryTest {
    @Autowired
    private RoleRepository roleRepository;

    private Role role;

    @BeforeEach
    public void setUp(){
        role = new Role();
        role.setName("ROLE_MOD");
        roleRepository.save(role);
    }

    @AfterEach
    public void tearDown(){
        roleRepository.delete(role);
        role = null;
    }

    @Test
    public void testFindByName_Found(){
        Role role = roleRepository.findByName("ROLE_MOD");
        assertThat(role.getName()).isEqualTo("ROLE_MOD");
    }

    @Test
    public void testFindByName_NotFound(){
        Role role = roleRepository.findByName("ROLE_MODERATOR");
        assertThat(role).isNull();
    }
    

}
