package com.team6.ecommerce.notification;


import com.team6.ecommerce.invoice.Invoice;
import com.team6.ecommerce.invoice.InvoiceService;
import com.team6.ecommerce.mail.MailService;
import com.team6.ecommerce.product.Product;
import com.team6.ecommerce.user.User;
import com.team6.ecommerce.user.UserRepository;
import com.team6.ecommerce.wishlist.WishlistService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@AllArgsConstructor
@Service
public class NotificationService {

    private final InvoiceService invoiceService;
    private final MailService mailService;
    private final UserRepository userRepository;
    private final WishlistService wishlistService;


    public void notifyUsersAboutDiscount(String productId, double discountRate) {
        log.info("[NotificationService][notifyUsersAboutDiscount] Preparing to notify users about discount on product ID: {}", productId);

        // Retrieve user IDs with the product in their wishlist
        List<String> userIds = wishlistService.getUsersByProductInWishlist(productId);

        if (userIds.isEmpty()) {
            log.info("[NotificationService][notifyUsersAboutDiscount] No users found with product ID: {} in their wishlist.", productId);
            return;
        }

        // Map user IDs to email addresses
        List<String> emailList = userIds.stream()
                .map(userId -> userRepository.findById(userId).map(User::getEmail).orElse(null))
                .filter(email -> email != null && !email.isBlank())
                .toList();

        if (emailList.isEmpty()) {
            log.info("[NotificationService][notifyUsersAboutDiscount] No valid email addresses found for product ID: {}", productId);
            return;
        }

        String[] recipients = emailList.toArray(new String[0]);
        String emailContent = String.format("Great news! A product in your wishlist is now discounted by %.2f%%. Check it out now!", discountRate);

        mailService.sendDiscountNotificationMail(recipients, emailContent);
        log.info("[NotificationService][notifyUsersAboutDiscount] Notifications sent for product ID: {}", productId);
    }



    public void notifyUserWithInvoice(Invoice invoice) {
        byte[] pdfContent = invoiceService.generateInvoicePDF(invoice);
        mailService.sendInvoiceMail(invoice.getEmail(), pdfContent, "Invoice_" + invoice.getId() + ".pdf");
    }


}