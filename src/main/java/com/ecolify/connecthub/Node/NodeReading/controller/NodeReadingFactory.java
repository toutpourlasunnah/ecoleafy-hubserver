package com.ecolify.connecthub.Node.NodeReading.controller;


import com.ecolify.connecthub.Node.NodeReading.model.NodeReadingRecord;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;

public class NodeReadingFactory {

    private static final String TIME_TAKEN_TAG = "timeTaken";
    /**
     * This method is used to create a SensorReadingRecord from a JSONObject
     * @param jsonSensorReadingRecord
     * @return SensorReadingRecord
     */
    public static NodeReadingRecord createSensorReadingRecord(JSONObject jsonSensorReadingRecord)  {

        ObjectMapper mapper = new ObjectMapper();
        NodeReadingRecord record = null;
        try{
            record = mapper.readValue(jsonSensorReadingRecord.toString(), NodeReadingRecord.class);
        } catch (Exception e){
            System.err.println("fail" + e);
        }
//
//        String macAddress =  jsonSensorReadingRecord.has("macAddress") ? jsonSensorReadingRecord.getString("macAddress") : null;
//        String room =  jsonSensorReadingRecord.has("room") ? jsonSensorReadingRecord.getString("room") : null;
//
//        Double temperature = jsonSensorReadingRecord.has("temperature") ? jsonSensorReadingRecord.getDouble("temperature") : null;
//        Double humidity = jsonSensorReadingRecord.has("humidity") ? jsonSensorReadingRecord.getDouble("humidity") : null;
//        Boolean presence = jsonSensorReadingRecord.has("presence") ? jsonSensorReadingRecord.getBoolean("presence") : null;
//
//        LocalDateTime timeTaken = LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS);

        return record;//new SensorReadingRecord(macAddress, room, temperature, humidity, presence, timeTaken);
    }

    /**
     * This method is used to create a SensorReadingRecord from a HashMap
     * @param map
     * @return SensorReadingRecord
     */
    public static NodeReadingRecord createSensorReadingRecord(HashMap map){
        JSONObject jsonObj = new JSONObject(map);
        return createSensorReadingRecord(jsonObj);
    }

    /**
     * This method is used to add a time marker to a JSONObject
     * @param jsonObject
     * @return JSONObject
     */
    public static JSONObject addTimeMarker(JSONObject jsonObject){
        LocalDateTime timeTaken = LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS);
        jsonObject.put(TIME_TAKEN_TAG, timeTaken);
        return jsonObject;
    }

    /**
     * This method is used to convert a SensorReadingRecord to a JSONObject
     * @param nodeReadingRecord
     * @return JSONObject
     */
    public static JSONObject toJSONObject(NodeReadingRecord nodeReadingRecord){
        JSONObject jsonObj = new JSONObject();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        try{
            jsonObj = new JSONObject(mapper.writeValueAsString(nodeReadingRecord));
        } catch (Exception e){
            System.err.println("fail" + e);
        }
//        jsonObj.put("macAddress", sensorReadingRecord.macAddress());
//        jsonObj.put("room", sensorReadingRecord.room());
//
//        jsonObj.put("temperature", sensorReadingRecord.temperature());
//        jsonObj.put("humidity", sensorReadingRecord.humidity());
//        jsonObj.put("presence", sensorReadingRecord.presence());
//
        jsonObj.put(TIME_TAKEN_TAG, nodeReadingRecord.timeTaken()); // JACKSON BEING WEIRD ABOUT IT

        return jsonObj;
    }
}
