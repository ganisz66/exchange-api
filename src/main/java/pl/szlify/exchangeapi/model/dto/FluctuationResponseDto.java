package pl.szlify.exchangeapi.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class FluctuationResponseDto {
    private Map<String, CurrencyRateDto> rates;
}
