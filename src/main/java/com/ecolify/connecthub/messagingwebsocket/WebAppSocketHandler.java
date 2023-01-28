package com.ecolify.connecthub.messagingwebsocket;

import com.ecolify.connecthub.model.SensorReadingFactory;
import com.ecolify.connecthub.model.SensorReadingRecord;
import com.ecolify.connecthub.persistency.MongoClientConnection;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONPointerException;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class WebAppSocketHandler extends TextWebSocketHandler  {
    public static HashMap<WebSocketSession, String> webSocketSessionManager = new HashMap<>(); // session, sub
    private MongoClientConnection mongoClientConnection = new MongoClientConnection();

    @Override
    public void afterConnectionEstablished(WebSocketSession session){
        WebAppSocketHandler.webSocketSessionManager.put(session, "");
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws InterruptedException, IOException {
        String payload = message.getPayload();

        try {
            JSONObject jsonRequest = new JSONObject(payload);
            getRoomCommand(jsonRequest, session);
            getRoomHistoryCommand(jsonRequest, session);
        } catch (JSONPointerException e){
            System.err.println("No room requested");
        } catch (JSONException e){
            session.sendMessage(new TextMessage("BAD REQUEST"));
        }
    }

    private void getRoomCommand(JSONObject jsonObject, WebSocketSession session){
        try {
            String room = jsonObject.getString("room");
            WebAppSocketHandler.webSocketSessionManager.put(session, room);
        } catch (JSONException e){
            System.out.println("not a room command");
        }
    }

    private void getRoomHistoryCommand(JSONObject jsonObject, WebSocketSession session){
        try {
            String room_history = jsonObject.getString("room_history");
            List<SensorReadingRecord> sensorReadingRecordList = mongoClientConnection.findReadings(room_history);
            for (SensorReadingRecord readingRecord: sensorReadingRecordList) {
                session.sendMessage(new TextMessage(SensorReadingFactory.toJSONObject(readingRecord).toString()));
            }

        } catch (JSONException e){
            System.out.println("WEBAPP WS : not a room history command");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        WebAppSocketHandler.webSocketSessionManager.remove(session);
    }

}
