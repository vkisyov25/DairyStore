package com.dairystore.Services;

import com.dairystore.Models.Cart;
import com.dairystore.Models.CartItem;
import com.dairystore.Models.Product;
import com.dairystore.Models.User;
import com.dairystore.Models.dtos.CreateProductDto;
import com.dairystore.Models.dtos.ProductForSaleDto;
import com.dairystore.Models.dtos.ViewProductDto;
import com.dairystore.Repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
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

    private Product product;
    private Cart cart;
    private User currentUser;
    private CreateProductDto createProductDto;

    private List<ViewProductDto> viewProductDtoList;
    private List<Product> productList;
    private List<CartItem> cartItemList;
    @BeforeEach
    void setUp(){
        currentUser = new User();
        currentUser.setId(1L);
        currentUser.setUsername("ventsy");
        currentUser.setCompanyName("Firma");

        product = new Product();
        product.setId(10L);
        product.setQuantity(50);
        product.setPrice(22.55);

        createProductDto = new CreateProductDto();
        createProductDto.setName("Краве кашкавал - Дъбраж");
        createProductDto.setType("Кашкавал");
        createProductDto.setWeight(0.565);
        createProductDto.setPrice(20.50);
        createProductDto.setDescription("100% краве мляко");
        createProductDto.setDiscount(1);
        createProductDto.setQuantity(100);

        viewProductDtoList = new ArrayList<>();
        ViewProductDto viewProductDto = new ViewProductDto(1L,"Краве кашкавал - Дъбраж","кашкавал", 0.560,22.67,"100% краве мляко",2,100);
        viewProductDtoList.add(viewProductDto);

        productList = new ArrayList<>();
        cartItemList = new ArrayList<>();
        Product product1 = new Product(1l,"Краве кашкавал - Дъбраж", "кашкавал",0.450,21.55,"100% краве мляко",1,100,currentUser,cartItemList);
        Product product2 = new Product(1l,"Краве масло - Дъбраж", "масло",0.450,21.55,"100% краве мляко",1,100,currentUser,cartItemList);
        Product product3 = new Product(1l,"Краве сирене - Дъбраж", "сирене",0.450,21.55,"100% краве мляко",1,100,currentUser,cartItemList);
        Product product4 = new Product(1l,"Краве кашкавал - Лещен", "кашкавал",0.450,21.55,"100% краве мляко",1,100,currentUser,cartItemList);
        productList.add(product1);
        productList.add(product2);
        productList.add(product3);
        productList.add(product4);
    }

    @Test
    public void createProduct_whenValuesIsCorrect_shouldSaveProduct() {
        //Arrange
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
    public void getCurrentUserProducts_whenUserHasProducts_shouldReturnProducts() {
        //Arrange
        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(productRepository.findBySellerUsername(currentUser.getUsername())).thenReturn(viewProductDtoList);
        //Act
        List<ViewProductDto> result = productService.getCurrentUserProducts();
        //Assert
        assertEquals(viewProductDtoList, result);
    }

    @Test
    public void deleteById_whenItIsExist_shouldDeleteProduct(){
        //Arrange
        Long productId = 2L;
        when(productRepository.existsById(productId)).thenReturn(true);
        //Act
        productService.deleteById(productId);
        //Assert
        verify(productRepository, times(1)).deleteById(productId);
    }

    @Test
    public void deleteById_whenProductIsNotExists_shouldThrowException(){
        //Arrange
        when(productRepository.existsById(product.getId())).thenReturn(false);
        //Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productService.deleteById(product.getId());
        });

        assertEquals("Продуктът с ID " + product.getId() + " не съществува!", exception.getMessage());
        verify(productRepository, never()).deleteById(any());
    }

    @Test
    public void getProductsByType_whenTypeIsAll_shouldReturnAll(){
        //Arrange
        String productType = "всички";
        when(productRepository.findAll()).thenReturn(productList);
        //Act
        List<Product> result = productService.getProductsByType(productType);
        //Assert
        assertEquals(productList,result);
        verify(productRepository, times(1)).findAll();
        verify(productRepository, never()).findProductsByType(any());
    }

    @Test
    public void getProductsByType_whenTypeIsSpecific_shouldReturnAllOfSameType(){
        //Arrange
        String productType = "сирене";
        productList = new ArrayList<>();
        Product product3 = new Product(1l,"Краве сирене - Дъбраж", "сирене",0.450,21.55,"100% краве мляко",1,100,currentUser,cartItemList);
        productList.add(product3);
        when(productRepository.findProductsByType(productType)).thenReturn(productList);
        //Act
        List<Product> result = productService.getProductsByType(productType);
        //Assert
        assertEquals(productList,result);
        verify(productRepository, times(1)).findProductsByType(productType);
        verify(productRepository, never()).findAll();
    }

    @Test
    public void getProductsForSale_whenQuantityIsMoreThanZero_shouldReturnItemsInAvailable(){
        //Arrange
        when(productRepository.findProductsForSale()).thenReturn(viewProductDtoList);
        //Act
        List<ProductForSaleDto> result =  productService.getProductsForSale();
        //Assert
        assertEquals(1, result.size());
        assertEquals("В наличност", result.get(0).getAvailability());
    }
}