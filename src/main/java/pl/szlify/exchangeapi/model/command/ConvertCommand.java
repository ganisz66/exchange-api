package pl.szlify.exchangeapi.model.command;

import jakarta.validation.constraints.Min;
import lombok.Data;
import pl.szlify.exchangeapi.validation.SupportedCurrency;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ConvertCommand {

    @SupportedCurrency
    private String from;

    @SupportedCurrency
    private String to;

    @Min(100)
    private BigDecimal amount;

    private LocalDate date;
}
