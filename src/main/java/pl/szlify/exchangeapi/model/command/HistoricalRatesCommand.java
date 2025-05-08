package pl.szlify.exchangeapi.model.command;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import pl.szlify.exchangeapi.validation.SupportedCurrency;

import java.time.LocalDate;
import java.util.List;

@Data
public class HistoricalRatesCommand {

    @NotNull
    @SupportedCurrency
    private List<String> symbols;

    @NotNull
    @SupportedCurrency
    private String base;

    private LocalDate date;

}
