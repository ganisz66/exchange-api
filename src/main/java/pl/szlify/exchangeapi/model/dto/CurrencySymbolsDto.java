package pl.szlify.exchangeapi.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;


@Getter
@Setter
@Builder
public class CurrencySymbolsDto {
    private Map<String, String> symbols;
}


