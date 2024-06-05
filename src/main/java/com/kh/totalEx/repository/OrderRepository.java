package com.kh.totalEx.repository;

import com.kh.totalEx.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
