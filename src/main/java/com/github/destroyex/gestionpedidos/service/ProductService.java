package com.github.destroyex.gestionpedidos.service;

import com.github.destroyex.gestionpedidos.dao.ProductRepository;
import com.github.destroyex.gestionpedidos.dto.ProductRequestDTO;
import com.github.destroyex.gestionpedidos.dto.ProductResponseDTO;
import com.github.destroyex.gestionpedidos.entity.Product;
import com.github.destroyex.gestionpedidos.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductResponseDTO> findAll() {
        return productRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public ProductResponseDTO findById(Long id) {
        return productRepository.findById(id)
                .map(this::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    public ProductResponseDTO create(ProductRequestDTO requestDTO) {
        Product product = new Product();

        product.setName(requestDTO.getName());
        product.setDescription(requestDTO.getDescription());
        product.setPrice(requestDTO.getPrice());
        product.setStock(requestDTO.getStock());

        Product saved = productRepository.save(product);
        return toResponseDTO(saved);
    }

    public void delete(Long id) {
        Product deletedProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        productRepository.delete(deletedProduct);
    }

    public ProductResponseDTO update(Long id, ProductRequestDTO updatedProduct) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        existingProduct.setName(updatedProduct.getName());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setStock(updatedProduct.getStock());

        Product updated = productRepository.save(existingProduct);
        return toResponseDTO(updated);
    }

    private ProductResponseDTO toResponseDTO(Product product) {
        return new ProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock()
        );
    }
}
