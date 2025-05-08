package pl.szlify.exchangeapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import pl.szlify.exchangeapi.model.dto.CurrencyConversionDto;

@Service
@RequiredArgsConstructor
public class EmailService {

    private static final String SUBJECT = "Covert confirmation";
    private final JavaMailSender mailSender;

    public void send(String to, CurrencyConversionDto convertDto) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("noreply@baeldung.com");
        message.setTo(to);
        message.setSubject(SUBJECT);
        message.setText(convertMessage(convertDto));
        mailSender.send(message);
    }

    private String convertMessage(CurrencyConversionDto convertDto) {
        StringBuilder message = new StringBuilder();

        message.append("Currency Conversion Confirmation\n\n");
        message.append("Date: ").append(convertDto.getDate()).append("\n");
        message.append("Conversion Details:\n");
        message.append("- Amount: ").append(convertDto.getQuery().getAmount())
                .append(" ").append(convertDto.getQuery().getFrom()).append("\n");
        message.append("- Converted to: ").append(convertDto.getResult())
                .append(" ").append(convertDto.getQuery().getTo()).append("\n");
        message.append("- Exchange rate: ").append(convertDto.getInfo().getRate()).append("\n");

        return message.toString();
    }
}
