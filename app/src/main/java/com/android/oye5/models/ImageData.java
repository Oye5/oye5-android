package com.android.oye5.models;

import com.android.oye5.globals.GlobalConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class ImageData implements Serializable {
    private String id = "";
    private String url = "";

    public ImageData() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return GlobalConstant.isDebug? ("http://mypmpnow.com/api.php/" + url):url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ImageData(JSONObject jsonObject){
        super();
        try{
            if (jsonObject.has("id") && !jsonObject.isNull("id")){
                this.setId(jsonObject.getString("id"));
            }
            if (jsonObject.has("url") && !jsonObject.isNull("url")){
                this.setUrl(jsonObject.getString("url"));
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    public static ArrayList<ImageData> buildDataListFromJSONArray(JSONArray jsonArray){
        ArrayList<ImageData> dataList = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                dataList.add(new ImageData(jsonArray.getJSONObject(i)));
            }
        }catch(JSONException e){
            e.printStackTrace();
        }

        return dataList;
    }
}
