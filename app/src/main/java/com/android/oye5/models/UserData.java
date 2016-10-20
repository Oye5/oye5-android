package com.android.oye5.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class UserData implements Serializable {
    private String providerToken = "";
    private String name = "";
    private String id = "";
    private String firstName = "";
    private String lastName = "";
    private String email = "";
    private String profilePicURL = "";
    private String countryCode = "";
    private String city = "";
    private String zipCode = "";
    private String banned = "";
    private String status = "";
    private double userRating = 0;

    public UserData() {
    }

    public String getProviderToken() {
        return providerToken;
    }

    public void setProviderToken(String providerToken) {
        this.providerToken = providerToken;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePicURL() {
        return profilePicURL;
    }

    public void setProfilePicURL(String profilePicURL) {
        this.profilePicURL = profilePicURL;
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

    public String getBanned() {
        return banned;
    }

    public void setBanned(String banned) {
        this.banned = banned;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getUserRating() {
        return userRating;
    }

    public void setUserRating(double userRating) {
        this.userRating = userRating;
    }

    public UserData(JSONObject jsonObject){
        super();
        try{
            if (jsonObject.has("providerToken") && !jsonObject.isNull("providerToken")){
                this.setProviderToken(jsonObject.getString("providerToken"));
            }
            if (jsonObject.has("userName") && !jsonObject.isNull("userName")){
                this.setName(jsonObject.getString("userName"));
            }else if (jsonObject.has("user_name") && !jsonObject.isNull("user_name")){
                this.setName(jsonObject.getString("user_name"));
            }
            if (jsonObject.has("userId") && !jsonObject.isNull("userId")){
                this.setId(jsonObject.getString("userId"));
            }else if (jsonObject.has("user_id") && !jsonObject.isNull("user_id")){
                this.setId(jsonObject.getString("user_id"));
            }
            if (jsonObject.has("first_name") && !jsonObject.isNull("first_name")){
                this.setFirstName(jsonObject.getString("first_name"));
            }
            if (jsonObject.has("last_name") && !jsonObject.isNull("last_name")){
                this.setLastName(jsonObject.getString("last_name"));
            }
            if (jsonObject.has("email") && !jsonObject.isNull("email")){
                this.setEmail(jsonObject.getString("email"));
            }
            if (jsonObject.has("profile_pic_url") && !jsonObject.isNull("profile_pic_url")){
                this.setProfilePicURL(jsonObject.getString("profile_pic_url"));
            }
            if (jsonObject.has("country_code") && !jsonObject.isNull("country_code")){
                this.setCountryCode(jsonObject.getString("country_code"));
            }
            if (jsonObject.has("city") && !jsonObject.isNull("city")) {
                this.setCity(jsonObject.getString("city"));
            }
            if (jsonObject.has("zip_code") && !jsonObject.isNull("zip_code")) {
                this.setZipCode(jsonObject.getString("zip_code"));
            }
            if (jsonObject.has("banned") && !jsonObject.isNull("banned")) {
                this.setBanned(jsonObject.getString("banned"));
            }
            if (jsonObject.has("status") && !jsonObject.isNull("status")) {
                this.setStatus(jsonObject.getString("status"));
            }
            if (jsonObject.has("userRating") && !jsonObject.isNull("userRating")) {
                this.setUserRating(jsonObject.getDouble("userRating"));
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    public static ArrayList<UserData> buildDataListFromJSONArray(JSONArray jsonArray){
        ArrayList<UserData> dataList = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                dataList.add(new UserData(jsonArray.getJSONObject(i)));
            }
        }catch(JSONException e){
            e.printStackTrace();
        }

        return dataList;
    }
}
