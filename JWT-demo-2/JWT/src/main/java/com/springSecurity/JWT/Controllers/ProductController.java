package com.springSecurity.JWT.Controllers;

import com.springSecurity.JWT.Models.Product;
import com.springSecurity.JWT.Models.User;
import com.springSecurity.JWT.Repository.UserRepository;
import com.springSecurity.JWT.Security.CustomUserDetailsService;
import com.springSecurity.JWT.Services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;
    private final CustomUserDetailsService customUserDetailsService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    public ProductController(ProductService productService, CustomUserDetailsService customUserDetailsService) {
        this.productService = productService;
        this.customUserDetailsService = customUserDetailsService;
    }

    //@PreAuthorize("hasAuthority('seller')")
    @GetMapping("/all")
    public String getAllProductsByUsername(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            String role = authentication.getAuthorities().iterator().next().getAuthority();
            List<Product> products = productService.getAllProductByUsername(username);
            model.addAttribute("productList", products);
            return "listOfProduct";
        }
        //TODO: Не може да я достъпим ако не сме логнати
        return "login"; // Ако няма логнат потребител
    }

    @GetMapping("/create")
    public String showCreateProductPage(Model model) {
        model.addAttribute("product", new Product());
        return "createProductPage";
    }

    @PostMapping("/create")
    public String createProduct(@ModelAttribute Product product) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName());
        product.setUser(user);
        productService.creteProduct(product);
        return "sellerPage";
    }

}
