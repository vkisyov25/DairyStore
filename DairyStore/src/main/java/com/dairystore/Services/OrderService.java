package com.dairystore.Services;

import com.dairystore.Models.DeliveryCompany;
import com.dairystore.Models.Order;
import com.dairystore.Models.dtos.BuyerOrderDto;
import com.dairystore.Models.enums.PaymentMethod;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    void checkAvailable() throws Exception;

    void makeOrder(String deliveryAddress, String deliveryCompanyId, PaymentMethod paymentMethod, String paymentIntentId) throws Exception;

    List<BuyerOrderDto> getOrders();

    List<DeliveryCompany> allDeliveryCompanies();

    List<Order> getOrdersByUserId(Long userId);
    Order getOrderById(Long id);
}
