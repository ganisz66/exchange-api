package pl.szlify.exchangeapi.model.command;

import lombok.Data;
import pl.szlify.exchangeapi.validation.SupportedCurrency;

import java.util.List;

@Data
public class LatestCommand {

    @SupportedCurrency
    private String baseCurrency;

    @SupportedCurrency
    private List<String> symbols;
}
