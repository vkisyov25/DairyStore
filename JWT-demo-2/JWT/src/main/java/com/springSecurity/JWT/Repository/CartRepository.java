package com.springSecurity.JWT.Repository;

import com.springSecurity.JWT.Models.Cart;
import com.springSecurity.JWT.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUser(User user);
}
