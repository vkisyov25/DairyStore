package com.dairystore.Services;

import com.dairystore.Models.CartItem;
import com.dairystore.Models.Product;
import com.dairystore.Models.User;
import com.dairystore.Models.dtos.CreateProductDto;
import com.dairystore.Models.dtos.ViewProductDto;
import com.dairystore.Repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;
    @Mock
    private UserServiceImpl userService;
    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    public void createProduct_shouldSaveProductWithCorrectValues() {
        //Arrange
        User currentUser = new User();
        currentUser.setId(1L);

        //Product product = new Product();

        CreateProductDto createProductDto = new CreateProductDto();
        createProductDto.setName("Краве кашкавал - Дъбраж");
        createProductDto.setType("Кашкавал");
        createProductDto.setWeight(0.565);
        createProductDto.setPrice(20.50);
        createProductDto.setDescription("100% краве мляко");
        createProductDto.setDiscount(1);
        createProductDto.setQuantity(100);

        when(userService.getCurrentUser()).thenReturn(currentUser);

        //Act
        productService.createProduct(createProductDto);

        //Assert
        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository, times(1)).save(captor.capture());

        Product savedProduct = captor.getValue();

        assertEquals("Краве кашкавал - Дъбраж", savedProduct.getName());
        assertEquals("Кашкавал", savedProduct.getType());
        assertEquals(0.565, savedProduct.getWeight());
        assertEquals(20.50, savedProduct.getPrice());
        assertEquals("100% краве мляко", savedProduct.getDescription());
        assertEquals(1, savedProduct.getDiscount());
        assertEquals(100, savedProduct.getQuantity());
        assertSame(currentUser, savedProduct.getUser());
    }


    @Test
    public void getCurrentUserProduct_shouldReturnProductsForCurrentUser() {
        //Arrange
        User currentUser = new User();
        currentUser.setId(1L);
        currentUser.setName("Ventsislav");
        List<ViewProductDto> viewProductDtoList = new ArrayList<>();
        ViewProductDto viewProductDto = new ViewProductDto(1L,"Краве кашкавал - Дъбраж","кашкавал", 0.560,22.67,"100% краве мляко",2,100);
        viewProductDtoList.add(viewProductDto);
        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(productRepository.findBySellerUsername(currentUser.getUsername())).thenReturn(viewProductDtoList);
        //Act
        List<ViewProductDto> result = productService.getCurrentUserProducts();
        //Assert
        assertEquals(viewProductDtoList, result);
    }

    @Test
    public void deleteById_productExists_shouldDeleteProduct(){
        //Arrange
        Long productId = 2L;
        when(productRepository.existsById(productId)).thenReturn(true);
        //Act
        productService.deleteById(productId);
        //Assert
        verify(productRepository, times(1)).deleteById(productId);
    }

    @Test
    public void deleteById_productNotExists_shouldThrowException(){
        //Arrange
        Long productId = 2L;
        when(productRepository.existsById(productId)).thenReturn(false);
        //Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productService.deleteById(productId);
        });

        assertEquals("Продуктът с ID " + productId + " не съществува!", exception.getMessage());
        verify(productRepository, never()).deleteById(any());
    }

    @Test
    public void getProductsByType_shouldReturnAllWhenTypeIsAll(){
        //Arrange
        String productType = "всички";
        //when(productType.equals("всички")).thenReturn(true);
        List<Product> productList = new ArrayList<>();
        User user = new User();
        user.setId(1l);
        user.setUsername("ventsy");
        List<CartItem> cartItemList = new ArrayList<>();
        Product product1 = new Product(1l,"Краве кашкавал - Дъбраж", "кашкавал",0.450,21.55,"100% краве мляко",1,100,user,cartItemList);
        Product product2 = new Product(1l,"Краве масло - Дъбраж", "масло",0.450,21.55,"100% краве мляко",1,100,user,cartItemList);
        Product product3 = new Product(1l,"Краве сирене - Дъбраж", "сирене",0.450,21.55,"100% краве мляко",1,100,user,cartItemList);
        Product product4 = new Product(1l,"Краве кашкавал - Лещен", "кашкавал",0.450,21.55,"100% краве мляко",1,100,user,cartItemList);
        productList.add(product1);
        productList.add(product2);
        productList.add(product3);
        productList.add(product4);
        when(productRepository.findAll()).thenReturn(productList);
        //Act
        List<Product> result = productService.getProductsByType(productType);
        //Assert
        assertEquals(productList,result);
        verify(productRepository, times(1)).findAll();
        verify(productRepository, never()).findProductsByType(any());
    }

    @Test
    public void getProductsByType_shouldAllOfSameType(){
        //Arrange
        String productType = "сирене";

        List<Product> productList = new ArrayList<>();
        User user = new User();
        user.setId(1l);
        user.setUsername("ventsy");
        List<CartItem> cartItemList = new ArrayList<>();
        Product product3 = new Product(1l,"Краве сирене - Дъбраж", "сирене",0.450,21.55,"100% краве мляко",1,100,user,cartItemList);
        productList.add(product3);
        when(productRepository.findProductsByType(productType)).thenReturn(productList);
        //Act
        List<Product> result = productService.getProductsByType(productType);
        //Assert
        assertEquals(productList,result);
        verify(productRepository, times(1)).findProductsByType(productType);
        verify(productRepository, never()).findAll();
    }
}