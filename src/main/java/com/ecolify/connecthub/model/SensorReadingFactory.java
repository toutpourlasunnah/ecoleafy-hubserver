package com.ecolify.connecthub.model;


import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;

public class SensorReadingFactory {

    public static SensorReadingRecord createSensorReadingRecord(JSONObject jsonObj){
        String macAddress =  jsonObj.has("macAddress") ? jsonObj.getString("macAddress") : null;
        String room =  jsonObj.has("room") ? jsonObj.getString("room") : null;

        Double temperature = jsonObj.has("temperature") ? jsonObj.getDouble("temperature") : null;
        Double humidity = jsonObj.has("humidity") ? jsonObj.getDouble("humidity") : null;
        Boolean presence = jsonObj.has("presence") ? jsonObj.getBoolean("presence") : null;

        LocalDateTime timeTaken = LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS);

        return new SensorReadingRecord(macAddress, room, temperature, humidity, presence, timeTaken);
    }

    public static SensorReadingRecord createSensorReadingRecord(HashMap map){
        JSONObject jsonObj = new JSONObject(map);
        return createSensorReadingRecord(jsonObj);
    }

    public static JSONObject addTimeMarker(JSONObject jsonObject){
        LocalDateTime timeTaken = LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS);
        jsonObject.put("timeTaken", timeTaken);
        return jsonObject;
    }

    public static JSONObject toJSONObject(SensorReadingRecord srr){
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("macAddress", srr.macAddress());
        jsonObj.put("room", srr.room());

        jsonObj.put("temperature", srr.temperature());
        jsonObj.put("humidity", srr.humidity());
        jsonObj.put("presence", srr.presence());

        jsonObj.put("timeTaken", srr.timeTaken());

        return jsonObj;
    }
}
