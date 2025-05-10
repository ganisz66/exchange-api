package pl.szlify.exchangeapi;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.szlify.exchangeapi.client.ExchangeClient;
import pl.szlify.exchangeapi.model.command.ConvertCommand;
import pl.szlify.exchangeapi.model.command.FluctuationCommand;
import pl.szlify.exchangeapi.model.command.LatestCommand;
import pl.szlify.exchangeapi.model.dto.CurrencyConversionDto;
import pl.szlify.exchangeapi.model.dto.FluctuationDto;
import pl.szlify.exchangeapi.model.dto.LatestRatesDto;
import pl.szlify.exchangeapi.service.CurrencyService;
import pl.szlify.exchangeapi.service.EmailService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CurrencyServiceTest {


    @InjectMocks
    private CurrencyService currencyService;

    @Mock
    private ExchangeClient exchangeClient;

    @Mock
    private EmailService emailService;

    private Faker faker = new Faker();

    @Test
    void convertCurrencyTest_ResultInCurrencyBeingConverted() {
        //given
        ConvertCommand command = buildConvertCommand();
        CurrencyConversionDto dto = buildCurrencyConversionDto();

        when(exchangeClient.currencyConversion(command.getFrom(),
                command.getTo(),
                command.getAmount(),
                command.getDate())).thenReturn(dto);
        //when
        CurrencyConversionDto result = currencyService.convertCurrency(command);

        //than
        verify(exchangeClient).currencyConversion(command.getFrom(),
                command.getTo(),
                command.getAmount(),
                command.getDate());
        verify(emailService).send("jankowalksiii12@gmail.com", dto);

        assertEquals(dto, result);
    }

    @Test
    void getFluctuationTest_WithSymbols_ResultInFluctuationBeingReturned() {
        //given
        FluctuationCommand command = buildFluctuationCommand();
        FluctuationDto dto = buildFluctuationDto();

        String symbolsParam = String.join(",", command.getSymbols());

        when(exchangeClient.fluctuation(
                command.getBase(),
                symbolsParam,
                command.getEndDate(),
                command.getStartDate()))
                .thenReturn(dto);

        //when
        FluctuationDto result = currencyService.getFluctuation(command);

        //than
        verify(exchangeClient).fluctuation(
                command.getBase(),
                symbolsParam,
                command.getEndDate(),
                command.getStartDate());

        assertEquals(dto, result);
    }

//    @Test
//    void getFluctuationTest_WithoutSymbols_ResultInFluctuationBeingReturned() {
//        //given
//        FluctuationCommand command = buildFluctuationCommandWithoutSymbols();
//        FluctuationDto dto = buildFluctuationDto();
//
//        when(exchangeClient.fluctuation(
//                command.getBase(),
//                null,
//                command.getEndDate(),
//                command.getStartDate()))
//                .thenReturn(dto);
//
//        //when
//        FluctuationDto result = currencyService.getFluctuation(command);
//
//        //than
//        verify(exchangeClient).fluctuation(
//                command.getBase(),
//                null,
//                command.getEndDate(),
//                command.getStartDate());
//
//        assertEquals(dto, result);
//    }

    @Test
    void getLatestRatesTest_ResultInLatestRatesBeingReturned() {
        //given
        LatestCommand command = buildLatestCommand();
        LatestRatesDto dto = buildLatestRatesDto();

        String symbolsParam = String.join(",", command.getSymbols());

        when(exchangeClient.latestRates(
                command.getBaseCurrency(),
                symbolsParam))
                .thenReturn(dto);

        //when
        LatestRatesDto result = currencyService.getLatestRates(command);

        //than
        verify(exchangeClient).latestRates(
                command.getBaseCurrency(),
                symbolsParam);

        assertEquals(dto, result);
    }

//    @Test
//    void getSymbolsTest_ResultInSymbolsBeingReturned() {
//        //given
//        SymbolsDto dto =
//    }
//
//    private SymbolsDto buildSymbolsDto() {
//        return new SymbolsDto()
//                .setSymbols(Map.of("PLN", "Polish Zloty"
//                                , "PHP", "Philippine Peso"));
//    }

    private LatestCommand buildLatestCommand() {
        List<String> symbols = Arrays.asList(randomCurrencyCode(), randomCurrencyCode());
        return new LatestCommand()
                .setBaseCurrency(randomCurrencyCode())
                .setSymbols(symbols);
    }

    private LatestRatesDto buildLatestRatesDto() {
        Map<String, BigDecimal> rates = Map.of(randomCurrencyCode(), BigDecimal.valueOf(randomDouble()));
        return LatestRatesDto.builder()
                .base(randomCurrencyCode())
                .date(faker.date().toString())
                .rates(rates)
                .timestamp(faker.random().nextLong())
                .build();
    }

    private FluctuationCommand buildFluctuationCommand() {
        return new FluctuationCommand()
                .setBase(randomCurrencyCode())
                .setSymbols(List.of(randomCurrencyCode(), randomCurrencyCode()))
                .setEndDate(LocalDate.now().plusDays(1))
                .setStartDate(LocalDate.now().minusDays(1));
    }

//    private FluctuationCommand buildFluctuationCommandWithoutSymbols() {
//        return new FluctuationCommand()
//                .setBase(randomCurrencyCode())
//                .setSymbols(null)
//                .setEndDate(LocalDate.now().plusDays(1))
//                .setStartDate(LocalDate.now().minusDays(1));
//    }

    private FluctuationDto buildFluctuationDto() {
        String start_date = faker.date().toString();
        String end_date = faker.date().toString();
        String base = randomCurrencyCode();

        FluctuationDto.FluctuationRate rate = new FluctuationDto.FluctuationRate();
        rate.setStart_rate(BigDecimal.valueOf(randomDouble()));
        rate.setEnd_rate(BigDecimal.valueOf(randomDouble()));
        rate.setChange(BigDecimal.valueOf(randomDouble()));
        rate.setChange_pct(BigDecimal.valueOf(randomDouble()));

        Map<String, FluctuationDto.FluctuationRate> rates = Map.of(randomCurrencyCode(), rate);
        return FluctuationDto.builder()
                .start_date(start_date)
                .end_date(end_date)
                .base(base)
                .rates(rates)
                .success(true)
                .build();
    }

//    private FluctuationDto buildFluctuationDtoWithoutSymbols() {
//        String start_date = faker.date().toString();
//        String end_date = faker.date().toString();
//        String base = randomCurrencyCode();
//
//        FluctuationDto.FluctuationRate rate = new FluctuationDto.FluctuationRate();
//        rate.setStart_rate(BigDecimal.valueOf(randomDouble()));
//        rate.setEnd_rate(BigDecimal.valueOf(randomDouble()));
//        rate.setChange(BigDecimal.valueOf(randomDouble()));
//        rate.setChange_pct(BigDecimal.valueOf(randomDouble()));
//
//        Map<String, FluctuationDto.FluctuationRate> rates = Map.of(randomCurrencyCode(), rate);
//        return FluctuationDto.builder()
//                .start_date(start_date)
//                .end_date(end_date)
//                .base(base)
//                .rates(rates)
//                .success(true)
//                .build();
//    }

    private ConvertCommand buildConvertCommand() {
        return new ConvertCommand()
                .setFrom(randomCurrencyCode())
                .setTo(randomCurrencyCode())
                .setAmount(BigDecimal.valueOf(faker.number().numberBetween(101, 1000)))
                .setDate(LocalDate.now());
    }

    private CurrencyConversionDto buildCurrencyConversionDto() {
        BigDecimal rate = BigDecimal.valueOf(randomDouble());
        BigDecimal amount = BigDecimal.valueOf(faker.number().numberBetween(50, 500));
        String from = randomCurrencyCode();
        String to = randomCurrencyCode();

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

    private String randomCurrencyCode() {
        return faker.currency().code();
    }

    private Double randomDouble() {
        return faker.number().randomDouble(2,1,5);
    }
}
