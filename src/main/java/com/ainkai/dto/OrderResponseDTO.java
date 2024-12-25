package com.ainkai.dto;

import com.ainkai.model.Address;
import com.ainkai.model.Order;
import com.ainkai.model.OrderItem;
import com.ainkai.user.domain.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderResponseDTO {



    private Long id;

    private String order_id;

    private Long user_id;

    private List<OrderItemResponseDTO> orderItemList;

    private LocalDateTime orderDate;

    private LocalDateTime deliveryDate;

    private Address shippingAddress;

    private double totalPrice;

    private Integer totalDiscountedPrice;

    private Integer discount;

    private OrderStatus OrderStatus;

    private int totalItem;

    private  LocalDateTime createdAt;



    public static OrderResponseDTO fromEntity(Order order){
        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setId(order.getId());
        dto.setOrder_id(order.getOrderId());
        dto.setDiscount(order.getDiscount());
        dto.setTotalPrice(order.getTotalPrice());
        dto.setTotalDiscountedPrice(order.getTotalDiscountedPrice());
        dto.setOrderItemList(OrderItemResponseDTO.fromEntityToList(order.getOrderItemList()));
        dto.setTotalItem(order.getTotalItem());
        dto.setShippingAddress(order.getShippingAddress());

        dto.setUser_id(order.getUser().getId());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setDeliveryDate(order.getDeliveryDate());
        dto.setOrderDate(order.getOrderDate());
        dto.setOrderStatus(order.getOrderStatus());

        return dto;
    }

    public static List<OrderResponseDTO> fromEntityToList(List<Order> orderList){
        List<OrderResponseDTO> dtoList = new ArrayList<>();
        for(Order order : orderList){
            dtoList.add(OrderResponseDTO.fromEntity(order));
        }
        return dtoList;
    }

}
