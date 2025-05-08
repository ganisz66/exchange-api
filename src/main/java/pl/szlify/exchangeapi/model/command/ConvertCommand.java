package pl.szlify.exchangeapi.model.command;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import pl.szlify.exchangeapi.validation.SupportedCurrency;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class ConvertCommand {

    @NotNull
    @SupportedCurrency
    private String from;

    @NotNull
    @SupportedCurrency
    private String to;

    @NotNull
    @Min(100)
    private BigDecimal amount;

    private LocalDate date;
}
