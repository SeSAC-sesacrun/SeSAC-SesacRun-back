package com.example.sesacrunback.domain.user.repository;

import com.example.sesacrunback.domain.user.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {


    boolean existsUserByEmail(String email);
}
