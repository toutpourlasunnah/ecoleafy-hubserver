package com.ecolify.connecthub.Node.NodeCommand.controller;

import com.ecolify.connecthub.Node.NodeCommand.model.NodeCommandRecord;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.json.JSONObject;

public class NodeCommandFactory {

    /**
     * This method is used to create a NodeCommandRecord from a NodeCommand
     * @param macAddress
     * @param room
     * @param switchId
     * @param state
     * @return NodeCommandRecord
     */
    static public NodeCommandRecord createNodeCommandRecord(String macAddress, String room, String switchId, double state){
        return new NodeCommandRecord(macAddress, room, switchId, state);
    }

    /**
     * This method is used to create a JSONObject from a NodeCommandRecord
     * @param nodeCommandRecord
     * @return JSONObject
     */
    static public JSONObject toJsonObject(NodeCommandRecord nodeCommandRecord){
        JSONObject jsonObject = new JSONObject();
        ObjectMapper mapper = new ObjectMapper();
        //mapper.registerModule(new JavaTimeModule());
        try{
            jsonObject = new JSONObject(mapper.writeValueAsString(nodeCommandRecord));
        } catch (Exception e){System.err.println("Fail" + e);}
        return jsonObject;
    }



    /** BADDDDDDDDDDDDD TO REDO DESIGNM
     * This method is used to create a JSONObject from a ModuleCommand
     * @param moduleCommand
     * @return JSONObject
     */
    static public JSONObject turnOnSwitch(String switchId, double state){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(switchId, state);
        return jsonObject;
    }

}
