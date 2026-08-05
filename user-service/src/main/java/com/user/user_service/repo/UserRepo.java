package com.user.user_service.repo;

import com.user.user_service.entity.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepo extends JpaRepository<UserDetails,Integer> {

    boolean existsByNameIgnoreCaseAndEmailIgnoreCase(String name,String email);
    boolean existsByUsernameIgnoreCase(String username);
}
