package com.team6.ecommerce.config.populator;

import com.team6.ecommerce.invoice.InvoiceRepository;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@AllArgsConstructor
@Component
public class InvoicePopulator {

    InvoiceRepository invoiceRepository;

    @PostConstruct
    public void init() {

        invoiceRepository.deleteAll();

        log.info("[InvoicePopulator] Cleared invoice collection.");

        // Invoiceler işlem sırasında düşecek db ye, fake gen yok.
    }
}
