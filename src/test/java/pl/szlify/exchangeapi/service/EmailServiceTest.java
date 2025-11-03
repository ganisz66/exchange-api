package pl.szlify.exchangeapi.service;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import pl.szlify.exchangeapi.model.dto.CurrencyConversionDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @InjectMocks
    private EmailService emailService;

    @Mock
    private JavaMailSender mailSender;

    @Captor
    private ArgumentCaptor<SimpleMailMessage> messageCaptor;

    private Faker faker = new Faker();

    private static final String FROM = "noreply@baeldung.com";

    @Test
    void shouldSendEmailWithCorrectContent() {
        // given
        String to = "test@example.com";
        CurrencyConversionDto dto = buildSampleDto();

        // when
        emailService.send(to, dto);

        // then
        verify(mailSender, times(1)).send(messageCaptor.capture());

        SimpleMailMessage sentMessage = messageCaptor.getValue();
        assertEquals(FROM, sentMessage.getFrom());
        assertArrayEquals(new String[]{to}, sentMessage.getTo());
        assertEquals("Convert confirmation", sentMessage.getSubject());
        assertTrue(Objects.requireNonNull(sentMessage.getText()).contains(dto.getQuery().getFrom()));
        assertTrue(Objects.requireNonNull(sentMessage.getText()).contains(dto.getQuery().getTo()));
        assertTrue(Objects.requireNonNull(sentMessage.getText()).contains(dto.getResult().toString()));
    }

    private CurrencyConversionDto buildSampleDto() {
        BigDecimal rate = BigDecimal.valueOf(faker.number().randomDouble(2, 1, 5));
        BigDecimal amount = BigDecimal.valueOf(faker.number().numberBetween(50, 500));
        String from = faker.currency().code();
        String to = faker.currency().code();

        CurrencyConversionDto.ConversionInfo info = new CurrencyConversionDto.ConversionInfo();
        info.setRate(rate);
        info.setTimestamp(System.currentTimeMillis());

        CurrencyConversionDto.ConversionQuery query = new CurrencyConversionDto.ConversionQuery();
        query.setAmount(amount);
        query.setFrom(from);
        query.setTo(to);

        return CurrencyConversionDto.builder()
                .date(LocalDate.now().toString())
                .historical("false")
                .info(info)
                .query(query)
                .result(rate.multiply(amount))
                .success(true)
                .build();
    }
}
