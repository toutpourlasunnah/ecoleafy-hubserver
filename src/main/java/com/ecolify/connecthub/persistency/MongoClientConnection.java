package com.ecolify.connecthub.persistency;

import com.ecolify.connecthub.Node.NodeReading.controller.NodeReadingFactory;
import com.ecolify.connecthub.Node.NodeReading.model.NodeReadingRecord;
import com.mongodb.client.model.Filters;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import io.github.cdimascio.dotenv.Dotenv;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.mongodb.client.model.Sorts.descending;

public class MongoClientConnection {
    Dotenv dotenv = Dotenv.load();
    private final String uri = dotenv.get("MONGODB_URI");
    private MongoClient mongoClient = MongoClients.create(this.uri);
    private MongoDatabase database = this.mongoClient.getDatabase("smartify");
    private  MongoCollection<Document> collection;

    public MongoClientConnection(){
        //this.collection = database.getCollection("module_data");

    }

    /**
     * This method is used to test the connection to the database
     * @return
     */
    public boolean testConfig(){
        Random r = new Random();
        double randomNumber = r.nextDouble();

        JSONObject jsonObj = new JSONObject();
        jsonObj.put("macAddress", "HUB_TEST");
        jsonObj.put("temperature", randomNumber);

        NodeReadingRecord nodeReadingRecord = NodeReadingFactory.createSensorReadingRecord(jsonObj);
        this.insertReading("HUB_TEST", nodeReadingRecord);

        Bson filter = Filters.eq("temperature", randomNumber);
        NodeReadingRecord retrievedSRR = this.findReading("HUB_TEST", filter);

        System.out.println("INITIAL RECORD " + nodeReadingRecord);
        System.out.println("FINAL RECORD " + retrievedSRR);

        return nodeReadingRecord.equals(retrievedSRR);
    }

    /**
     * This method is used to insert a single reading into the database
     * @param collectionName
     * @param nodeReadingRecord
     */
    public void insertReading(String collectionName, NodeReadingRecord nodeReadingRecord){
        MongoCollection<NodeReadingRecord> collection = database.getCollection(collectionName, NodeReadingRecord.class);

        // insert the record
        collection.insertOne(nodeReadingRecord);
    }

    /**
     * This method is used to insert a list of readings into the database
     * @param collectionName
     * @param nodeReadingRecord
     */
    public void insertReadings(String collectionName, List<NodeReadingRecord> nodeReadingRecord){
        MongoCollection<NodeReadingRecord> collection = database.getCollection(collectionName, NodeReadingRecord.class);

        // insert the records
        collection.insertMany(nodeReadingRecord);
    }

    /**
     * This method is used to retrieve all the readings from a collection
     * @param collectionName
     * @return
     */
    public List<NodeReadingRecord> findReadings(String collectionName){
        MongoCollection<NodeReadingRecord> collection = database.getCollection(collectionName, NodeReadingRecord.class);

        // retrieve and print the records
        List<NodeReadingRecord> nodeReadingRecordList = new ArrayList<NodeReadingRecord>();
        collection.find().into(nodeReadingRecordList);

        //sensorReadingRecordList.forEach(System.out::println);

        return nodeReadingRecordList;
    }

    public List<NodeReadingRecord> findLast100Readings(String collectionName){
        MongoCollection<NodeReadingRecord> collection = database.getCollection(collectionName, NodeReadingRecord.class);

        // retrieve and print the records
        List<NodeReadingRecord> nodeReadingRecordList = new ArrayList<NodeReadingRecord>();
        collection.find().sort(descending("timeTaken")).limit(100).into(nodeReadingRecordList);

        //sensorReadingRecordList.forEach(System.out::println);

        return nodeReadingRecordList;
    }

    /**
     * This method is used to retrieve all the readings from a collection
     * @param collectionName
     * @return
     */
    public NodeReadingRecord findReading(String collectionName, Bson filter){
        MongoCollection<NodeReadingRecord> collection = database.getCollection(collectionName, NodeReadingRecord.class);

        // retrieve the records
        List<NodeReadingRecord> nodeReadingRecordList = new ArrayList<NodeReadingRecord>();

        collection.find(filter).limit(1).into(nodeReadingRecordList);

        return nodeReadingRecordList.isEmpty() ? null : nodeReadingRecordList.get(0);
    }

}