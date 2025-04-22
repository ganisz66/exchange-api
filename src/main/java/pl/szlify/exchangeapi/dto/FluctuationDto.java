package pl.szlify.exchangeapi.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Builder
public class FluctuationDto {
    private String start_date;
    private String end_date;
    private String base;
    private Map<String, FluctuationRate> rates;
    private boolean success;

    @Getter
    @Setter
    public static class FluctuationRate {
        private BigDecimal start_rate;
        private BigDecimal end_rate;
        private BigDecimal change;
        private BigDecimal change_pct;

    }
}
