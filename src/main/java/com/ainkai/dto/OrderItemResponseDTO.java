package com.ainkai.dto;

import com.ainkai.model.OrderItem;
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
public class OrderItemResponseDTO {

    private Long id;

    private ProductResponseDTO product;

    private String size;

    private int quantity;

    private  Integer price;

    private Integer discountedPrice;

    private  Long userId;

    private LocalDateTime deliveryDate;


    public static OrderItemResponseDTO fromEntity(OrderItem orderItem){
        OrderItemResponseDTO orderItemDto = new OrderItemResponseDTO();
        orderItemDto.setId(orderItem.getId());
        orderItemDto.setProduct(ProductResponseDTO.fromEntity(orderItem.getProduct()));
        orderItemDto.setSize(orderItem.getSize());
        orderItemDto.setQuantity(orderItem.getQuantity());
        orderItemDto.setDiscountedPrice(orderItem.getDiscountedPrice());
        orderItemDto.setPrice(orderItem.getPrice());
        orderItemDto.setUserId(orderItem.getUserId());
        orderItem.setDeliveryDate(orderItem.getDeliveryDate());
        return orderItemDto;
    }

    public static List<OrderItemResponseDTO> fromEntityToList(List<OrderItem> orderItemList){
        List<OrderItemResponseDTO> orderItemResponseDTOList = new ArrayList<>();
        for(OrderItem orderItem : orderItemList ){
            orderItemResponseDTOList.add(OrderItemResponseDTO.fromEntity(orderItem));
        }
        return orderItemResponseDTOList;

    }


}
