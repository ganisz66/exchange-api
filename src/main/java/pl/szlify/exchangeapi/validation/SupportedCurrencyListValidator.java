package pl.szlify.exchangeapi.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import pl.szlify.exchangeapi.model.dto.CurrencySymbolsDto;
import pl.szlify.exchangeapi.service.CurrencyService;

import java.util.List;

@RequiredArgsConstructor
public class SupportedCurrencyListValidator implements ConstraintValidator<SupportedCurrency, List<String>> {

    private final CurrencyService currencyService;

    @Override
    public boolean isValid(List<String> currencySymbols, ConstraintValidatorContext constraintValidatorContext) {
        if (currencySymbols == null || currencySymbols.isEmpty()) {
            return true;
        }

        CurrencySymbolsDto symbolsDto = currencyService.getAllCurrencySymbols();
        return symbolsDto != null
                && symbolsDto.getSymbols() != null
                && currencySymbols.stream().allMatch(symbolsDto.getSymbols()::containsKey);
    }
}
