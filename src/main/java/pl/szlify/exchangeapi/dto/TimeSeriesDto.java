package pl.szlify.exchangeapi.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Builder
public class TimeSeriesDto {

        private String base;
        private String start_date;
        private String end_date;
        private boolean success;
        private boolean timeseries;
        private Map<String, Map<String, BigDecimal>> rates;
}