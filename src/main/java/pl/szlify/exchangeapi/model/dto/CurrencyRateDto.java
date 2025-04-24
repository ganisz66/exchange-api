package pl.szlify.exchangeapi.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class CurrencyRateDto {
    private BigDecimal change;
    @JsonProperty("change_pct")
    private BigDecimal changePct;
    @JsonProperty("end_rate")
    private BigDecimal endRate;
    @JsonProperty("start_rate")
    private BigDecimal startRate;
}
