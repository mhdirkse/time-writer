package com.github.mhdirkse.timewriter;

import java.time.Instant;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class InstantConverter implements Converter<String, Instant> {
    @Override
    public Instant convert(String inputSeconds) {
        long millis = 1000 * Long.valueOf(inputSeconds).longValue();
        return Instant.ofEpochMilli(millis);
    }
}
