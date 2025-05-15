package pl.szlify.exchangeapi.model.command;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import pl.szlify.exchangeapi.validation.SupportedCurrency;

import java.util.List;

@Data
@Builder
public class LatestRatesCommand {

    @NotNull
    @SupportedCurrency
    private String base;

    @NotNull
    @SupportedCurrency
    private List<String> symbols;

}
