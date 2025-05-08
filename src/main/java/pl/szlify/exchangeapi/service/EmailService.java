package pl.szlify.exchangeapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import pl.szlify.exchangeapi.model.dto.CurrencyConversionDto;

@Service
@RequiredArgsConstructor
public class EmailService {

    private static final String SUBJECT = "Convert confirmation";

    private final JavaMailSender mailSender;

    public void send(String to, CurrencyConversionDto convertDto) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@baeldung.com");
        message.setTo(to);
        message.setSubject(SUBJECT);
        message.setText("DUPA");
        mailSender.send(message);
    }
}
