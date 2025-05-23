package pl.szlify.exchangeapi.model.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Builder
public class HistoricalDateRatesDto {
    private String base;
    private String date;
    private boolean historical;
    private Map<String, BigDecimal> rates;
    private boolean success;
    private long timestamp;
}
