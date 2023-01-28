package com.ecolify.connecthub.model;

import org.json.JSONObject;

public class ModuleCommandFactory {

    static public JSONObject turnOnSwitch(String switchName, double state){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(switchName, state);
        return jsonObject;
    }

}
