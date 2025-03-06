package com.team6.ecommerce.invoice;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.team6.ecommerce.cartitem.CartItem2;
import com.team6.ecommerce.user.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@AllArgsConstructor
@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepo;
    private final UserRepository userRepo;

    public Invoice generateInvoice(String orderId, String userId, Double totalAmount, List<CartItem2> items) {

        String email = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found")).getEmail();

        Invoice invoice = Invoice.builder()
                .orderId(orderId)
                .userId(userId)
                .totalAmount(totalAmount)
                .invoiceDate(new Date())
                .email(email)
                .purchasedItems(items)
                .build();

        invoiceRepo.save(invoice);

        return invoice;
    }

    public Optional<Invoice> getInvoiceById(String invoiceId) {
        log.info("[InvoiceService][getInvoiceById] Fetching invoice with ID: {}", invoiceId);
        return invoiceRepo.findById(invoiceId);
    }


    public List<Invoice> getAllInvoices() {
        log.info("[InvoiceService][getAllInvoices] Fetching all invoices");
        return invoiceRepo.findAll();
    }




    public List<Invoice> getInvoicesByUserId(String userId) {
        log.info("[InvoiceService][getInvoicesByUserId] Fetching invoices for user ID: {}", userId);
        return invoiceRepo.findByUserId(userId);
    }


    public List<Invoice> getInvoicesByDateRange(Date startDate, Date endDate) {
        log.info("[InvoiceService][getInvoicesByDateRange] Fetching invoices between {} and {}", startDate, endDate);
        return invoiceRepo.findAll().stream()
                .filter(invoice -> invoice.getInvoiceDate().after(startDate) && invoice.getInvoiceDate().before(endDate))
                .collect(Collectors.toList());
    }




    public byte[] generateInvoicePDF(Invoice invoice) {
        Document document = new Document();
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter.getInstance(document, baos);
            document.open();

            // Add title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph title = new Paragraph("Invoice", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(Chunk.NEWLINE); // Add a blank line

            // Add invoice details
            Font detailsFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
            document.add(new Paragraph("Invoice ID: " + invoice.getId(), detailsFont));
            document.add(new Paragraph("Order ID: " + invoice.getOrderId(), detailsFont));
            document.add(new Paragraph("User Email: " + invoice.getEmail(), detailsFont));
            document.add(new Paragraph("Total Amount: $" + invoice.getTotalAmount(), detailsFont));
            document.add(new Paragraph("Invoice Date: " + invoice.getInvoiceDate(), detailsFont));

            document.add(Chunk.NEWLINE); // Add a blank line

            // Add purchased items header
            Font itemHeaderFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
            Paragraph itemHeader = new Paragraph("Purchased Items:", itemHeaderFont);
            document.add(itemHeader);

            document.add(Chunk.NEWLINE); // Add a blank line

            // Add purchased items table
            PdfPTable table = new PdfPTable(3); // 3 columns: Product Name, Quantity, Price
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            // Add table headers
            Font tableHeaderFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            table.addCell(new PdfPCell(new Phrase("Product Name", tableHeaderFont)));
            table.addCell(new PdfPCell(new Phrase("Quantity", tableHeaderFont)));
            table.addCell(new PdfPCell(new Phrase("Price", tableHeaderFont)));

            // Add items to the table
            Font tableFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
            for (CartItem2 item : invoice.getPurchasedItems()) {
                table.addCell(new PdfPCell(new Phrase(item.getProductName(), tableFont)));
                table.addCell(new PdfPCell(new Phrase(item.getQuantity().toString(), tableFont)));
                table.addCell(new PdfPCell(new Phrase(String.format("$%.2f", item.getPrice()), tableFont)));
            }

            document.add(table); // Add the table to the document

            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            log.error("[InvoiceService][generateInvoicePDF] Error generating PDF: {}", e.getMessage());
            throw new RuntimeException("Failed to generate PDF", e);
        }
    }
}


