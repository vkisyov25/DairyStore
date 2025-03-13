package com.dairystore.Controllers;

import com.dairystore.Models.Order;
import com.dairystore.Models.dtos.BuyerOrderDto;
import com.dairystore.Models.enums.PaymentMethod;
import com.dairystore.Services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/make")
    public String displayMakeOrderPage() {
        return "makeOrderPage";
    }

    @PostMapping("/make")
    public String makeOrder(@RequestParam String deliveryAddress, @RequestParam String deliveryCompanyName, @RequestParam PaymentMethod paymentMethod) {
        orderService.makeOrder(deliveryAddress, deliveryCompanyName, paymentMethod);
        return "redirect:/order/latest-order";
    }

    @GetMapping("/latest-order")
    public String viewOrder(Model model) {
        Order order = orderService.getLatestOrder();
        model.addAttribute("order", order);
        return "currentOrder";
    }


    @GetMapping("/viewAll")
    public ResponseEntity<List<BuyerOrderDto>> displayAllOrders() {
        List<BuyerOrderDto> buyerOrderDtoList = orderService.getCurrentUserOrders();
        return ResponseEntity.ok(buyerOrderDtoList);

    }
}
