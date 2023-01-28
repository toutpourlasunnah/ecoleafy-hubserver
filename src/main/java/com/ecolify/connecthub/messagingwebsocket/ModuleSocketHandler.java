package com.ecolify.connecthub.messagingwebsocket;


import com.ecolify.connecthub.model.SensorReadingFactory;
import com.ecolify.connecthub.model.SensorReadingRecord;
import com.ecolify.connecthub.persistency.MongoClientConnection;
import com.mongodb.MongoException;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ModuleSocketHandler extends TextWebSocketHandler {
    public HashMap<WebSocketSession, String> webSocketSessionManager = new HashMap<>();
    private MongoClientConnection mongoClientConnection = new MongoClientConnection();

    @Override
    public void afterConnectionEstablished(WebSocketSession session){
        try{
            webSocketSessionManager.put(session, "");
        } catch (MongoException e){
            System.err.println("MONGO ERROR " + e);
        }

    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws InterruptedException, IOException {
        String payload = message.getPayload();
        try {
            JSONObject jsonReceived = new JSONObject(payload);
            SensorReadingRecord newReadingRecord = SensorReadingFactory.createSensorReadingRecord(jsonReceived);
            mongoClientConnection.insertReading(jsonReceived.getString("room"), newReadingRecord);

            System.out.println("sending back received thing");
            SensorReadingFactory.addTimeMarker(jsonReceived);
            session.sendMessage(new TextMessage(jsonReceived.toString()));

            // UPDATE EVERYONE
            sendUpdateToAppSubs(jsonReceived);

        } catch (JSONException e){
            System.err.println("Bad Request");
            session.sendMessage(new TextMessage("BAD REQUEST"));
        } catch (Exception e){
            System.err.println("Other error " + e);
        }



    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        webSocketSessionManager.remove(session);
    }

    private void sendUpdateToAppSubs(JSONObject newValue) throws IOException {
        String roomName = newValue.getString("room");
        for (Map.Entry<WebSocketSession, String> entry: WebAppSocketHandler.webSocketSessionManager.entrySet()) {
            if (entry.getValue().equals(roomName)){
                entry.getKey().sendMessage(new TextMessage(newValue.toString()));
            } else if (entry.getValue().equals("ALL")) {
                entry.getKey().sendMessage(new TextMessage(newValue.toString()));
            }
        }


    }


}
