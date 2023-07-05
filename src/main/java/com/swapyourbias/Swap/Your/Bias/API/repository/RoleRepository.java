package com.swapyourbias.Swap.Your.Bias.API.repository;

import com.swapyourbias.Swap.Your.Bias.API.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String roleUser);
}
