package pl.szlify.exchangeapi.exchange;

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
import pl.szlify.exchangeapi.service.EmailService;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    @Captor
    private ArgumentCaptor<SimpleMailMessage> messageCaptor;

    @Test
    void testSend_ResultsInEmailBeingCorrect() {

        // given
        String recipientEmail = "test@example.com";

        CurrencyConversionDto.ConversionQuery query = CurrencyConversionDto.ConversionQuery.builder()
                .amount(new BigDecimal("100.00"))
                .from("USD")
                .to("EUR")
                .build();

        CurrencyConversionDto.ConversionInfo info = CurrencyConversionDto.ConversionInfo.builder()
                .rate(new BigDecimal("0.85"))
                .timestamp(1683532800000L)
                .build();

        CurrencyConversionDto convertDto = CurrencyConversionDto.builder()
                .date("2025-05-08")
                .info(info)
                .query(query)
                .result(new BigDecimal("85.00"))
                .success(true)
                .build();

        // when
        emailService.send(recipientEmail, convertDto);

        // then
        verify(mailSender).send(messageCaptor.capture());

        SimpleMailMessage message = messageCaptor.getValue();
        assertEquals("noreply@baeldung.com", message.getFrom());
        assertEquals(recipientEmail, message.getTo()[0]);
        assertEquals("Covert confirmation", message.getSubject());

        String expectedText = "Currency Conversion Confirmation\n\n" +
                "Date: 2025-05-08\n" +
                "Conversion Details:\n" +
                "- Amount: 100.00 USD\n" +
                "- Converted to: 85.00 EUR\n" +
                "- Exchange rate: 0.85\n";

        assertEquals(expectedText, message.getText());
    }
}
