package com.ainkai.service;

import com.ainkai.exceptions.OrderException;
import com.ainkai.model.*;
import com.ainkai.repository.*;
import com.ainkai.user.domain.OrderStatus;
import com.ainkai.user.domain.PaymentStatus;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private CartService cartService;
    @Autowired
    private AddressRepo addressRepo;
    @Autowired
    private OrderRepo orderRepo;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private OrderItemRepo orderItemRepo;
    @Autowired
    private UserRepo userRepo;

    public OrderServiceImpl(AddressRepo addressRepo, CartService cartService, OrderItemRepo orderItemRepo, OrderItemService orderItemService, OrderRepo orderRepo, UserRepo userRepo) {
        this.addressRepo = addressRepo;
        this.cartService = cartService;
        this.orderItemRepo = orderItemRepo;
        this.orderItemService = orderItemService;
        this.orderRepo = orderRepo;
        this.userRepo = userRepo;
    }

    @Override
    public Order createOrder(User user, Address shippingAddress) {
        //Set The User for Shiping address
        shippingAddress.setUser(user);
        //after saving the user, save to the database
        Address address = addressRepo.save(shippingAddress);
        //add the saved address to the user Addresses List
        user.getAddresses().add(address);
        //save the User
        userRepo.save(user);

        //Now Fetch the cart of the particular user
        Cart cart = cartService.findUserCart(user.getId());

        //now create a List of Order items and fetch each order Item One by One
        List<OrderItem> orderItems = new ArrayList<>();

        //Loop through Each cart item, and add the cart item to respective orderitem
        for(CartItem item :cart.getCartItems() ){
            OrderItem orderItem = new OrderItem();

            orderItem.setPrice(item.getPrice());
            orderItem.setProduct(item.getProduct());
            orderItem.setDiscountedPrice(item.getDiscountedPrice());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setSize(item.getSize());
            orderItem.setUserId(item.getUserId());

            OrderItem createdOrderItem = orderItemRepo.save(orderItem);
            orderItems.add(createdOrderItem);
        }

        Order createdOrder = new Order();
        createdOrder.setUser(user);
        createdOrder.setOrderItemList(orderItems);
        createdOrder.setTotalPrice(cart.getTotalPrice());
        createdOrder.setTotalDiscountedPrice(cart.getTotalDiscountedPrice());
        createdOrder.setDiscount(cart.getDiscount());
        createdOrder.setTotalItem(cart.getTotalItem());
        createdOrder.setOrderId(user.getLastName() + (cart.getId()));
        createdOrder.setShippingAddress(address);
        createdOrder.setOrderDate(LocalDateTime.now());
        createdOrder.setOrderStatus(OrderStatus.PENDING);
        createdOrder.getPaymentDetails().setStatus(PaymentStatus.PENDING);
        createdOrder.setCreatedAt(LocalDateTime.now());

        Order saveOrder = orderRepo.save(createdOrder);

        //Update the order items to set the order or (set the ORDER for orderItems)
        for(OrderItem item : orderItems){
            item.setOrder(saveOrder);
            orderItemRepo.save(item);
        }

        return saveOrder;

    }

    @Override
    public Order findOrderById(Long orderId) throws OrderException {
        Optional<Order> opt = orderRepo.findById(orderId);

        if(opt.isPresent()){
            return opt.get();
        }
        throw  new OrderException("ORDER NOT EXISTS WITH ORDER ID : "+ orderId);

    }

    @Override
    public List<Order> usersOrderHistory(Long userId) {
       List<Order> orderList = orderRepo.getUsersOrders(userId);
       return orderList;
    }

    @Override
    public Order placedOrder(Long orderId) throws OrderException {
        return null;
    }

    @Override
    public Order confirmedOrder(Long orderId) throws OrderException {
        return null;
    }

    @Override
    public Order shippedOrder(Long orderId) throws OrderException {
        return null;
    }

    @Override
    public Order deliveredOrder(Long orderId) throws OrderException {
        return null;
    }

    @Override
    public Order cancledOrder(Long orderId) throws OrderException {
        return null;
    }

    @Override
    public List<Order> getAllOrders() {
        return List.of();
    }

    @Override
    public void deleteOrder(Long orderId) throws OrderException {

    }
}
