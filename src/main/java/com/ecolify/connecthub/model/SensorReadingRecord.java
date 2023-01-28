package com.ecolify.connecthub.model;

import java.time.LocalDateTime;

public record SensorReadingRecord(
        String macAddress,
        String room,
        Double temperature,
        Double humidity,
        Boolean presence,
        LocalDateTime timeTaken
) {}

