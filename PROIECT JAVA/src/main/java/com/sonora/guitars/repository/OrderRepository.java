package com.sonora.guitars.repository;

import com.sonora.guitars.entity.Order;
import com.sonora.guitars.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
    List<Order> findByUserUsername(String username);
}
