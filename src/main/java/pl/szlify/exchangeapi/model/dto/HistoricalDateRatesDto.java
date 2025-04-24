package pl.szlify.exchangeapi.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class HistoricalDateRatesDto {
    private Map<String, Double> rates;
}
