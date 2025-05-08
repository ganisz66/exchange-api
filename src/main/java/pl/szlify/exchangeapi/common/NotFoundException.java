package pl.szlify.exchangeapi.common;

import java.text.MessageFormat;

public class NotFoundException extends RuntimeException {

    private final Class<?> clazz;

    public NotFoundException(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    public String getMessage() {
        return MessageFormat.format("", clazz.getSimpleName());
    }



}

