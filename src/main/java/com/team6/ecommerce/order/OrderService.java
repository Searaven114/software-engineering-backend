package com.team6.ecommerce.order;



import com.team6.ecommerce.address.Address;
import com.team6.ecommerce.cart.Cart;
import com.team6.ecommerce.cartitem.CartItem;
import com.team6.ecommerce.mail.MailService;
import com.team6.ecommerce.product.Product;
import com.team6.ecommerce.product.ProductRepository;
import com.team6.ecommerce.user.User;
import com.team6.ecommerce.user.UserRepository;
import com.team6.ecommerce.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Log4j2
@AllArgsConstructor
@Service
public class OrderService {


    private final OrderRepository orderRepo;
    private final UserService userService;
    private final UserRepository userRepo;
    private final ProductRepository productRepo;
    private final MailService mailService;


    @Secured({"ROLE_ADMIN"})
    @PreAuthorize("isAuthenticated()")
    public List<Order> getAllOrdersAdmin( OrderStatus orderStatus){
        return orderRepo.findAllByOrderStatus( orderStatus );
    }


    @Secured({"ROLE_ADMIN"})
    @PreAuthorize("isAuthenticated()")
    public List<Order> getAllOrdersByUserIdAdmin(String userId, OrderStatus orderStatus){

        if ( orderStatus == null){
            return orderRepo.findAll();
        } else {
            return orderRepo.findAllByUserIdAndOrderStatus(userId, orderStatus);
        }
    }


    /**
     * Fetch an order by its ID.
     *
     * @param orderId The order ID
     * @return The order if found
     */
    public Optional<Order> fetchOrderById(String orderId) {
        log.info("[OrderService][fetchOrderById] Fetching order with ID: {}", orderId);

        if (orderId == null || orderId.isEmpty()) {
            log.error("[OrderService][fetchOrderById] Invalid order ID provided.");
            throw new IllegalArgumentException("Order ID cannot be null or empty.");
        }

        Optional<Order> order = orderRepo.findById(orderId);

        if (order.isEmpty()) {
            log.info("[OrderService][fetchOrderById] No order found with ID: {}", orderId);
        } else {
            log.info("[OrderService][fetchOrderById] Order found with ID: {}", orderId);
        }

        return order;
    }


    /**
     * Fetch all orders for a specific user ID.
     *
     * @param userId The user ID
     * @return List of orders for the user
     */
    public List<Order> fetchOrdersByUserId(String userId) {

        log.info("[OrderService][fetchOrdersByUserId] Fetching orders for User ID: {}", userId);

        if (userId == null || userId.isEmpty()) {
            log.error("[OrderService][fetchOrdersByUserId] Invalid user ID provided.");
            throw new IllegalArgumentException("User ID cannot be null or empty.");
        }

        List<Order> orders = orderRepo.findAllByUserId(userId);

        log.info("[OrderService][fetchOrdersByUserId] Retrieved {} orders for User ID: {}", orders.size(), userId);
        return orders;
    }


    /**
     * Fetch all orders by their status.
     *
     * @param status The order status
     * @return List of orders with the specified status
     */
    public List<Order> fetchOrdersByStatus(OrderStatus status) {
        log.info("[OrderService][fetchOrdersByStatus] Fetching orders with status: {}", status);

        List<Order> orders = orderRepo.findAllByOrderStatus(status);

        log.info("[OrderService][fetchOrdersByStatus] Retrieved {} orders with status: {}", orders.size(), status);
        return orders;
    }



//    @Scheduled(fixedRate = 30000)
//    public void simulateOrderStatus() {
//        log.info("[OrderService][simulateOrderStatus] Starting order status simulation...");
//
//        // Fetch all orders with status 'PROCESSING'
//        List<Order> processingOrders = orderRepo.findAllByOrderStatus(OrderStatus.PROCESSING);
//
//        for (Order order : processingOrders) {
//            log.info("[OrderService][simulateOrderStatus] Updating order ID: {} from PROCESSING to IN_TRANSIT", order.getId());
//
//            // Update the status
//            order.setOrderStatus(OrderStatus.IN_TRANSIT);
//
//            // Save the updated order
//            orderRepo.save(order);
//
//            log.info("[OrderService][simulateOrderStatus] Order ID: {} successfully updated to IN_TRANSIT", order.getId());
//        }
//
//        log.info("[OrderService][simulateOrderStatus] Simulation completed.");
//    }





//    @Scheduled(fixedRate = 40000)
//    public void simulateDeliveryStatus() {
//        log.info("[OrderService][simulateDeliveryStatus] Starting delivery status simulation...");
//
//        // Fetch all orders with status 'IN_TRANSIT'
//        List<Order> inTransitOrders = orderRepo.findAllByOrderStatus(OrderStatus.IN_TRANSIT);
//
//        for (Order order : inTransitOrders) {
//            log.info("[OrderService][simulateDeliveryStatus] Updating order ID: {} from IN_TRANSIT to DELIVERED", order.getId());
//
//            // Update the status
//            order.setOrderStatus(OrderStatus.DELIVERED);
//
//            // Save the updated order
//            orderRepo.save(order);
//
//            log.info("[OrderService][simulateDeliveryStatus] Order ID: {} successfully updated to DELIVERED", order.getId());
//        }
//
//        log.info("[OrderService][simulateDeliveryStatus] Delivery simulation completed.");
//    }


    @Transactional
    public Order createOrder(String userId, Cart cart, Address deliveryAddress) {
        log.info("[OrderService][createOrder] Creating order for user ID: {}", userId);

        if (cart == null || cart.getCartItems().isEmpty()) {
            log.error("[OrderService][createOrder] Cart cannot be null or empty.");
            throw new IllegalArgumentException("Cart cannot be null or empty.");
        }

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        Order order = Order.builder()
                .userId(userId)
                .cart(cart)
                .orderStatus(OrderStatus.PROCESSING)
                .createdAt(new Date())
                .total(cart.getTotalPrice().longValue())
                .address(deliveryAddress) // Assuming the first address is used
                .build();

        orderRepo.save(order);
        log.info("[OrderService][createOrder] Order created with ID: {}", order.getId());
        return order;
    }


    @Transactional
    public String requestRefund(String orderId) {
        log.info("[OrderService][requestRefund] Requesting refund for order ID: {}", orderId);

        // Fetch the order
        Optional<Order> orderOpt = orderRepo.findById(orderId);
        if (orderOpt.isEmpty()) {
            throw new IllegalArgumentException("Order not found.");
        }
        Order order = orderOpt.get();

        // Validate if the order is eligible for refund (must not be already refunded)
        if (order.getOrderStatus().equals(OrderStatus.REFUNDED)) {
            throw new IllegalArgumentException("This order has already been refunded.");
        }

        long daysSincePurchase = (new Date().getTime() - order.getCreatedAt().getTime()) / (1000 * 60 * 60 * 24);
        if (daysSincePurchase > 30) {
            throw new IllegalArgumentException("Refund window expired. Orders must be refunded within 30 days.");
        }

        // Mark the order as pending refund
        order.setOrderStatus(OrderStatus.PENDING_REFUND);
        orderRepo.save(order);

        log.info("[OrderService][requestRefund] Refund request recorded for order ID: {}", orderId);
        return "Refund request recorded successfully.";
    }


    public List<Order> getPendingRefunds() {
        log.info("[OrderService][getPendingRefunds] Fetching all orders with status PENDING_REFUND.");

        return orderRepo.findAllByOrderStatus(OrderStatus.PENDING_REFUND);
    }



    @Transactional
    public String processRefund(String orderId, boolean approve) {
        log.info("[OrderService][processRefund] Processing refund for order ID: {}, approve: {}", orderId, approve);

        // Fetch the order
        Optional<Order> orderOpt = orderRepo.findById(orderId);
        if (orderOpt.isEmpty()) {
            throw new IllegalArgumentException("Order not found.");
        }
        Order order = orderOpt.get();

        // Check the refund status
        if (!order.getOrderStatus().equals(OrderStatus.PENDING_REFUND)) {
            throw new IllegalArgumentException("Order is not pending a refund.");
        }

        if (approve) {
            // Validate product and quantities before processing the refund
            for (CartItem cartItem : order.getCart().getCartItems()) {
                Product product = productRepo.findById(cartItem.getProduct().getId())
                        .orElseThrow(() -> new RuntimeException("Product not found: " + cartItem.getProduct().getId()));

                // Ensure the stock increment matches the cartItem quantity
                int adjustedStock = product.getQuantityInStock() + cartItem.getQuantity();
                product.setQuantityInStock(adjustedStock);
                productRepo.save(product);
            }

            // Update order status to REFUNDED
            order.setOrderStatus(OrderStatus.REFUNDED);
            orderRepo.save(order);

            // Notify the user via email
            User user = userRepo.findById(order.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found: " + order.getUserId()));
            mailService.sendRefundApprovalMail(user.getEmail(), orderId);

            log.info("[OrderService][processRefund] Refund approved for order ID: {}", orderId);
            return "Refund approved and processed successfully.";
        } else {
            // If refund is disapproved, reset status to PROCESSING
            order.setOrderStatus(OrderStatus.PROCESSING);
            orderRepo.save(order);

            log.info("[OrderService][processRefund] Refund disapproved for order ID: {}", orderId);
            return "Refund disapproved.";
        }
    }




}























