package com.dairystore.Services;

import com.dairystore.Models.Cart;
import com.dairystore.Models.DeliveryCompany;
import com.dairystore.Models.Order;
import com.dairystore.Models.OrderItem;
import com.dairystore.Models.Product;
import com.dairystore.Models.User;
import com.dairystore.Models.dtos.BuyerOrderDto;
import com.dairystore.Models.dtos.OrderItemResultDto;
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
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final CartItemServiceImpl cartItemService;
    private final OrderItemServiceImpl orderItemService;
    private final DeliveryCompanyServiceImpl deliveryCompanyService;
    private final UserServiceImpl userService;
    private final CartServiceImpl cartService;
    private final ProductRepository productRepository;

    private static Order createOrder(String deliveryAddress, PaymentMethod paymentMethod, String paymentIntentId, User user, DeliveryCompany deliveryCompany) {
        Order order = Order.builder()
                .paymentMethod(paymentMethod)
                .deliveryAddress(deliveryAddress)
                .deliveryCompany(deliveryCompany)
                .date(LocalDateTime.now())
                .user(user)
                .paymentIntentId(paymentIntentId)
                .deliveryCompany(deliveryCompany)
                .build();
        return order;
    }

    @Override
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

    @Override
    public void makeOrder(String deliveryAddress, String deliveryCompanyId, PaymentMethod paymentMethod, String paymentIntentId) throws Exception {
        User user = userService.getUserByUsername();
        Cart cart = cartService.getCartByUser(user);
        List<ShoppingCartDto> shoppingCartDtoList = cartService.viewShoppingCart();

        updateProductQuantities(shoppingCartDtoList);

        DeliveryCompany deliveryCompany = deliveryCompanyService.getDeliveryCompanyById(Long.parseLong(deliveryCompanyId));

        Order order = createOrder(deliveryAddress, paymentMethod, paymentIntentId, user, deliveryCompany);

        createOrderItem(cart, shoppingCartDtoList, order);

        orderRepository.save(order);
        cartItemService.deleteCartItemByCartId(cart.getId());

    }

    private void createOrderItem(Cart cart, List<ShoppingCartDto> shoppingCartDtoList, Order order) {
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
                    /*.cart(cart)*/
                    .order(order)
                    .sellerId(product.getUser().getId())
                    .description(product.getDescription())
                    .build();
            orderItemService.saveOrderItem(orderItem);
        }
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

    @Override
    public Order getLatestOrder() {
        User user = userService.getUserByUsername();
        return orderRepository.findTopByUserIdOrderByDateDesc(user.getId())
                .orElseThrow(() -> new RuntimeException("Не е намерена последна поръчка за потребителя с ID: " + user.getId()));
    }

    @Override
    public List<BuyerOrderDto> getOrders() {
        User user = userService.getUserByUsername();
        return getOrdersByBuyerId(user.getId());
    }

    private List<BuyerOrderDto> getOrdersByBuyerId(Long buyerId) {
        List<Order> orderList = orderRepository.findOrdersByUserId(buyerId);
        List<BuyerOrderDto> buyerOrderDtoList = new ArrayList<>();
        for (int i = 0; i < orderList.size(); i++) {
            Order order = orderList.get(i);
            LocalDateTime orderDate = order.getDate();
            String addressToDelivery = order.getDeliveryAddress();
            PaymentMethod paymentMethod = order.getPaymentMethod();
            String deliveryCompany = order.getDeliveryCompany().getName();
            double deliveryFee = order.getDeliveryCompany().getDeliveryFee();

            OrderItemResultDto orderItemResultDto = getOrderItemsByOrderId(order.getId());
            buyerOrderDtoList.add(new BuyerOrderDto(orderDate, addressToDelivery, paymentMethod, deliveryCompany, deliveryFee, orderItemResultDto.getTotalItemPrice() + deliveryFee, orderItemResultDto.getOrderProductDtoList()));
        }
        return buyerOrderDtoList;
    }

    private OrderItemResultDto getOrderItemsByOrderId(Long orderId) {
        List<OrderItem> orderItemList = orderItemService.getOrderItemsByOrderId(orderId);
        List<OrderProductDto> orderProductDtoList = new ArrayList<>();
        double priceWithDeliveryFee = 0;
        for (OrderItem orderItem : orderItemList) {
            String productName = orderItem.getName();
            String productType = orderItem.getType();
            int quantity = orderItem.getQuantity();
            OrderProductDto orderProductDto = new OrderProductDto(productName, productType, quantity);
            orderProductDtoList.add(orderProductDto);
            priceWithDeliveryFee += orderItem.getTotalPrice();
        }
        OrderItemResultDto orderItemResultDto = new OrderItemResultDto(orderProductDtoList, priceWithDeliveryFee);
        return orderItemResultDto;
    }

    @Override
    public List<DeliveryCompany> allDeliveryCompanies() {
        return deliveryCompanyService.getDeliveryCompanies();
    }

    @Override
    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findOrdersByUserId(userId);
    }

   /* @Override
    public Order getOrderById(Long orderId){
        return orderRepository.findOrdersById(orderId);
    }*/
}