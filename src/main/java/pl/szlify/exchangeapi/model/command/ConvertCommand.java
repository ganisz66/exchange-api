package pl.szlify.exchangeapi.model.command;

import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;
import pl.szlify.exchangeapi.validation.SupportedCurrency;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Accessors(chain = true)
public class ConvertCommand {

    @SupportedCurrency
    private String from;

    @SupportedCurrency
    private String to;

    @Min(100)
    private BigDecimal amount;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;
}
