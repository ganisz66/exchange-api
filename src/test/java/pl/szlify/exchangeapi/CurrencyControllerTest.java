package pl.szlify.exchangeapi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.szlify.exchangeapi.service.CurrencyService;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CurrencyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CurrencyService currencyService;

    @Test
    void testConvertCurrency_ResultInConvertedCurrencyBeingReturned() throws Exception {
        // given
        String from = "USD";
        String to = "PLN";
        BigDecimal amount = new BigDecimal("100.00");
        LocalDate date = LocalDate.of(2025, 4, 22);
        BigDecimal expectedResult = new BigDecimal("92.55");

        // when + then
        mockMvc.perform(MockMvcRequestBuilders.get("/convert")
                        .param("from", from)
                        .param("to", to)
                        .param("amount", amount.toString())
                        .param("date", date.toString())
                )
                .andExpect(status().isOk())
                .andExpect(content().string("92.55"));
    }
}
