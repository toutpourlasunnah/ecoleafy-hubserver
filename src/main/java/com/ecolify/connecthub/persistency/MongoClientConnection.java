package com.ecolify.connecthub.persistency;

import com.ecolify.connecthub.model.SensorReadingFactory;
import com.ecolify.connecthub.model.SensorReadingRecord;
import com.mongodb.client.model.Filters;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class MongoClientConnection {

    private final String uri = "mongodb+srv://app:skrrbrryesyesyes@cluster0.vk6rilj.mongodb.net/?retryWrites=true&w=majority";
    private MongoClient mongoClient = MongoClients.create(this.uri);
    private MongoDatabase database = this.mongoClient.getDatabase("smartify");
    private  MongoCollection<Document> collection;

    public MongoClientConnection(){
        //this.collection = database.getCollection("module_data");

    }

    public boolean testConfig(){
        Random r = new Random();
        double randomNumber = r.nextDouble();

        JSONObject jsonObj = new JSONObject();
        jsonObj.put("macAddress", "HUB_TEST");
        jsonObj.put("temperature", randomNumber);

        SensorReadingRecord sensorReadingRecord = SensorReadingFactory.createSensorReadingRecord(jsonObj);
        this.insertReading("HUB_TEST", sensorReadingRecord);

        Bson filter = Filters.eq("temperature", randomNumber);
        SensorReadingRecord retrievedSRR = this.findReading("HUB_TEST", filter);

        System.out.println("INITIAL RECORD " + sensorReadingRecord);
        System.out.println("FINAL RECORD " + retrievedSRR);

        return sensorReadingRecord.equals(retrievedSRR);
    }

    public void insertReading(String collectionName, SensorReadingRecord sensorReadingRecord){
        MongoCollection<SensorReadingRecord> collection = database.getCollection(collectionName, SensorReadingRecord.class);

        // insert the record
        collection.insertOne(sensorReadingRecord);
    }

    public void insertReadings(String collectionName, List<SensorReadingRecord> sensorReadingRecord){
        MongoCollection<SensorReadingRecord> collection = database.getCollection(collectionName, SensorReadingRecord.class);

        // insert the records
        collection.insertMany(sensorReadingRecord);
    }

    public List<SensorReadingRecord> findReadings(String collectionName){
        MongoCollection<SensorReadingRecord> collection = database.getCollection(collectionName, SensorReadingRecord.class);

        // retrieve and print the records
        List<SensorReadingRecord> sensorReadingRecordList = new ArrayList<SensorReadingRecord>();
        collection.find().into(sensorReadingRecordList);

        sensorReadingRecordList.forEach(System.out::println);

        return sensorReadingRecordList;
    }

    public SensorReadingRecord findReading(String collectionName, Bson filter){
        MongoCollection<SensorReadingRecord> collection = database.getCollection(collectionName, SensorReadingRecord.class);

        // retrieve the records
        List<SensorReadingRecord> sensorReadingRecordList = new ArrayList<SensorReadingRecord>();

        collection.find(filter).limit(1).into(sensorReadingRecordList);

        return sensorReadingRecordList.isEmpty() ? null : sensorReadingRecordList.get(0);
    }

}