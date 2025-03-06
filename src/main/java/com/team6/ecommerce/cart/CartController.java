package com.team6.ecommerce.cart;
import com.team6.ecommerce.cart.dto.CheckoutResponseDTO;
import com.team6.ecommerce.cartitem.CartItem;
import com.team6.ecommerce.constants.Strings;
import com.team6.ecommerce.invoice.Invoice;
import com.team6.ecommerce.invoice.InvoiceService;
import com.team6.ecommerce.notification.NotificationService;
import com.team6.ecommerce.payment.dto.PaymentRequest;
import com.team6.ecommerce.payment.dto.PaymentRequestDTO;
import com.team6.ecommerce.user.User;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@Log4j2
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;
    private final InvoiceService invoiceService;
    private final NotificationService notificationService;


    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<?> fetchUserCart() {

        String userId = getAuthenticatedUserId("fetchUserCart");

        Cart cart = cartService.fetchUserCart( userId );

        if (cart.getCartItems().isEmpty()) {
            log.info("[CartController][fetchUserCart] Cart is empty for user with ID: {}", userId);

            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Your cart is empty.");
        }

        return ResponseEntity.ok(cart);
    }


//━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━//

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/clear")
    public ResponseEntity<?> clearCart() {

        String userId = getAuthenticatedUserId("clearCart");

        String result = cartService.clearUserCart(userId);

        if (Strings.CART_IS_EMPTY.equals(result)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(result);
        }
        return ResponseEntity.ok(result);
    }


//━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━//

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/cart/add")
    public ResponseEntity<String> addProductToCart(@RequestParam String productId, @RequestParam int quantity) {

        String userId = getAuthenticatedUserId("addProductToCart");

        String result = cartService.addItemToUserCart(userId, productId, quantity);

        if (Strings.PRODUCT_NOT_FOUND.equals(result)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);

        } else if (Strings.PRODUCT_OUT_OF_STOCK.equals(result)) {
            return ResponseEntity.badRequest().body(result);

        } else if (result.startsWith(String.format(Strings.PRODUCT_NOT_AVAILABLE_IN_REQUESTED_QUANTITY, 0))) {
            return ResponseEntity.badRequest().body(result);

        } else {
            return ResponseEntity.ok(result);
        }
    }


    //━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━//

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/update-quantity")
    public ResponseEntity<String> updateCartItemQuantity(@RequestParam String productId, @RequestParam int quantity) {

        String userId = getAuthenticatedUserId("updateCartItemQuantity");

        String result = cartService.updateCartItemQuantity(userId, productId, quantity);

        if (Strings.CART_IS_EMPTY.equals(result)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(result);

        } else if (Strings.PRODUCT_NOT_IN_CART.equals(result)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);

        } else if (result.startsWith(String.format(Strings.PRODUCT_NOT_AVAILABLE_IN_REQUESTED_QUANTITY, 0))) {
            return ResponseEntity.badRequest().body(result);
        }

        return ResponseEntity.ok(result);
    }

    //━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━//

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/remove")
    public ResponseEntity<String> removeProductFromCart(@RequestParam String productId) {

        String userId = getAuthenticatedUserId("removeProductFromCart");

        String result = cartService.removeItemFromUserCart(userId, productId);

        if (result.equals(Strings.PRODUCT_NOT_IN_CART)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
        return ResponseEntity.ok(result);
    }


    //━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━//


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@RequestBody PaymentRequestDTO paymentRequest) {

        String userId = getAuthenticatedUserId("checkout");

        CheckoutResponseDTO response = cartService.checkout(userId, paymentRequest);

        if (response == null) {
            log.warn("[CartController][Checkout] Checkout failed. Cart might be empty for user ID: {}", userId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Strings.CART_IS_EMPTY);
        }

        log.info("[CartController][Checkout] Checkout successful for user ID: {}", userId);
        return ResponseEntity.ok(response);
    }


    //━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━//

    private String getAuthenticatedUserId(String methodName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            log.warn("[CartController][{}] Unauthorized access attempt.", methodName);
            throw new IllegalStateException("User is not authenticated.");
        }
        if (!(authentication.getPrincipal() instanceof User)) {
            log.warn("[CartController][{}] Invalid principal type.", methodName);
            throw new IllegalStateException("Invalid user principal.");
        }
        User user = (User) authentication.getPrincipal();
        log.info("[CartController][{}] Authenticated user ID: {}", methodName, user.getId());
        return user.getId();
    }

}