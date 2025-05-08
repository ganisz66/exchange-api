package pl.szlify.exchangeapi.exchange;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.szlify.exchangeapi.client.ExchangeClient;
import pl.szlify.exchangeapi.model.command.ConvertCommand;
import pl.szlify.exchangeapi.model.dto.CurrencyConversionDto;
import pl.szlify.exchangeapi.model.dto.CurrencySymbolsDto;
import pl.szlify.exchangeapi.service.CurrencyService;
import pl.szlify.exchangeapi.service.EmailService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CurrencyServiceTest {


    @InjectMocks
    private CurrencyService currencyService;

    @Mock
    private ExchangeClient exchangeClient;

    @Captor
    private ArgumentCaptor<String> emailCaptor;

    @Captor
    private ArgumentCaptor<CurrencyConversionDto> conversionDtoCaptor;

    @Mock
    private EmailService emailService;

    @Test
    void testGetAllCurrencySymbols_ResultsInSymbolsBeingRetrieved() {

        // given
        CurrencySymbolsDto expectedSymbols = CurrencySymbolsDto.builder().build();
        expectedSymbols.setSymbols(Map.of(
                "USD", "US Dollar",
                "EUR", "Euro",
                "GBP", "British Pound"
        ));

        when(exchangeClient.getCurrencySymbols()).thenReturn(expectedSymbols);

        // when
        CurrencySymbolsDto result = currencyService.getAllCurrencySymbols();
        CurrencySymbolsDto cachedResult = currencyService.getAllCurrencySymbols();

        // tthen
        assertEquals(expectedSymbols, result);
        verify(exchangeClient, times(2)).getCurrencySymbols();
        assertEquals(expectedSymbols, cachedResult);
        verify(exchangeClient, times(2)).getCurrencySymbols();
    }

    @Test
    void testConvertCurrency_ReturnsCurrencyConversionFromClientAndSendsEmail() {
        // given
        ConvertCommand command = ConvertCommand.builder()
                .from("USD")
                .to("EUR")
                .amount(new BigDecimal("100.00"))
                .date(LocalDate.parse("2025-05-08"))
                .build();

        CurrencyConversionDto.ConversionQuery query = CurrencyConversionDto.ConversionQuery.builder()
                .amount(new BigDecimal("100.00"))
                .from("USD")
                .to("EUR")
                .build();

        CurrencyConversionDto.ConversionInfo info = CurrencyConversionDto.ConversionInfo.builder()
                .rate(new BigDecimal("0.85"))
                .timestamp(1683532800000L)
                .build();

        CurrencyConversionDto expectedConversion = CurrencyConversionDto.builder()
                .date("2025-05-08")
                .info(info)
                .query(query)
                .result(new BigDecimal("85.00"))
                .success(true)
                .build();

        when(exchangeClient.getCurrencyConversion(
                command.getFrom(),
                command.getTo(),
                command.getAmount(),
                command.getDate()
        )).thenReturn(expectedConversion);

        doNothing().when(emailService).send(anyString(), any(CurrencyConversionDto.class));

        // when
        CurrencyConversionDto result = currencyService.convertCurrency(command);

        // then
        assertEquals(expectedConversion, result);
        verify(exchangeClient).getCurrencyConversion(
                command.getFrom(),
                command.getTo(),
                command.getAmount(),
                command.getDate()
        );
        verify(emailService).send(emailCaptor.capture(), conversionDtoCaptor.capture());
        assertEquals("jankowalksiii12@gmail.com", emailCaptor.getValue());
        assertEquals(expectedConversion, conversionDtoCaptor.getValue());
    }
}

