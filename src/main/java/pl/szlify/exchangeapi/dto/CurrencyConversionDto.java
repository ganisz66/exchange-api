package pl.szlify.exchangeapi.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Builder
public class CurrencyConversionDto {

        private String date;
        private String historical;
        private ConversionInfo info;
        private ConversionQuery query;
        private BigDecimal result;
        private boolean success;

        @Getter
        @Setter
        public static class ConversionInfo {
            private BigDecimal rate;
            private long timestamp;
        }

        @Setter
        @Getter
        public static class ConversionQuery {
            private BigDecimal amount;
            private String from;
            private String to;
    }
}
