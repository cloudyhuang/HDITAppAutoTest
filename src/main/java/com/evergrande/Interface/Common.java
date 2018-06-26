package com.evergrande.Interface;

import org.json.JSONException;
import org.json.JSONObject;

public class Common {
	 /**
     * 解析Json内容
     * 
     * @author 黄霄
     * @version 1.0 2017/5/07
     * @return JsonValue 返回JsonString中JsonId对应的Value
     **/
    public static String getJsonValue(String JsonString, String JsonId) {
        String JsonValue = "";
        if (JsonString == null || JsonString.trim().length() < 1) {
            return null;
        }
        try {
            JSONObject obj1 = new JSONObject(JsonString);
            JsonValue = String.valueOf(obj1.get(JsonId));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return JsonValue;
    }
}
