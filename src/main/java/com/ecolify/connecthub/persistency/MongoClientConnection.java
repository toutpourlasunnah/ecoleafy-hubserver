package com.ecolify.connecthub.persistency;

import com.ecolify.connecthub.model.SensorReadingFactory;
import com.ecolify.connecthub.model.SensorReadingRecord;
import com.mongodb.client.model.Filters;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import io.github.cdimascio.dotenv.Dotenv;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
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

        SensorReadingRecord sensorReadingRecord = SensorReadingFactory.createSensorReadingRecord(jsonObj);
        this.insertReading("HUB_TEST", sensorReadingRecord);

        Bson filter = Filters.eq("temperature", randomNumber);
        SensorReadingRecord retrievedSRR = this.findReading("HUB_TEST", filter);

        System.out.println("INITIAL RECORD " + sensorReadingRecord);
        System.out.println("FINAL RECORD " + retrievedSRR);

        return sensorReadingRecord.equals(retrievedSRR);
    }

    /**
     * This method is used to insert a single reading into the database
     * @param collectionName
     * @param sensorReadingRecord
     */
    public void insertReading(String collectionName, SensorReadingRecord sensorReadingRecord){
        MongoCollection<SensorReadingRecord> collection = database.getCollection(collectionName, SensorReadingRecord.class);

        // insert the record
        collection.insertOne(sensorReadingRecord);
    }

    /**
     * This method is used to insert a list of readings into the database
     * @param collectionName
     * @param sensorReadingRecord
     */
    public void insertReadings(String collectionName, List<SensorReadingRecord> sensorReadingRecord){
        MongoCollection<SensorReadingRecord> collection = database.getCollection(collectionName, SensorReadingRecord.class);

        // insert the records
        collection.insertMany(sensorReadingRecord);
    }

    /**
     * This method is used to retrieve all the readings from a collection
     * @param collectionName
     * @return
     */
    public List<SensorReadingRecord> findReadings(String collectionName){
        MongoCollection<SensorReadingRecord> collection = database.getCollection(collectionName, SensorReadingRecord.class);

        // retrieve and print the records
        List<SensorReadingRecord> sensorReadingRecordList = new ArrayList<SensorReadingRecord>();
        collection.find().into(sensorReadingRecordList);

        //sensorReadingRecordList.forEach(System.out::println);

        return sensorReadingRecordList;
    }

    public List<SensorReadingRecord> findLast100Readings(String collectionName){
        MongoCollection<SensorReadingRecord> collection = database.getCollection(collectionName, SensorReadingRecord.class);

        // retrieve and print the records
        List<SensorReadingRecord> sensorReadingRecordList = new ArrayList<SensorReadingRecord>();
        collection.find().sort(descending("timeTaken")).limit(100).into(sensorReadingRecordList);

        //sensorReadingRecordList.forEach(System.out::println);

        return sensorReadingRecordList;
    }

    /**
     * This method is used to retrieve all the readings from a collection
     * @param collectionName
     * @return
     */
    public SensorReadingRecord findReading(String collectionName, Bson filter){
        MongoCollection<SensorReadingRecord> collection = database.getCollection(collectionName, SensorReadingRecord.class);

        // retrieve the records
        List<SensorReadingRecord> sensorReadingRecordList = new ArrayList<SensorReadingRecord>();

        collection.find(filter).limit(1).into(sensorReadingRecordList);

        return sensorReadingRecordList.isEmpty() ? null : sensorReadingRecordList.get(0);
    }

}