package com.github.destroyex.gestionpedidos.dao;

import com.github.destroyex.gestionpedidos.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
