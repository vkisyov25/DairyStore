package com.dairystore.Services;

import com.dairystore.Models.Cart;
import com.dairystore.Models.CartItem;
import com.dairystore.Repository.CartItemRepository;
import com.stripe.model.tax.Registration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CartItemServiceTest {
    @Mock
    private CartItemRepository cartItemRepository;
    @InjectMocks
    private CartItemServiceImpl cartItemService;

    @Test
    public void getCartItemByCart(){
        //Arrange
        Cart cart = new Cart();
        List<CartItem> expectedCartItemList = new ArrayList<>();
        CartItem item1 = new CartItem();
        CartItem item2 = new CartItem();
        expectedCartItemList.add(item1);
        expectedCartItemList.add(item2);
        when(cartItemRepository.findByCart(cart)).thenReturn(expectedCartItemList);
        //Act
        List<CartItem> result = cartItemService.getCartItemsByCart(cart);
        //Assert
        assertEquals(expectedCartItemList, result);
    }
}
