package com.dairystore.Services;

import com.dairystore.Models.Cart;
import com.dairystore.Models.DeliveryCompany;
import com.dairystore.Models.Order;
import com.dairystore.Models.OrderItem;
import com.dairystore.Models.Product;
import com.dairystore.Models.User;
import com.dairystore.Models.dtos.BuyerOrderDto;
import com.dairystore.Models.dtos.ShoppingCartDto;
import com.dairystore.Models.enums.PaymentMethod;
import com.dairystore.Repository.OrderRepository;
import com.dairystore.Repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private CartServiceImpl cartService;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderItemServiceImpl orderItemService;
    @Mock
    private UserServiceImpl userService;
    @Mock
    private DeliveryCompanyServiceImpl deliveryCompanyService;
    @Mock
    private CartItemServiceImpl cartItemService;
    @Mock
    private StripeService stripeService;
    @InjectMocks
    private OrderServiceImpl orderService;

    private User currentUser;
    private Product product;
    private List<ShoppingCartDto> shoppingCartDtoList;
    private ShoppingCartDto shoppingCartDto;
    private Order order;
    private List<Order> orderList;
    private DeliveryCompany deliveryCompany;
    private Cart cart;
    private PaymentMethod paymentMethod;

    @BeforeEach
    void setUp() {

        currentUser = new User();
        currentUser.setId(1L);
        currentUser.setUsername("ventsy");
        currentUser.setCompanyName("Firma");
        currentUser.setAccountId("1324413213213");
        product = new Product();
        product.setId(10L);
        product.setQuantity(50);
        product.setPrice(22.55);
        product.setUser(currentUser);

        shoppingCartDtoList = new ArrayList<>();
        shoppingCartDto = new ShoppingCartDto();
        shoppingCartDto.setId(2L);
        shoppingCartDto.setQuantity(7);
        shoppingCartDtoList.add(shoppingCartDto);


        deliveryCompany = new DeliveryCompany();
        deliveryCompany.setDeliveryFee(5.0);

        order = new Order();
        order.setId(5L);
        //order.setDeliveryCompany(deliveryCompany);

        orderList = new ArrayList<>();
        orderList.add(order);

        cart = new Cart();
        cart.setId(2L);
        cart.setUser(currentUser);

        paymentMethod = PaymentMethod.CARD;

        deliveryCompany = new DeliveryCompany();
        deliveryCompany.setId(2L);


    }

    private void getUser(){
        currentUser = new User();
        currentUser.setId(1L);
        currentUser.setUsername("ventsy");
        currentUser.setCompanyName("Firma");
        currentUser.setAccountId("1324413213213");
    }

    @Test
    public void checkAvailable_whenProductQuantityIsMoreThanInputQuantity_shouldNotThrowException() throws Exception {
        //Arrange
        when(cartService.viewShoppingCart()).thenReturn(shoppingCartDtoList);
        when(productRepository.findProductById(shoppingCartDto.getId())).thenReturn(product);

        //Act
        orderService.checkAvailable();
        //Assert
        verify(cartService, times(1)).viewShoppingCart();
        verify(productRepository, times(1)).findProductById(shoppingCartDto.getId());
        assertDoesNotThrow(() -> orderService.checkAvailable());
    }

    @Test
    public void checkAvailable_whenProductQuantityIsNotMoreThanInputQuantity_shouldThrowException() throws Exception {
        //Arrange
        shoppingCartDto.setQuantity(70);
        when(cartService.viewShoppingCart()).thenReturn(shoppingCartDtoList);
        when(productRepository.findProductById(shoppingCartDtoList.get(0).getId())).thenReturn(product);
        //Act & Arrange
        assertThrows(Exception.class, () -> orderService.checkAvailable());
        verify(cartService, times(1)).viewShoppingCart();
        verify(productRepository, times(1)).findProductById(shoppingCartDtoList.get(0).getId());

    }

    @Test
    public void getOrdersByBuyerId_whenTheParametersIsValid_shouldReturnBuyerOrderDtoList() {
        //Arrange
        order.setDeliveryCompany(deliveryCompany);
        when(orderRepository.findOrdersByUserId(currentUser.getId())).thenReturn(orderList);
        when(orderItemService.getOrderItemsByOrderId(order.getId())).thenReturn(new ArrayList<OrderItem>());

        //Act
        List<BuyerOrderDto> buyerOrderDtoList = orderService.getOrdersByBuyerId(currentUser.getId());
        //Assert
        verify(orderRepository, times(1)).findOrdersByUserId(currentUser.getId());
        verify(orderItemService, times(1)).getOrderItemsByOrderId(order.getId());
    }

    @Test
    public void makeOrder_whenAllIsValid_shouldMakeOrder() throws Exception {
        //Arrange
        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(cartService.getCartByUser(currentUser)).thenReturn(cart);
        when(cartService.viewShoppingCart()).thenReturn(shoppingCartDtoList);
        when(productRepository.updateProductQuantity(shoppingCartDtoList.get(0).getId(), shoppingCartDtoList.get(0).getQuantity())).thenReturn(1);
        when(deliveryCompanyService.getDeliveryCompanyById(deliveryCompany.getId())).thenReturn(deliveryCompany);
        when(productRepository.findProductById(shoppingCartDtoList.get(0).getId())).thenReturn(product);
        when(productRepository.findProductByName(product.getName())).thenReturn(product);
        //Act
        orderService.makeOrder("Kochan", deliveryCompany.getId().toString(), paymentMethod, "12314324425");
        //Assert
        verify(orderItemService).saveOrderItem(any(OrderItem.class));
        verify(productRepository).updateProductQuantity(anyLong(), anyInt());
        verify(orderRepository).save(any(Order.class));
        verify(cartItemService).deleteCartItemByCartId(cart.getId());

        verify(userService).getCurrentUser();
        verify(cartService).getCartByUser(currentUser);
        verify(cartService).viewShoppingCart();
        verify(deliveryCompanyService).getDeliveryCompanyById(deliveryCompany.getId());
        verify(productRepository).findProductById(shoppingCartDtoList.get(0).getId());
        verify(productRepository).findProductByName(product.getName());


    }
}
