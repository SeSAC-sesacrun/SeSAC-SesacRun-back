package com.example.sesacrunback.domain.user.repository;

import com.example.sesacrunback.domain.user.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {


    boolean existsUserByEmail(String email);


    Optional<User> findByEmail(String email);
}
