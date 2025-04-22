package pl.szlify.exchangeapi.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;


@Getter
@Builder
public class CurrencySymbolsDto {
    private Map<String, String> symbols;
}


