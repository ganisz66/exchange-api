package pl.szlify.exchangeapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import pl.szlify.exchangeapi.model.dto.CurrencyConversionDto;

import java.math.RoundingMode;

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
        message.setText(buildConversionEmailMessage(convertDto));
        mailSender.send(message);
    }

    private String buildConversionEmailMessage(CurrencyConversionDto convertDto) {
        StringBuilder sb = new StringBuilder();
        sb.append("Hello, \n\n")
                .append("There was exchange from: ").append(convertDto.getQuery().getFrom())
                .append(" to ").append(convertDto.getQuery().getTo())
                .append(", in the amount of: ").append(convertDto.getQuery().getAmount()).append("\n\n")
                .append("The exchange rate was: ").append(convertDto.getInfo().getRate())
                .append(", but result is: ").append(convertDto.getResult().setScale(2, RoundingMode.HALF_UP).toPlainString());
        return sb.toString();
    }
}
