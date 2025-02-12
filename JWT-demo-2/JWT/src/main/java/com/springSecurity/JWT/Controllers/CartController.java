package com.springSecurity.JWT.Controllers;

import com.springSecurity.JWT.Models.CartItem;
import com.springSecurity.JWT.Services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class CartController {
    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam int quantity, @RequestParam Long productId, RedirectAttributes redirectAttributes) {
        cartService.addToCart(productId, quantity);
        redirectAttributes.addFlashAttribute("success", "Successfully added product in the cart");
        return "redirect:/products/listToBuy";

    }

    @GetMapping("/view")
    public String getYourProductsFromCart(Model model) {
        List<CartItem> cartItemList = cartService.getCartItems();
        model.addAttribute("productList", cartItemList);
        return "cartPage";
    }

    @PostMapping("/deleteById")
    public String deleteProductById(@RequestParam("productId") Long productId, RedirectAttributes redirectAttributes) {
        cartService.deleteByProductId(productId);
        redirectAttributes.addFlashAttribute("success", "Product is successfully deleted");
        return "redirect:/test/buyer";
    }
}
