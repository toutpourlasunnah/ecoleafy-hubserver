package com.ecolify.connecthub.model;


import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;

public class SensorReadingFactory {

    /**
     * This method is used to create a SensorReadingRecord from a JSONObject
     * @param jsonSensorReadingRecord
     * @return
     */
    public static SensorReadingRecord createSensorReadingRecord(JSONObject jsonSensorReadingRecord){
        String macAddress =  jsonSensorReadingRecord.has("macAddress") ? jsonSensorReadingRecord.getString("macAddress") : null;
        String room =  jsonSensorReadingRecord.has("room") ? jsonSensorReadingRecord.getString("room") : null;

        Double temperature = jsonSensorReadingRecord.has("temperature") ? jsonSensorReadingRecord.getDouble("temperature") : null;
        Double humidity = jsonSensorReadingRecord.has("humidity") ? jsonSensorReadingRecord.getDouble("humidity") : null;
        Boolean presence = jsonSensorReadingRecord.has("presence") ? jsonSensorReadingRecord.getBoolean("presence") : null;

        LocalDateTime timeTaken = LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS);

        return new SensorReadingRecord(macAddress, room, temperature, humidity, presence, timeTaken);
    }

    /**
     * This method is used to create a SensorReadingRecord from a HashMap
     * @param map
     * @return
     */
    public static SensorReadingRecord createSensorReadingRecord(HashMap map){
        JSONObject jsonObj = new JSONObject(map);
        return createSensorReadingRecord(jsonObj);
    }

    /**
     * This method is used to add a time marker to a JSONObject
     * @param jsonObject
     * @return
     */
    public static JSONObject addTimeMarker(JSONObject jsonObject){
        LocalDateTime timeTaken = LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS);
        jsonObject.put("timeTaken", timeTaken);
        return jsonObject;
    }

    /**
     * This method is used to convert a SensorReadingRecord to a JSONObject
     * @param sensorReadingRecord
     * @return
     */
    public static JSONObject toJSONObject(SensorReadingRecord sensorReadingRecord){
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("macAddress", sensorReadingRecord.macAddress());
        jsonObj.put("room", sensorReadingRecord.room());

        jsonObj.put("temperature", sensorReadingRecord.temperature());
        jsonObj.put("humidity", sensorReadingRecord.humidity());
        jsonObj.put("presence", sensorReadingRecord.presence());

        jsonObj.put("timeTaken", sensorReadingRecord.timeTaken());

        return jsonObj;
    }
}
