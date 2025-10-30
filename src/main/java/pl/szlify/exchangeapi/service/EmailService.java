package pl.szlify.exchangeapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pl.szlify.exchangeapi.model.dto.CurrencyConversionDto;

import java.math.RoundingMode;
import java.text.MessageFormat;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

    private static final String SUBJECT = "Convert confirmation";

    private final JavaMailSender mailSender;

    @Async("asyncTaskExecutor")
    public void send(String to, CurrencyConversionDto convertDto) {
        log.info("sending...");
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {}
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@baeldung.com");
        message.setTo(to);
        message.setSubject(SUBJECT);
        message.setText(buildConversionEmailMessage(convertDto));
        mailSender.send(message);
        log.info("sent");
    }

    private String buildConversionEmailMessage(CurrencyConversionDto convertDto) {
        return MessageFormat.format("""
                Hello,
                
                There was exchange
                    from: {0}
                    to: {1}
                    amount: {2}
                
                Exchanging with rate of {3} you got {4}
                """,
                convertDto.getQuery().getFrom(),
                convertDto.getQuery().getTo(),
                convertDto.getQuery().getAmount(),
                convertDto.getInfo().getRate(),
                convertDto.getResult().setScale(2, RoundingMode.HALF_UP).toPlainString());
//        StringBuilder sb = new StringBuilder();
//        sb.append("Hello, \n\n")
//                .append("There was exchange from: ").append(convertDto.getQuery().getFrom())
//                .append(" to ").append(convertDto.getQuery().getTo())
//                .append(", in the amount of: ").append(convertDto.getQuery().getAmount()).append("\n\n")
//                .append("The exchange rate was: ").append(convertDto.getInfo().getRate())
//                .append(", but result is: ").append(convertDto.getResult().setScale(2, RoundingMode.HALF_UP).toPlainString());
//        return sb.toString();
    }
}
