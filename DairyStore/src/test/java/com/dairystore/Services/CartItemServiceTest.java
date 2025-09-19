package com.dairystore.Services;

import com.dairystore.Models.Cart;
import com.dairystore.Models.CartItem;
import com.dairystore.Models.Product;
import com.dairystore.Models.User;
import com.dairystore.Repository.CartItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CartItemServiceTest {
    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private UserServiceImpl userService;
    @InjectMocks
    private CartItemServiceImpl cartItemService;

    private Product product;
    private Cart cart;
    private User currentUser;
    private List<CartItem> expectedCartItemList;
    private CartItem cartItem;

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

        List<CartItem> expectedCartItemList = new ArrayList<>();
        CartItem item1 = new CartItem();
        CartItem item2 = new CartItem();
        expectedCartItemList.add(item1);
        expectedCartItemList.add(item2);

        cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProduct(product);
    }

    @Test
    public void saveCartItems_whenCartItemNotExistInDatabase_shouldSaveCartItems() {
        //Arrange
        when(cartItemRepository.existsByProductIdAndCartId(product.getId(), cart.getId())).thenReturn(false);
        //Act
        cartItemService.saveCartItems(cart, product, 5);
        //Assert
        verify(cartItemRepository, times(1)).save(any());
        verify(cartItemRepository, never()).updateProductQuantity(anyLong(), anyInt(), anyLong());
    }

    @Test
    public void saveCartItems_whenCartItemExistInDatabase_shouldUpdateItsQuantity() {
        //Arrange
        int inputQuantity = 5;
        when(cartItemRepository.existsByProductIdAndCartId(product.getId(), cart.getId())).thenReturn(true);
        //Act
        cartItemService.saveCartItems(cart, product, inputQuantity);
        //Assert
        verify(cartItemRepository, times(1)).updateProductQuantity(product.getId(), inputQuantity, cart.getId());
        verify(cartItemRepository, never()).save(any());


    }

    @Test
    public void getCartItemByCart_whenCartHasItems_shouldReturnThem() {
        //Arrange
        when(cartItemRepository.findByCart(cart)).thenReturn(expectedCartItemList);
        //Act
        List<CartItem> result = cartItemService.getCartItemsByCart(cart);
        //Assert
        assertEquals(expectedCartItemList, result);
    }

    @Test
    public void getCartItemByCart_shouldReturnEmptyList_whenCartHasNoItems() {
        //Arrange
        when(cartItemRepository.findByCart(cart)).thenReturn(new ArrayList<>());
        //Act
        List<CartItem> result = cartItemService.getCartItemsByCart(cart);
        //Assert
        assertEquals(new ArrayList<>(), result);
        verify(cartItemRepository, times(1)).findByCart(cart);
    }

    @Test
    public void creteCartItem_whenAllFieldAreValid_shouldSaveCartItem() {
        //Arrange
        int quantity = 15;
        //Act
        cartItemService.createCartItem(cart, product, quantity);
        //Assert
        ArgumentCaptor<CartItem> captor = ArgumentCaptor.forClass(CartItem.class);
        verify(cartItemRepository, times(1)).save(captor.capture());
        CartItem cartItem = captor.getValue();
        assertEquals(cart, cartItem.getCart());
        assertEquals(product, cartItem.getProduct());
        assertEquals(quantity, quantity);
    }

    @Test
    public void deleteCartItemByProductId_whenItIsExist_shouldDeleteCartItem() throws Exception {
        //Arrange
        currentUser.setCart(cart);
        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(cartItemRepository.existsByProductIdAndCartId(product.getId(), cart.getId())).thenReturn(true);
        //Act
        cartItemService.deleteCartItemByProductId(product.getId());
        //Assert
        verify(cartItemRepository, times(1)).deleteByProductId(product.getId(), cart.getId());

    }

    //TODO: this test is not working
    @Test
    public void deleteCartItemByProductId_whenItIsNotExist_shouldThrowException() throws Exception {
        //Arrange;
        currentUser.setCart(cart);
        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(cartItemRepository.existsByProductIdAndCartId(product.getId(), cart.getId())).thenReturn(false);
        //Act & Assert
        assertThrows(Exception.class, () -> cartItemService.deleteCartItemByProductId(product.getId()));

        verify(cartItemRepository, never()).deleteByProductId(product.getId(), cart.getId());
    }

    @Test
    public void isExist_whenIsNotExist_shouldReturnException() {
        //Arrange
        when(cartItemRepository.existsByProductIdAndCartId(product.getId(), cart.getId())).thenReturn(false);

        //Act & Assert
        assertThrows(Exception.class, () -> cartItemService.isExist(product.getId(), cart.getId()));
    }

    @Test
    public void isExist_whenIsExist_shouldNotReturnException() throws Exception {
        //Arrange
        when(cartItemRepository.existsByProductIdAndCartId(product.getId(), cart.getId())).thenReturn(true);

        //Act & Assert
        assertDoesNotThrow(() -> cartItemService.isExist(product.getId(), cart.getId()));

    }
}
