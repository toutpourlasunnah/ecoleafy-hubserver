package com.ecolify.connecthub.model;

import org.json.JSONObject;

public class ModuleCommandFactory {
    /**
     * This method is used to create a JSONObject from a ModuleCommand
     * @param moduleCommand
     * @return
     */
    static public JSONObject turnOnSwitch(String switchId, double state){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(switchId, state);
        return jsonObject;
    }

}
