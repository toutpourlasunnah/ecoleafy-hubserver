package com.ecolify.connecthub.messagingwebsocket;

import com.ecolify.connecthub.Node.NodeReading.controller.NodeReadingFactory;
import com.ecolify.connecthub.Node.NodeReading.model.NodeReadingRecord;
import com.ecolify.connecthub.persistency.MongoClientConnection;
import com.google.common.collect.Lists;
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
import java.util.Map;

public class WebAppSocketHandler extends TextWebSocketHandler  {
    public static HashMap<WebSocketSession, String> webSocketSessionManager = new HashMap<>(); // session, sub
    private MongoClientConnection mongoClientConnection = new MongoClientConnection();

    /**
     * When a client connects, add it to the list of subscribers
     * @param session
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session){
        System.out.println("[DEBUG] WebApp WebSocket - New Connection : " + session.getId());
        WebAppSocketHandler.webSocketSessionManager.put(session, "");
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws InterruptedException, IOException {
        String payload = message.getPayload();

        try {
            JSONObject jsonRequest = new JSONObject(payload);
            System.out.println("[DEBUG] WebApp WebSocket - DataReceived : " + jsonRequest.toString());

            getRoomCommand(jsonRequest, session);
            getRoomHistoryCommand(jsonRequest, session);
            getModuleCommand(jsonRequest, session);
        } catch (JSONPointerException e){
            System.err.println("JSON Pointer error");
        } catch (JSONException e){
            //session.sendMessage(new TextMessage("BAD REQUEST"));
        }
    }

    /**
     * Subscribe a WebSocketSession to a room
     * @param session
     */
    private void getRoomCommand(JSONObject jsonObject, WebSocketSession session){
        try {
            String room = jsonObject.getString("room");
            WebAppSocketHandler.webSocketSessionManager.put(session, room);
        } catch (JSONException e){}
    }


    /**
     * Send a command to a module
     * @param jsonObject
     * @param session
     * @throws IOException
     */
    private void getModuleCommand(JSONObject jsonObject, WebSocketSession session) throws IOException {
        String[] listSwitchName = {"switch_1", "switch_2", "switch_3"};

        for (Map.Entry<WebSocketSession, String> entry:ModuleSocketHandler.webSocketSessionManager.entrySet()) {
            String targetRoom = jsonObject.getString("room_command");
            if (targetRoom.equals(entry.getValue())){
                entry.getKey().sendMessage(new TextMessage(jsonObject.toString()));
                break;
            }
        }
    }

    /**
     * Send the history of a room
     * @param jsonObject
     * @param session
     */
    private void getRoomHistoryCommand(JSONObject jsonObject, WebSocketSession session){
        try {
            String room_history = jsonObject.getString("room_history");
            List<NodeReadingRecord> nodeReadingRecordList = mongoClientConnection.findLast100Readings(room_history);

            for (NodeReadingRecord readingRecord: Lists.reverse(nodeReadingRecordList)) {
                session.sendMessage(new TextMessage(NodeReadingFactory.toJSONObject(readingRecord).toString()));
            }

        } catch (JSONException e){} catch (IOException e) {}
    }

    /**
     * When a client disconnects, remove it from the list of subscribers
     * @param session
     * @param status
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        System.out.println("[DEBUG] WebApp WebSocket - Lost Connection : " + session.getId());
        WebAppSocketHandler.webSocketSessionManager.remove(session);
    }

}
