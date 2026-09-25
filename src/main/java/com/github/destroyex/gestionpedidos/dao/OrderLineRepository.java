package com.github.destroyex.gestionpedidos.dao;

import com.github.destroyex.gestionpedidos.entity.OrderLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderLineRepository extends JpaRepository<OrderLine, Long> {
}
