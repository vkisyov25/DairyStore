package com.springSecurity.JWT.Security;

import com.springSecurity.JWT.Models.User;
import com.springSecurity.JWT.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private UserRepository userRepository;
    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if(user == null){
            throw  new UsernameNotFoundException("User not found");
        }

        return new CustomUserDetails(user);
    }

    /*public String create(String username, String password) {
        // Encodes the password and creates a new User object
        User user = User.builder()
                .username(username)
                .password(new BCryptPasswordEncoder().encode(password)) // Encrypts the password
                .authorities("student") // Assigns default authority
                .build();

        userRepository.save(user);

        return "Create Successfully!";
    }*/

    public String create(User user) {

        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));

        userRepository.save(user);

        return "Create Successfully!";
    }
}
