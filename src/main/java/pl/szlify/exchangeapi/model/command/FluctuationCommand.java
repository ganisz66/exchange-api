package pl.szlify.exchangeapi.model.command;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import pl.szlify.exchangeapi.validation.SupportedCurrency;

import java.time.LocalDate;
import java.util.List;

@Data
public class FluctuationCommand {

    @NotNull
    @SupportedCurrency
    private String base;

    @NotNull
    @SupportedCurrency
    private List<String> symbols;

    private LocalDate start_date;

    private LocalDate end_date;



}
