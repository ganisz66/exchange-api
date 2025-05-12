package pl.szlify.exchangeapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pl.szlify.exchangeapi.model.dto.CurrencyConversionDto;

import java.text.MessageFormat;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private static final String SUBJECT = "Covert confirmation";
    private final JavaMailSender mailSender;

    @Async("asyncTaskExecutor")
    public void send(String to, CurrencyConversionDto convertDto) {
        log.info("Sending email...");
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {}
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@baeldung.com");
        message.setTo(to);
        message.setSubject(SUBJECT);
        message.setText(convertMessage(convertDto));
        mailSender.send(message);
        log.info("Email sent.");
    }

    private String convertMessage(CurrencyConversionDto convertDto) {

        return MessageFormat.format("""
                Currency Conversion Confirmation
                Date: {0}
                Conversion Details:
                - Amount: {1} {2}
                - Converted to: {3} {4}
                - Exchange rate: {5}
                
                """,
                convertDto.getDate(),
                convertDto.getQuery().getAmount(),
                convertDto.getQuery().getFrom(),
                convertDto.getResult(),
                convertDto.getQuery().getTo(),
                convertDto.getInfo().getRate()
                );
    }
}
