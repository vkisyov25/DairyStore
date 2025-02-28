package com.springSecurity.JWT.Controllers;

import com.springSecurity.JWT.Models.Order;
import com.springSecurity.JWT.Models.dtos.BuyerOrderDto;
import com.springSecurity.JWT.Models.enums.PaymentMethod;
import com.springSecurity.JWT.Services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

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
