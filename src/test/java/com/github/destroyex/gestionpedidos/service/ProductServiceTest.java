package com.github.destroyex.gestionpedidos.service;

import com.github.destroyex.gestionpedidos.dao.ProductRepository;
import com.github.destroyex.gestionpedidos.dto.ProductRequestDTO;
import com.github.destroyex.gestionpedidos.dto.ProductResponseDTO;
import com.github.destroyex.gestionpedidos.entity.Product;
import com.github.destroyex.gestionpedidos.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductService productService;

    @Test
    public void findById_ShouldReturnProduct_IfExists() {
        Product product = new Product(1L, "Apple", "An apple", BigDecimal.valueOf(10), 10);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductResponseDTO result = productService.findById(1L);

        assertEquals(1L, result.getId());
        assertEquals("Apple", result.getName());
    }

    @Test
    public void findById_ShouldThrowException_IfNotExists() {

        when(productRepository.findById(99L)).thenReturn(Optional.empty());
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, ()
                -> productService.findById(99L));

        assertEquals("Product not found with id: 99", exception.getMessage());
    }

    @Test
    public void create_ShouldReturnNewProduct() {
        ProductRequestDTO productRequestDTO = new ProductRequestDTO("Apple", "An apple", BigDecimal.valueOf(10), 10);
        Product savedProduct = new Product(1L, "Apple", "An apple", BigDecimal.valueOf(10), 10);

        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        ProductResponseDTO responseDTO = productService.create(productRequestDTO);
        assertEquals("Apple", responseDTO.getName());
    }

    @Test
    public void update_ShouldReturnUpdatedProduct_IfExists() {
        Product product = new Product(1L, "Apple", "An apple", BigDecimal.valueOf(10), 10);
        ProductRequestDTO updatedRequestDTO = new ProductRequestDTO("Banana", "A banana", BigDecimal.valueOf(15), 5);
        Product updatedProduct = new Product(1L, "Banana", "A banana", BigDecimal.valueOf(15), 5);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        ProductResponseDTO result = productService.update(1L, updatedRequestDTO);

        assertEquals("Banana", result.getName());
    }

    @Test
    public void update_ShouldThrowException_IfNotExists() {
        ProductRequestDTO updatedRequestDTO = new ProductRequestDTO("Banana", "A banana", BigDecimal.valueOf(15), 5);

        when(productRepository.findById(99L)).thenReturn(Optional.empty());
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, ()
                -> productService.update(99L, updatedRequestDTO));

        assertEquals("Product not found with id: 99", exception.getMessage());

    }

    @Test
    public void delete_ShouldDeleteProduct_IfExists() {
        Product product = new Product(1L, "Apple", "An apple", BigDecimal.valueOf(10), 10);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        productService.delete(1L);

        verify(productRepository).delete(product);

    }

    @Test
    public void delete_ShouldThrowException_IfNotExists() {

        when(productRepository.findById(99L)).thenReturn(Optional.empty());
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, ()
                -> productService.delete(99L));

        assertEquals("Product not found with id: 99", exception.getMessage());
    }
}
