package com.dairystore.Repository;

import com.dairystore.Models.User;
import com.dairystore.Models.dtos.UserDto;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u from User u where u.username = ?1")
    User findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByCompanyName(String companyName);

    boolean existsByUsername(String username);

    boolean existsByCompanyEIK(String companyEIK);

    @Query("SELECT new com.dairystore.Models.dtos.UserDto(u.id, u.username, u.authorities, u.email, u.name, u.phone, u.address, u.companyName, u.companyEIK) FROM User u")
    List<UserDto> findAllUsersAsDto();

    void deleteById(@NotNull Long userId);

    boolean existsById(@NotNull Long userId);

}
