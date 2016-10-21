package com.android.oye5.models;

import com.android.oye5.globals.GlobalConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class ThumbData implements Serializable {
    private String url = "";
    private int width = 0;
    private int height = 0;

    public ThumbData() {
    }

    public String getUrl() {
        return GlobalConstant.isDebug? "http://mypmpnow.com/api.php/" + url:url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public ThumbData(JSONObject jsonObject){
        super();
        try{
            if (jsonObject.has("url") && !jsonObject.isNull("url")){
                this.setUrl(jsonObject.getString("url"));
            }
            if (jsonObject.has("width") && !jsonObject.isNull("width")){
                this.setWidth(jsonObject.getInt("width"));
            }
            if (jsonObject.has("height") && !jsonObject.isNull("height")){
                this.setHeight(jsonObject.getInt("height"));
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    public static ArrayList<ThumbData> buildDataListFromJSONArray(JSONArray jsonArray){
        ArrayList<ThumbData> dataList = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                dataList.add(new ThumbData(jsonArray.getJSONObject(i)));
            }
        }catch(JSONException e){
            e.printStackTrace();
        }

        return dataList;
    }
}
