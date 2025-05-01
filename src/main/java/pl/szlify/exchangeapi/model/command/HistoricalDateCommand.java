package pl.szlify.exchangeapi.model.command;

import lombok.Data;
import pl.szlify.exchangeapi.validation.SupportedCurrency;

import java.time.LocalDate;
import java.util.List;

@Data
public class HistoricalDateCommand {

    @SupportedCurrency
    private String baseCurrency;

    @SupportedCurrency
    private List<String> symbols;

    private LocalDate date;

}
