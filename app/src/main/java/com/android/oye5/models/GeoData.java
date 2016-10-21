package com.android.oye5.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class GeoData implements Serializable {
    private double lat = 0;
    private double lng = 0;
    private String countryCode = "";
    private String city = "";
    private String zipCode = "";
    private double distance = 0;

    public GeoData() {
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public GeoData(JSONObject jsonObject){
        super();
        try{
            if (jsonObject.has("lat") && !jsonObject.isNull("lat")){
                this.setLat(jsonObject.getDouble("lat"));
            }
            if (jsonObject.has("lng") && !jsonObject.isNull("lng")){
                this.setLng(jsonObject.getDouble("lng"));
            }
            if (jsonObject.has("country_code") && !jsonObject.isNull("country_code")){
                this.setCountryCode(jsonObject.getString("country_code"));
            }
            if (jsonObject.has("city") && !jsonObject.isNull("city")){
                this.setCity(jsonObject.getString("city"));
            }
            if (jsonObject.has("zip_code") && !jsonObject.isNull("zip_code")){
                this.setZipCode(jsonObject.getString("zip_code"));
            }
            if (jsonObject.has("distance") && !jsonObject.isNull("distance")){
                this.setDistance(jsonObject.getDouble("distance"));
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    public static ArrayList<GeoData> buildDataListFromJSONArray(JSONArray jsonArray){
        ArrayList<GeoData> dataList = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                dataList.add(new GeoData(jsonArray.getJSONObject(i)));
            }
        }catch(JSONException e){
            e.printStackTrace();
        }

        return dataList;
    }
}
