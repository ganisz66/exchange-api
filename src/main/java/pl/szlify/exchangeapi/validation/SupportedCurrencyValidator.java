package pl.szlify.exchangeapi.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import pl.szlify.exchangeapi.model.dto.CurrencySymbolsDto;
import pl.szlify.exchangeapi.service.CurrencyService;

@RequiredArgsConstructor
public class SupportedCurrencyValidator implements ConstraintValidator<SupportedCurrency, String> {

    private final CurrencyService currencyService;

    @Override
    public boolean isValid(String currencySymbol, ConstraintValidatorContext constraintValidatorContext) {
        if (currencySymbol == null || currencySymbol.isBlank()) {
            return true;
        }

        CurrencySymbolsDto symbolsDto = currencyService.getAllCurrencySymbols();
        return symbolsDto != null
                && symbolsDto.getSymbols() != null
                && symbolsDto.getSymbols().containsKey(currencySymbol);
    }
}
