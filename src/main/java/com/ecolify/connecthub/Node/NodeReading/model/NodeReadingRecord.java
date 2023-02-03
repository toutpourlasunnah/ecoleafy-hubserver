package com.ecolify.connecthub.Node.NodeReading.model;

import java.time.LocalDateTime;

public record NodeReadingRecord(
        String macAddress,
        String room,
        Double temperature,
        Double humidity,
        Boolean presence,
        LocalDateTime timeTaken
) {}

