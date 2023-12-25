package com.example.pizza.service;

import com.example.pizza.persistence.entity.OrderEntity;
import com.example.pizza.persistence.projection.OrderSummary;
import com.example.pizza.persistence.repository.OrderRepository;
import com.example.pizza.service.dto.RandomOrderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class OrderService {
  private final OrderRepository orderRepository;

  @Autowired
  public OrderService(OrderRepository orderRepository) {
    this.orderRepository = orderRepository;
  }

  private static final String DELIVERY = "D";
  private static final String CARRYOUT = "C";
  private static final String ON_SITE = "S";

  public List<OrderEntity> getAll() {
    List<OrderEntity> orders = this.orderRepository.findAll();
    orders.forEach(o -> System.out.println(o.getCustomer().getName()));
    return orders;
  }

  public List<OrderEntity> getTodayOrders() {
    LocalDateTime today = LocalDate.now().atTime(0, 0);
    return this.orderRepository.findAllByDateAfter(today);
  }

  public List<OrderEntity> getOutsideOrders() {
    List<String> methods = Arrays.asList(DELIVERY, CARRYOUT);
    return this.orderRepository.findAllByMethodIn(methods);
  }

  @Secured("ROLE_ADMIN")
  public List<OrderEntity> getCustomerOrders(String idCustomer) {
    return this.orderRepository.findCustomerOrders(idCustomer);
  }

  public OrderSummary getSummary(int orderId) {
    return this.orderRepository.findSummary(orderId);
  }

  @Transactional
  public boolean saveRandomOrder(RandomOrderDto randomOrderDto) {
    return this.orderRepository.saveRandomOrder(randomOrderDto.getIdCustomer(), randomOrderDto.getMethod());
  }
}
