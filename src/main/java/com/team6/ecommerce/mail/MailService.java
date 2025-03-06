package com.team6.ecommerce.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Log4j2
@AllArgsConstructor
@Service
public class MailService {

    private JavaMailSender mailSender;

    public void sendInvoiceMail(String recipient, byte[] pdfContent, String fileName) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(recipient);
            helper.setSubject("Your Invoice");
            helper.setText("Please find your invoice attached.");
            helper.addAttachment(fileName, new ByteArrayDataSource(pdfContent, "application/pdf"));

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }


    public void sendDiscountNotificationMail(String[] recipients, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(recipients);
            helper.setSubject("Exciting Discount on Your Wishlist Item!");
            helper.setText(content, true); // Enable HTML content if needed

            mailSender.send(message);
            log.info("[MailService][sendDiscountNotificationMail] Notification sent to {} recipients.", recipients.length);
        } catch (MessagingException e) {
            log.error("[MailService][sendDiscountNotificationMail] Failed to send notification email.", e);
            throw new RuntimeException("Failed to send discount notification email", e);
        }
    }


    public void sendRefundApprovalMail(String recipient, String orderId) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(recipient);
            helper.setSubject("Refund Approved");
            helper.setText(String.format(
                    "Dear Customer,\n\nYour refund request for order ID %s has been approved and processed successfully. "
                            + "You should receive the refund shortly.\n\nThank you for shopping with us!\n\nBest regards,\nTeam6 eCommerce.",
                    orderId));

            mailSender.send(message);
            log.info("[MailService][sendRefundApprovalMail] Refund approval email sent to {}", recipient);
        } catch (MessagingException e) {
            log.error("[MailService][sendRefundApprovalMail] Failed to send refund approval email to {}", recipient, e);
            throw new RuntimeException("Failed to send refund approval email", e);
        }
    }





}
