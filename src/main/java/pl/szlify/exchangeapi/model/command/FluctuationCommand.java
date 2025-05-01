package pl.szlify.exchangeapi.model.command;

import lombok.Data;
import pl.szlify.exchangeapi.validation.SupportedCurrency;

import java.time.LocalDate;
import java.util.List;

@Data
public class FluctuationCommand {

    @SupportedCurrency
    private String base;

    @SupportedCurrency
    private List<String> symbols;

    private LocalDate endDate;

    private LocalDate startDate;
}
