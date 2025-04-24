package pl.szlify.exchangeapi.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class SymbolsDto {
    private Map<String, String> symbols;
}
