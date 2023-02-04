package com.ecolify.connecthub.Hub.model;

import com.ecolify.connecthub.Node.NodeConfigRecord;

public record HubConfigRecord(
        String name,
        String location,
        String owner,
        NodeConfigRecord[] nodeConfigRecordList
) {
}
