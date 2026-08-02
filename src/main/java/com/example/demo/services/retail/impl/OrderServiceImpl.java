package com.example.demo.services.retail.impl;

import com.example.demo.dtos.retail.request.OrderRequestDto;
import com.example.demo.dtos.retail.response.OrderResponseDto;
import com.example.demo.exceptions.InsufficientStockException;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.mappers.OrderMapper;
import com.example.demo.models.retail.Order;
import com.example.demo.models.retail.OrderItem;
import com.example.demo.models.retail.Product;
import com.example.demo.models.users.User;
import com.example.demo.repositories.retail.OrderRepository;
import com.example.demo.repositories.retail.ProductRepository;
import com.example.demo.repositories.users.UserRepository;
import com.example.demo.services.retail.OrderService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public OrderResponseDto createOrder(OrderRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + requestDto.getUserId()));

        Order order = new Order();
        order.setUser(user);

        for (Map.Entry<Long, Integer> entry : requestDto.getProducts().entrySet()) {
            Long productId = entry.getKey();
            Integer quantityToBuy = entry.getValue();

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

            if (product.getStockQuantity() < quantityToBuy) {
                throw new InsufficientStockException("Not enough products in stock: " + product.getName());
            }

            product.setStockQuantity(product.getStockQuantity() - quantityToBuy);

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(quantityToBuy);
            orderItem.setPrice(product.getPrice());

            order.getItems().add(orderItem);
        }
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toResponseDto(savedOrder);
    }
}
