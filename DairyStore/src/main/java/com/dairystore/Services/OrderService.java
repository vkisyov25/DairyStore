package com.dairystore.Services;

import com.dairystore.Models.Cart;
import com.dairystore.Models.DeliveryCompany;
import com.dairystore.Models.Order;
import com.dairystore.Models.OrderItem;
import com.dairystore.Models.Product;
import com.dairystore.Models.User;
import com.dairystore.Models.dtos.BuyerOrderDto;
import com.dairystore.Models.dtos.OrderProductDto;
import com.dairystore.Models.dtos.ShoppingCartDto;
import com.dairystore.Models.enums.PaymentMethod;
import com.dairystore.Repository.OrderRepository;
import com.dairystore.Repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartItemService cartItemService;
    private final OrderItemService orderItemService;
    private final DeliveryCompanyService deliveryCompanyService;
    private final UserService userService;
    private final CartService cartService;
    private final SoldProductService soldProductService;
    private final ProductRepository productRepository;

    public void checkAvailable() throws Exception {
        List<ShoppingCartDto> shoppingCartDtoList = cartService.viewShoppingCart();
        for (int i = 0; i < shoppingCartDtoList.size(); i++) {
            Product product = productRepository.findProductById(shoppingCartDtoList.get(i).getId());
            int quantity = product.getQuantity();
            int receivedQuantity = shoppingCartDtoList.get(i).getQuantity();
            if (receivedQuantity > quantity) {
                throw new Exception("Няма достатъчно количество от този продукт: име: " + product.getName() + " тип: " + product.getType() + " тегло: " + product.getWeight()
                        + " цена: " + product.getPrice() + " количество: " + product.getQuantity() + " отстъпка: " + product.getDiscount());
            }

        }
    }

    public void makeOrder(String deliveryAddress, String deliveryCompanyName, PaymentMethod paymentMethod, String paymentIntentId) throws Exception {
        User user = userService.getUserByUsername();
        Cart cart = cartService.getCartByUser(user);

        List<ShoppingCartDto> shoppingCartDtoList = cartService.viewShoppingCart();
        updateProductQuantities(shoppingCartDtoList);

        DeliveryCompany deliveryCompany = deliveryCompanyService.getDeliveryCompanyByName(deliveryCompanyName);

        Order order = Order.builder()
                .paymentMethod(paymentMethod)
                .deliveryAddress(deliveryAddress)
                .deliveryCompany(deliveryCompany)
                .date(LocalDateTime.now())
                .userId(user.getId())
                .paymentIntentId(paymentIntentId)
                .deliveryCompany(deliveryCompany)
                .build();

        OrderItem orderItem = null;
        for (int i = 0; i < shoppingCartDtoList.size(); i++) {
            Product product = productRepository.findProductById(shoppingCartDtoList.get(i).getId());
            orderItem = OrderItem.builder()
                    .quantity(shoppingCartDtoList.get(i).getQuantity())
                    .totalPrice(shoppingCartDtoList.get(i).getTotalPricePerProduct())
                    .name(shoppingCartDtoList.get(i).getName())
                    .type(shoppingCartDtoList.get(i).getType())
                    .weight(shoppingCartDtoList.get(i).getWeight())
                    .price(shoppingCartDtoList.get(i).getPrice())
                    .discount(shoppingCartDtoList.get(i).getDiscount())
                    .cartId(cart.getId())
                    .order(order)
                    .seller_id(product.getUser().getId())
                    .description(product.getDescription())
                    .build();
            orderItemService.saveOrderItem(orderItem);
        }

        orderRepository.save(order);
        cartItemService.deleteCartItemByCartId(cart.getId());

    }

    public Order getLatestOrder() {
        User user = userService.getUserByUsername();
        return orderRepository.findTopByUserIdOrderByDateDesc(user.getId())
                .orElseThrow(() -> new RuntimeException("Не е намерена последна поръчка за потребителя с ID: " + user.getId()));
    }

    public List<BuyerOrderDto> getCurrentUserOrders() {
        User user = userService.getUserByUsername();
        List<Order> orderList = orderRepository.findOrdersByUserId(user.getId());
        List<BuyerOrderDto> buyerOrderDtoList = new ArrayList<>();

        for (int i = 0; i < orderList.size(); i++) {
            Order order = orderList.get(i);
            LocalDateTime orderDate = order.getDate();
            String addressToDelivery = order.getDeliveryAddress();
            PaymentMethod paymentMethod = order.getPaymentMethod();
            String deliveryCompany = order.getDeliveryCompany().getName();
            double deliveryFee = order.getDeliveryCompany().getDeliveryFee();
            /* double priceWithDeliveryFee = order.getTotalPrice();*/

            List<OrderItem> orderItemList = orderItemService.getOrderItemsByOrderId(order.getId());
            List<OrderProductDto> orderProductDtoList = new ArrayList<>();
            for (OrderItem orderItem : orderItemList) {
                /*Product product = productRepository.findProductById(orderItem.getProductId());*/
               /* String productName = product.getName();
                String productType = product.getType();*/
                int quantity = orderItem.getQuantity();
                /*OrderProductDto orderProductDto = new OrderProductDto(productName, productType, quantity);
                orderProductDtoList.add(orderProductDto);*/
            }
            /* BuyerOrderDto buyerOrderDto = new BuyerOrderDto(orderDate, addressToDelivery, paymentMethod, deliveryCompany, deliveryFee, priceWithDeliveryFee, orderProductDtoList);*/ //productName, productType, quantity
            /*buyerOrderDtoList.add(buyerOrderDto);*/
        }

        return buyerOrderDtoList;
    }

    public List<DeliveryCompany> allDeliveryCompanies() {
        return deliveryCompanyService.getDeliveryCompanies();
    }

    @Transactional
    private void updateProductQuantities(List<ShoppingCartDto> shoppingCartDtoList) throws Exception {
        for (ShoppingCartDto item : shoppingCartDtoList) {
            int updatedRows = productRepository.updateProductQuantity(item.getId(), item.getQuantity());

            if (updatedRows == 0) {
                throw new Exception("Недостатъчно количество от продукта с име: " + item.getName());
            }
        }
    }
  
    public List<DeliveryCompany> allDeliveryCompanies(){
        return deliveryCompanyService.getDeliveryCompanies();
    }

}
