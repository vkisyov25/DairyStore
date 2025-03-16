package com.dairystore.Controllers;

import com.dairystore.Models.Cart;
import com.dairystore.Models.CartItem;
import com.dairystore.Models.User;
import com.dairystore.Services.CartItemService;
import com.dairystore.Services.CartService;
import com.dairystore.Services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    private final CartItemService cartItemService;
    private final UserService userService;

    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@RequestParam Long productId, @RequestParam int quantity) {
        try {
            cartService.addToCart(productId, quantity);
            return ResponseEntity.status(HttpStatus.OK).body("Продуктът беше добавен успешно!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/view")
    public String getYourProductsFromCart(Model model) {
        //This logic is here because the program gets stuck in a loop if it is in CartItemService
        User user = userService.getUserByUsername();
        Cart cart = cartService.getCartByUser(user);
        List<CartItem> cartItemList = cartItemService.getCartItemsByCart(cart);
        model.addAttribute("productList", cartItemList);
        return "cartPage";
    }

    @PostMapping("/deleteById")
    public String deleteProductById(@RequestParam("productId") Long productId, RedirectAttributes redirectAttributes) {
        cartItemService.deleteCartItemsByProductId(productId);
        redirectAttributes.addFlashAttribute("success", "Product is successfully deleted");
        return "redirect:/test/buyer";
    }
}
