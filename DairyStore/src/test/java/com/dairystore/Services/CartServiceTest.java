package com.dairystore.Services;

import com.dairystore.Models.Cart;
import com.dairystore.Models.Product;
import com.dairystore.Models.User;
import com.dairystore.Repository.CartRepository;
import com.dairystore.Repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {
    @Mock
    ProductServiceImpl productService;
    @Mock
    CartItemServiceImpl cartItemService;
    @Mock
    ProductRepository productRepository;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private UserServiceImpl userService;
    @InjectMocks
    private CartServiceImpl cartService;

    private User currentUser;
    private Product product;
    private Cart cart;

    @BeforeEach
    void setUp() {
        currentUser = new User();
        currentUser.setId(1L);
        currentUser.setUsername("ventsy");
        currentUser.setCompanyName("Firma");

        product = new Product();
        product.setId(10L);
        product.setQuantity(50);
        product.setPrice(22.55);

        cart = new Cart();
    }

    @Test
    public void findOrCreateCart_whenCartExist_shouldReturnExistingCart() {
        //Arrange
        User currentUser = new User();
        currentUser.setId(2L);
        currentUser.setUsername("Ventsy");
        Cart expectedCart = new Cart();
        when(cartRepository.findByUser(currentUser)).thenReturn(expectedCart);
        //Act
        Cart resultCart = cartService.findOrCreateCart(currentUser);
        //Assert
        assertNotNull(resultCart);
        verify(cartRepository, never()).save(any());
    }

    @Test
    public void findOrCreateCart_whenCartNotExist_shouldCreateNewCart_() {
        //Arrange
        when(cartRepository.findByUser(currentUser)).thenReturn(null);
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));

        //Act
        Cart result = cartService.findOrCreateCart(currentUser);

        //Assert
        assertNotNull(result);
        assertSame(currentUser, result.getUser());
        verify(cartRepository, times(1)).findByUser(currentUser);
        verify(cartRepository, times(1)).save(result);
    }

    @Test
    public void addToCart_whenQuantityIsValid_shouldSaveCartItem() throws Exception {
        //Arrange;
        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(productService.getProductById(product.getId())).thenReturn(product);
        when(cartRepository.findByUser(currentUser)).thenReturn(cart);
        //Act
        cartService.addToCart(product.getId(), 5);
        //Assert
        verify(cartItemService, times(1)).saveCartItems(cart, product, 5);
        verify(userService, times(1)).getCurrentUser();
        verify(productService, times(1)).getProductById(product.getId());

    }


    @Test
    void addToCart_whenQuantityIsNotValid_shouldThrowException() throws Exception {
        //Arrange
        when(userService.getCurrentUser()).thenReturn(currentUser);
        product.setQuantity(4);
        when(productService.getProductById(product.getId())).thenReturn(product);

        // Act + Assert
        assertThrows(Exception.class, () -> cartService.addToCart(product.getId(), 5));

        verify(cartItemService, never()).saveCartItems(any(), any(), anyInt());
    }

    @Test
    public void calculateTotalPricePerProduct_whenIsCompanyAndGetDiscount_shouldReturnTotalPricePerProduct() {
        //Arrange;
        int inputQuantity = 20;
        product.setPrice(10.0);
        product.setDiscount(10.0);
        // Act
        double result = cartService.calculateTotalPricePerProduct(currentUser, product, inputQuantity);

        // Assert
        // basePrice = 10 * 20 = 200
        // discount = 200 * 0.10 = 20
        // total = 200 - 20 = 180
        assertEquals(180.0, result);
    }

    @Test
    public void calculateTotalPricePerProduct_whenIsNotCompanyAndNotGetDiscount_shouldReturnTotalPricePerProduct() {
        //Arrange
        currentUser.setCompanyName("");
        int inputQuantity = 20;
        product.setPrice(10);
        // Act
        double result = cartService.calculateTotalPricePerProduct(currentUser, product, inputQuantity);
        // Assert
        assertEquals(200.0, result);
    }

    @Test
    public void getCartByUser_whenCartIsExist_shouldReturnCart() {
        //Arrange
        when(cartRepository.findByUser(currentUser)).thenReturn(cart);
        //Act
        Cart result = cartService.getCartByUser(currentUser);
        //Assert
        assertEquals(cart, result);
        verify(cartRepository, times(1)).findByUser(currentUser);

    }

    @Test
    public void getCartByUser_whenCartIsNotExist_shouldNotReturnCart() {
        //Arrange
        when(cartRepository.findByUser(currentUser)).thenReturn(null);
        //Act
        Cart result = cartService.getCartByUser(currentUser);
        //Assert
        assertEquals(null, result);
        verify(cartRepository, times(1)).findByUser(currentUser);

    }
}
