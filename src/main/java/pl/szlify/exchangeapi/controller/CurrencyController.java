package pl.szlify.exchangeapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.szlify.exchangeapi.model.TestModel;
import pl.szlify.exchangeapi.service.CurrencyService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/currencies")
public class CurrencyController {

    public final CurrencyService currencyService;

    @GetMapping("/test")
    public TestModel test() {
        return currencyService.test();
    }
}
