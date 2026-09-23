package com.github.destroyex.gestionpedidos.service;

import com.github.destroyex.gestionpedidos.dao.OrderRepository;
import com.github.destroyex.gestionpedidos.dao.ProductRepository;
import com.github.destroyex.gestionpedidos.dto.OrderLineRequestDTO;
import com.github.destroyex.gestionpedidos.dto.OrderLineResponseDTO;
import com.github.destroyex.gestionpedidos.dto.OrderRequestDTO;
import com.github.destroyex.gestionpedidos.dto.OrderResponseDTO;
import com.github.destroyex.gestionpedidos.entity.Order;
import com.github.destroyex.gestionpedidos.entity.OrderLine;
import com.github.destroyex.gestionpedidos.entity.Product;
import com.github.destroyex.gestionpedidos.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    public List<OrderResponseDTO> findAll() {
        return orderRepository.findAll()
                .stream()
                .map(this::toOrderResponseDTO)
                .toList();
    }

    public OrderResponseDTO findById(Long id) {
        return orderRepository.findById(id)
                .map(this::toOrderResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
    }

    public OrderResponseDTO create(OrderRequestDTO orderRequestDTO) {
        Order order = new Order();
        order.setStatus("PENDING");
        order.setOrderDate(LocalDateTime.now());

        List<OrderLine> orderLines = new ArrayList<>();
        for (OrderLineRequestDTO lineDTO : orderRequestDTO.getOrderLines()) {
            Product product = productRepository.findById(lineDTO.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + lineDTO.getProductId()));
            OrderLine orderLine = new OrderLine();
            orderLine.setProduct(product);
            orderLine.setQuantity(lineDTO.getQuantity());
            orderLine.setUnitPrice(product.getPrice());
            orderLine.setOrder(order);
            orderLines.add(orderLine);
        }
        order.setOrderLines(orderLines);

        Order saved = orderRepository.save(order);
        return toOrderResponseDTO(saved);
    }

    public void delete(Long id) {
        Order deletedOrder = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + id));
        orderRepository.delete(deletedOrder);
    }

    public OrderResponseDTO update(Long id, OrderRequestDTO updatedOrder) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("order not found with id: " + id));

        List<OrderLine> orderLines = new ArrayList<>();
        for (OrderLineRequestDTO lineDTO : updatedOrder.getOrderLines()) {
            Product product = productRepository.findById(lineDTO.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + lineDTO.getProductId()));
            OrderLine orderLine = new OrderLine();
            orderLine.setProduct(product);
            orderLine.setQuantity(lineDTO.getQuantity());
            orderLine.setUnitPrice(product.getPrice());
            orderLine.setOrder(existingOrder);
            orderLines.add(orderLine);
        }
        existingOrder.setOrderLines(orderLines);

        Order updated = orderRepository.save(existingOrder);
        return toOrderResponseDTO(updated);

    }

    private OrderLineResponseDTO toOrderLineResponseDTO(OrderLine orderLine) {
        BigDecimal subtotal = orderLine.getUnitPrice().multiply(BigDecimal.valueOf(orderLine.getQuantity()));

        return new OrderLineResponseDTO(
                orderLine.getProduct().getId(),
                orderLine.getProduct().getName(),
                orderLine.getQuantity(),
                orderLine.getUnitPrice(),
                subtotal
        );
    }

    private OrderResponseDTO toOrderResponseDTO(Order order) {
        List<OrderLineResponseDTO> orderLineResponseDTOS = order.getOrderLines()
                .stream()
                .map(this::toOrderLineResponseDTO)
                .toList();

        return new OrderResponseDTO(order.getId(), order.getOrderDate(), order.getStatus(), orderLineResponseDTOS);
    }
}
