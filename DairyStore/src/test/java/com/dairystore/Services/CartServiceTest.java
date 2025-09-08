package com.dairystore.Services;

import com.dairystore.Models.Cart;
import com.dairystore.Models.User;
import com.dairystore.Repository.CartRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {
    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    @Test
    public void findOrCreateCart_cartIsExist_shouldReturnTheCart(){
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
        verify(cartRepository,never()).save(any());
    }

    @Test
    public void findOrCreateCart_cartIsNotExist_shouldCreateCart(){
        //Arrange
        User currentUser = new User();
        currentUser.setId(2L);
        currentUser.setUsername("ventsy");

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

}
