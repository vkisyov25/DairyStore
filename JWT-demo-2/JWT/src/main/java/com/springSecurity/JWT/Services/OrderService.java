package com.springSecurity.JWT.Services;

import com.springSecurity.JWT.Models.*;
import com.springSecurity.JWT.Models.dtos.BuyerOrderDto;
import com.springSecurity.JWT.Models.dtos.OrderProductDto;
import com.springSecurity.JWT.Models.enums.PaymentMethod;
import com.springSecurity.JWT.Repository.OrderRepository;
import com.springSecurity.JWT.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartItemService cartItemService;
    private final OrderItemService orderItemService;
    private final DeliveryCompanyService deliveryCompanyService;
    private final UserService userService;
    private final CartService cartService;
    private final SoldProductService soldProductService;
    private final ProductRepository productRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, CartItemService cartItemService, OrderItemService orderItemService, DeliveryCompanyService deliveryCompanyService, UserService userService, CartService cartService, SoldProductService soldProductService, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.orderItemService = orderItemService;
        this.cartItemService = cartItemService;
        this.deliveryCompanyService = deliveryCompanyService;
        this.userService = userService;
        this.cartService = cartService;
        this.soldProductService = soldProductService;
        this.productRepository = productRepository;
    }

    public void makeOrder(String deliveryAddress, String deliveryCompanyName, PaymentMethod paymentMethod) {
        User user = userService.getUserByUsername();
        Cart cart = cartService.getCartByUser(user);
        //List<CartItem> cartItemList = cartService.getCartItems(userService.getUserByUsername()); // cartItem на текуция потребител
        List<CartItem> cartItemList = cartItemService.getCartItemsByCart(cart); // cartItem на текуция потребител

        DeliveryCompany deliveryCompany = deliveryCompanyService.getDeliveryCompanyByName(deliveryCompanyName);
        double deliveryFee = deliveryCompany.getDeliveryFee();
        double finalPrice = 0;

        Order order = Order.builder()
                .paymentMethod(paymentMethod)
                .deliveryAddress(deliveryAddress)
                .deliveryCompany(deliveryCompany)
                .date(LocalDateTime.now())
                .userId(user.getId())
                .build();

        OrderItem orderItem = null;
        for (CartItem cartItem : cartItemList) {
            orderItem = OrderItem.builder().
                    quantity(cartItem.getQuantity())
                    .cartId(cartItem.getCart().getId())
                    .productId(cartItem.getProduct().getId())
                    .totalPrice(cartItem.getTotalPrice())
                    .order(order)
                    .build();
            finalPrice += cartItem.getTotalPrice();
            orderItemService.saveOrderItem(orderItem);
            soldProductService.saveSoldProduct(cartItem.getCart(), cartItem.getQuantity(), cartItem.getProduct().getId(), cartItem.getCart().getUser(), cartItem.getProduct(), cartItem.getTotalPrice());
        }
        finalPrice += deliveryFee;
        order.setTotalPrice(finalPrice);
        orderRepository.save(order);

        /*//Създава и добавя в базата данни SoldProduct
        SoldProduct soldProduct = SoldProduct.builder()
                .quantity(quantity)
                .product_id(productId)
                .cart_id(cart.getId())
                .finalPrice(totalPrice)
                .buyer_id(user.getId())
                .seller_id(product.getUser().getId())
                .build();
        soldProductRepository.save(soldProduct);*/
        //TODO: Is this a good practise
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
            double priceWithDeliveryFee = order.getTotalPrice();

            List<OrderItem> orderItemList = orderItemService.getOrderItemsByOrderId(order.getId());
            List<OrderProductDto> orderProductDtoList = new ArrayList<>();
            for (OrderItem orderItem : orderItemList) {
                Product product = productRepository.findProductById(orderItem.getProductId());
                String productName = product.getName();
                String productType = product.getType();
                int quantity = orderItem.getQuantity();
                OrderProductDto orderProductDto = new OrderProductDto(productName, productType, quantity);
                orderProductDtoList.add(orderProductDto);
            }
            BuyerOrderDto buyerOrderDto = new BuyerOrderDto(orderDate, addressToDelivery, paymentMethod, deliveryCompany, deliveryFee, priceWithDeliveryFee, orderProductDtoList); //productName, productType, quantity
            buyerOrderDtoList.add(buyerOrderDto);
        }

        return buyerOrderDtoList;
    }
}
