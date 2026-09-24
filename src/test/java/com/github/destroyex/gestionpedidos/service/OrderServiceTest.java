package com.github.destroyex.gestionpedidos.service;

import com.github.destroyex.gestionpedidos.dao.OrderRepository;
import com.github.destroyex.gestionpedidos.dao.ProductRepository;
import com.github.destroyex.gestionpedidos.dto.OrderLineRequestDTO;
import com.github.destroyex.gestionpedidos.dto.OrderRequestDTO;
import com.github.destroyex.gestionpedidos.dto.OrderResponseDTO;
import com.github.destroyex.gestionpedidos.entity.Order;
import com.github.destroyex.gestionpedidos.entity.OrderLine;
import com.github.destroyex.gestionpedidos.entity.Product;
import com.github.destroyex.gestionpedidos.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private OrderService orderService;

    @Test
    public void findById_ShouldReturnOrder_IfExists() {
        Product product = new Product(1L, "Apple", "An apple", BigDecimal.valueOf(10), 10);
        Order order = new Order(1L, LocalDateTime.now(), "PENDING", null);
        OrderLine orderLine = new OrderLine(1L, product, 5, BigDecimal.valueOf(10), order);
        order.setOrderLines(List.of(orderLine));

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        OrderResponseDTO responseDTO = orderService.findById(1L);

        assertEquals(1, responseDTO.getOrderLines().size());
        assertEquals("PENDING", responseDTO.getStatus());
    }

    @Test
    public void findById_ShouldThrowException_IfNotExists() {
        when(orderRepository.findById(99L)).thenReturn(Optional.empty());
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, ()
                -> orderService.findById(99L));

        assertEquals("Order not found with id: 99", exception.getMessage());
    }

    @Test
    public void create_ShouldReturnNewOrder() {
        Product product = new Product(1L, "Apple", "An apple", BigDecimal.valueOf(10), 10);
        OrderLineRequestDTO orderLineRequestDTO = new OrderLineRequestDTO(1L, 5);
        OrderRequestDTO orderRequestDTO = new OrderRequestDTO(List.of(orderLineRequestDTO));

        Order order = new Order(1L, LocalDateTime.now(), "PENDING", null);
        OrderLine orderLine = new OrderLine(1L, product, 5, BigDecimal.valueOf(10), order);
        order.setOrderLines(List.of(orderLine));

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        OrderResponseDTO responseDTO = orderService.create(orderRequestDTO);
        assertEquals(1, responseDTO.getOrderLines().size());
    }

    @Test
    public void update_ShouldReturnUpdatedOrder_IfExists() {
        Product product = new Product(1L, "Apple", "An apple", BigDecimal.valueOf(10), 10);
        Order order = new Order(1L, LocalDateTime.now(), "PENDING", null);
        OrderLine orderLine = new OrderLine(1L, product, 5, BigDecimal.valueOf(10), order);
        order.setOrderLines(List.of(orderLine));

        OrderLineRequestDTO orderLineRequestDTO = new OrderLineRequestDTO(1L, 8);
        OrderRequestDTO orderRequestDTO = new OrderRequestDTO(List.of(orderLineRequestDTO));

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        OrderResponseDTO responseDTO = orderService.update(1L, orderRequestDTO);
        assertEquals(8, responseDTO.getOrderLines().getFirst().getQuantity());
    }

    @Test
    public void update_ShouldThrowException_IfNotExists() {
        OrderRequestDTO updatedRequestDTO = new OrderRequestDTO();

        when(orderRepository.findById(99L)).thenReturn(Optional.empty());
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, ()
                -> orderService.update(99L, updatedRequestDTO));

        assertEquals("Order not found with id: 99", exception.getMessage());

    }

    @Test
    public void delete_ShouldDeleteOrder_IfExists() {
        Order order = new Order(1L, LocalDateTime.now(), "PENDING", null);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        orderService.delete(1L);

        verify(orderRepository).delete(order);
    }

    @Test
    public void delete_ShouldThrowException_IfNotExists() {

        when(orderRepository.findById(99L)).thenReturn(Optional.empty());
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, ()
                -> orderService.delete(99L));

        assertEquals("Order not found with id: 99", exception.getMessage());
    }
}
