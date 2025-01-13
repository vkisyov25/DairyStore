package com.springSecurity.JWT.Repository;

import com.springSecurity.JWT.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u from User u where u.username = ?1")
    User findByUsername (String username);

    boolean existsByEmail(String email);
    boolean existsByCompanyName(String companyName);
    boolean existsByUsername(String username);
    boolean existsByCompanyEIK(String companyEIK);
}
