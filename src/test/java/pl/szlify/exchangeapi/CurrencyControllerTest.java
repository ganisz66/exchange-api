package pl.szlify.exchangeapi;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.szlify.exchangeapi.client.ExchangeClient;
import pl.szlify.exchangeapi.model.dto.CurrencyConversionDto;
import pl.szlify.exchangeapi.model.dto.SymbolsDto;
import pl.szlify.exchangeapi.service.CurrencyService;
import pl.szlify.exchangeapi.service.EmailService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CurrencyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CurrencyService currencyService;

    @Mock
    private EmailService emailService;

    @Mock
    private ExchangeClient exchangeClient;

    private Faker faker = new Faker();

    @BeforeEach
    void seUp() {
        doNothing().when(emailService).send(anyString(),any());
    }

    @Test
    void testConvertCurrency_ResultInConvertedCurrencyBeingReturned() throws Exception {
        // given
        String from = faker.currency().code();
        String to = faker.currency().code();
        BigDecimal amount = new BigDecimal(faker.number().numberBetween(101, 1000));
        LocalDate date = LocalDate.now();

        CurrencyConversionDto.ConversionInfo info = new CurrencyConversionDto.ConversionInfo();
        info.setRate(new BigDecimal("3.75"));
        info.setTimestamp(System.currentTimeMillis());

        CurrencyConversionDto.ConversionQuery query = new CurrencyConversionDto.ConversionQuery();
        query.setAmount(amount);
        query.setFrom(from);
        query.setTo(to);

        CurrencyConversionDto dto = CurrencyConversionDto.builder()
                .date(date.toString())
                .historical("false")
                .info(info)
                .query(query)
                .result(amount.multiply(info.getRate()))
                .success(true)
                .build();

        when(exchangeClient.currencyConversion(from, to, amount, date)).thenReturn(dto);

        mockMvc.perform(get("/api/v1/currencies/convert")
                .param("from", from)
                .param("to", to)
                .param("amount", amount.toString())
                .param("date", date.toString())
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.date").value(date.toString()))
                .andExpect(jsonPath("$.historical").value("false"))
                .andExpect(jsonPath("$.info.rate").value(3.75))
                .andExpect(jsonPath("$.query.amount").value(amount.toString()))
                .andExpect(jsonPath("$.result").value(dto.getResult().toString()))
                .andExpect(jsonPath("$.success").value(true));
    }
}
