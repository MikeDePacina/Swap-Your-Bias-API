package com.swapyourbias.repository;

import com.swapyourbias.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String roleUser);
}
