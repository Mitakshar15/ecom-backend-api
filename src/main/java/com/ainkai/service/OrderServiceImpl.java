package com.ainkai.service;

import com.ainkai.emailservice.OrderConfirmationEmail;
import com.ainkai.exceptions.OrderException;
import com.ainkai.exceptions.ProductException;
import com.ainkai.model.*;
import com.ainkai.repository.*;
import com.ainkai.user.domain.OrderStatus;
import com.ainkai.user.domain.PaymentStatus;
import jakarta.mail.MessagingException;
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
    @Autowired
    private  UserService userService;
    @Autowired
    private OrderConfirmationEmail orderConfirmationEmailsender;
    @Autowired
    private  ProductService productService;

    public OrderServiceImpl(AddressRepo addressRepo, CartService cartService, OrderItemRepo orderItemRepo, OrderItemService orderItemService, OrderRepo orderRepo, UserRepo userRepo,OrderConfirmationEmail orderConfirmationEmailsender,UserService userService,ProductService productService) {
        this.addressRepo = addressRepo;
        this.cartService = cartService;
        this.orderItemRepo = orderItemRepo;
        this.orderItemService = orderItemService;
        this.orderRepo = orderRepo;
        this.userRepo = userRepo;
        this.orderConfirmationEmailsender = orderConfirmationEmailsender;
        this.userService = userService;
        this.productService = productService;
    }

    @Override
    public Order createOrder(User user, Address shippingAddress)throws ProductException {
        //Set The User for Shiping address
        shippingAddress.setUser(user);
        //after saving the user, save to the database

        //Add Checks for Alredy existing Address
        Address finalAddress = new Address();

        if(isShippingAddressExists(user.getId(),shippingAddress)){

           Optional<Address> opt=addressRepo.findByStreetAddressAndCityAndStateAndZipCodeAndUser(shippingAddress.getStreetAddress(),shippingAddress.getCity(),shippingAddress.getState(),shippingAddress.getZipCode(),user);
           if(opt.isPresent()){
               finalAddress = opt.get();
           }

        }
        else{

            Address address = addressRepo.save(shippingAddress);
            finalAddress = address;
            //add the saved address to the user Addresses List
            user.getAddresses().add(address);
            //save the User
            userRepo.save(user);
        }


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
            orderItem.setDeliveryDate(LocalDateTime.now().plusDays(4));
            OrderItem createdOrderItem = orderItemRepo.save(orderItem);
            orderItems.add(createdOrderItem);
            Product product = orderItem.getProduct();
            if(product.getQuantity()>orderItem.getQuantity()){
                product.setQuantity(product.getQuantity()-orderItem.getQuantity());
            }
            productService.updateProduct(product.getId(),product);
        }

        Order createdOrder = new Order();
        createdOrder.setUser(user);
        createdOrder.setOrderItemList(orderItems);
        createdOrder.setTotalPrice(cart.getTotalPrice());
        createdOrder.setTotalDiscountedPrice(cart.getTotalDiscountedPrice());
        createdOrder.setDiscount(cart.getDiscount());
        createdOrder.setTotalItem(cart.getTotalItem());
        createdOrder.setOrderId(user.getLastName() + (cart.getId()));
        createdOrder.setShippingAddress(finalAddress);
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
    public Order placedOrder(Long orderId) throws OrderException, MessagingException {

        Order order = findOrderById(orderId);
        order.setOrderStatus(OrderStatus.PLACED);
        order.getPaymentDetails().setStatus(PaymentStatus.COMPLETED);
        return   orderRepo.save(order);
    }

    @Override
    public Order confirmedOrder(Long orderId) throws OrderException {
        Order order = findOrderById(orderId);
        order.setOrderStatus(OrderStatus.CONFIRMED);
        return   orderRepo.save(order);
    }

    @Override
    public Order shippedOrder(Long orderId) throws OrderException {
        Order order = findOrderById(orderId);
        order.setOrderStatus(OrderStatus.SHIPPED);
        return   orderRepo.save(order);
    }

    @Override
    public Order deliveredOrder(Long orderId) throws OrderException {
        Order order = findOrderById(orderId);
        order.setOrderStatus(OrderStatus.DELIVERED);
        return  order;
    }

    @Override
    public Order cancledOrder(Long orderId) throws OrderException {
        Order order = findOrderById(orderId);
        order.setOrderStatus(OrderStatus.CANCELLED);
        return  orderRepo.save(order);
    }

    @Override
    public List<Order> getAllOrders() {
       return  orderRepo.findAll();
    }

    @Override
    public void deleteOrder(Long orderId) throws OrderException {
        Order order = findOrderById(orderId);
        orderRepo.deleteById(orderId);

    }

    @Override
    public boolean isShippingAddressExists(Long userId, Address address) {

        Optional<User> opt = userRepo.findById(userId);
        User user = new User();
        if(opt.isPresent()){
         user = opt.get();
        }
        if(addressRepo.existsByStreetAddressAndCityAndStateAndZipCodeAndUser(address.getStreetAddress(),address.getCity(),address.getState(),address.getZipCode(),user)){
            return true;
        }
        else{
            return false;
        }



    }
}
