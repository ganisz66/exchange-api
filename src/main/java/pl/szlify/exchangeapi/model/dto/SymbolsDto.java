package pl.szlify.exchangeapi.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class SymbolsDto {
    private Map<String, String> symbols;
}
