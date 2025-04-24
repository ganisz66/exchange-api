package pl.szlify.exchangeapi.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class TimeSeriesRatesResponseDto {
    private Map<String, Map<String, Double>> rates;
}
