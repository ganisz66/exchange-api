package pl.szlify.exchangeapi.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Builder
public class LatestRatesDto {
    private String base;
    private String date;
    private Map<String, BigDecimal> rates;
    private boolean success;
    private long timestamp;

}
