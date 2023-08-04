package com.hang.stackask.converter;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
public class LocalDateTimeToTypeConverter {
    public long convertLocalDateTimeToLong(LocalDateTime dateTime) {
        Instant instant = dateTime.toInstant(ZoneOffset.UTC);
        return instant.toEpochMilli();
    }
}
