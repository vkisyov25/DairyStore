package com.springSecurity.JWT.Controllers;

import com.springSecurity.JWT.Models.CartItem;
import com.springSecurity.JWT.Models.User;
import com.springSecurity.JWT.Services.CartService;
import com.springSecurity.JWT.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;
    private final UserService userService;



    @Autowired
    public CartController(CartService cartService, UserService userService) {
        this.cartService = cartService;
        this.userService = userService;
    }

    @PostMapping("/add")
    public String processLogin(@RequestParam int quantity, @RequestParam Long productId, RedirectAttributes redirectAttributes) {
        cartService.addToCart(productId,quantity);
        redirectAttributes.addFlashAttribute("success", "Successfully added product in the cart");
        return "redirect:/products/listToBuy";

    }

    @GetMapping("/view")
    public String getYourProductsFromCart(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByUsername(authentication.getName());
        List<CartItem> cartItemList = cartService.getCartItems(user.getId());
        model.addAttribute("productList",cartItemList);
        return "cartPage";
    }
}
