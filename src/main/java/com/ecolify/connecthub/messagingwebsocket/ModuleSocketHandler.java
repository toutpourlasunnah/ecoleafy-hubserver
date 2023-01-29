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
    static public HashMap<WebSocketSession, String> webSocketSessionManager = new HashMap<>();
    private MongoClientConnection mongoClientConnection = new MongoClientConnection();

    /**
     * When a client connects, add it to the list of subscribers
     * @param session
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session){
        try{
            System.out.println("[DEBUG] Module WebSocket - New Connection : " + session.getId());
            ModuleSocketHandler.webSocketSessionManager.put(session, "");
        } catch (MongoException e){
            System.err.println("MONGO ERROR " + e);
        }

    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws InterruptedException, IOException {
        String payload = message.getPayload();
        try {
            JSONObject jsonReceived = new JSONObject(payload);

            String room = jsonReceived.getString("room");

            SensorReadingRecord newReadingRecord = SensorReadingFactory.createSensorReadingRecord(jsonReceived);

            // Add Reading to DB
            manageNewReading(newReadingRecord, room, session);

            System.out.println("[DEBUG] Module WebSocket - DataReceived : " + jsonReceived.toString());

            // UPDATE EVERYONE
            sendUpdateToAppSubs(SensorReadingFactory.addTimeMarker(jsonReceived));

        } catch (JSONException e){
            System.err.println("Bad Request");
            session.sendMessage(new TextMessage("BAD REQUEST"));
        } catch (Exception e){
            System.err.println("Other error " + e);
        }



    }

    /**
     * When a client disconnects, remove it from the list of subscribers
     * @param session
     * @param status
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        System.out.println("[DEBUG] Module WebSocket - Lost Connection : " + session.getId());
        ModuleSocketHandler.webSocketSessionManager.remove(session);
    }

    /**
     * Send a message to all the subscribers of a room
     * @param newValue
     * @throws IOException
     */
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

    private void manageNewReading(SensorReadingRecord newReading, String room, WebSocketSession session) throws IOException {
        mongoClientConnection.insertReading(room, newReading);
        webSocketSessionManager.put(session, room);
    }


}
