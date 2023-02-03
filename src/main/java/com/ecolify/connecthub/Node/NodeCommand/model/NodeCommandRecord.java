package com.ecolify.connecthub.Node.NodeCommand.model;

public record NodeCommandRecord(
        String macAddress,
        String room,
        String switchId,
        Double state
) {
}
