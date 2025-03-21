package com.dairystore.Controllers;

import com.dairystore.Models.DeliveryCompany;
import com.dairystore.Models.Order;
import com.dairystore.Models.dtos.BuyerOrderDto;
import com.dairystore.Models.dtos.OrderRequestDto;
import com.dairystore.Services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/delivery-companies")
    public ResponseEntity<List<DeliveryCompany>> deliveryCompanies() {
        return ResponseEntity.ok().body(orderService.allDeliveryCompanies());

    }

    @GetMapping("/check-availability")
    public ResponseEntity<?> checkAvailable() {
        try {
            orderService.checkAvailable();
            return ResponseEntity.ok().body("Всичко е в наличност");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());

        }
    }
        @PostMapping("/make")
        public ResponseEntity<?> makeOrder (@RequestBody OrderRequestDto orderRequestDto){
            try {
                orderService.makeOrder(orderRequestDto.getDeliveryAddress(), orderRequestDto.getDeliveryCompanyName(), orderRequestDto.getPaymentMethod(), orderRequestDto.getPaymentIntentId());
                return ResponseEntity.ok().body("Поръчката е направена успешно!");
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }

        @GetMapping("/latest-order")
        public String viewOrder (Model model){
            Order order = orderService.getLatestOrder();
            model.addAttribute("order", order);
            return "currentOrder";
        }


        @GetMapping("/viewAll")
        public ResponseEntity<List<BuyerOrderDto>> displayAllOrders () {
            List<BuyerOrderDto> buyerOrderDtoList = orderService.getCurrentUserOrders();
            return ResponseEntity.ok(buyerOrderDtoList);

        }
    }
