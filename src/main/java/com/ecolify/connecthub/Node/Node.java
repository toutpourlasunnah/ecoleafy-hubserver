package com.ecolify.connecthub.Node;

import com.ecolify.connecthub.Node.NodeReading.model.NodeReadingRecord;
import com.ecolify.connecthub.persistency.MongoClientConnection;
import org.bson.conversions.Bson;

import java.util.List;

public class Node {
    private NodeConfigRecord nodeConfigRecord;


    public Node(String macAddress) {
        this.nodeConfigRecord = new NodeConfigRecord(macAddress, null);
    }

    private List<NodeReadingRecord> getAllReadingsFromDb(){
        MongoClientConnection mongoClientConnection = new MongoClientConnection();
        return mongoClientConnection.findReadings(this.nodeConfigRecord.macAddress());
    }
    private List<NodeReadingRecord> getReadingsFromDb(int numberOfReadings){
        MongoClientConnection mongoClientConnection = new MongoClientConnection();
        return mongoClientConnection.findReadings(this.nodeConfigRecord.macAddress(), numberOfReadings);
    }
    private NodeReadingRecord getReadingFromDb(Bson filter){
        MongoClientConnection mongoClientConnection = new MongoClientConnection();
        return mongoClientConnection.findReading(this.nodeConfigRecord.macAddress(), filter);
    }


}
