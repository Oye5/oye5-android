package com.android.oye5.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProductData implements Serializable {
    private String productId = "";
    private String description = "";
    private String displayName = "";
    private int quantity = 0;
    private int categoryId = -1;
    private String languageCode = "";
    private double price = 0;
    private String currency = "";
    private String status = "";
    private String condition = "";
    private GeoData geoData = new GeoData();
    private UserData seller = new UserData();
    private List<ImageData> imagesList = new ArrayList<>();
    private ThumbData thumb = new ThumbData();
    private int favoriteCount = 0;
    private int viewsCount = 0;
    private String createdAt = "";
    private String updatedAt = "";

    public ProductData() {
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public GeoData getGeoData() {
        return geoData;
    }

    public void setGeoData(GeoData geoData) {
        this.geoData = geoData;
    }

    public UserData getSeller() {
        return seller;
    }

    public void setSeller(UserData seller) {
        this.seller = seller;
    }

    public List<ImageData> getImagesList() {
        return imagesList;
    }

    public void setImagesList(List<ImageData> imagesList) {
        this.imagesList = imagesList;
    }

    public ThumbData getThumb() {
        return thumb;
    }

    public void setThumb(ThumbData thumb) {
        this.thumb = thumb;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(int favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public int getViewsCount() {
        return viewsCount;
    }

    public void setViewsCount(int viewsCount) {
        this.viewsCount = viewsCount;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public ProductData(JSONObject jsonObject){
        super();
        try{
            if (jsonObject.has("product_id") && !jsonObject.isNull("product_id")){
                this.setProductId(jsonObject.getString("product_id"));
            }
            if (jsonObject.has("description") && !jsonObject.isNull("description")){
                this.setDescription(jsonObject.getString("description"));
            }
            if (jsonObject.has("display_name") && !jsonObject.isNull("display_name")){
                this.setDisplayName(jsonObject.getString("display_name"));
            }
            if (jsonObject.has("quantity") && !jsonObject.isNull("quantity")){
                this.setQuantity(jsonObject.getInt("quantity"));
            }
            if (jsonObject.has("category_id") && !jsonObject.isNull("category_id")){
                this.setCategoryId(jsonObject.getInt("category_id"));
            }
            if (jsonObject.has("language_code") && !jsonObject.isNull("language_code")){
                this.setLanguageCode(jsonObject.getString("language_code"));
            }
            if (jsonObject.has("price") && !jsonObject.isNull("price")){
                this.setPrice(jsonObject.getDouble("price"));
            }
            if (jsonObject.has("currency") && !jsonObject.isNull("currency")){
                this.setCurrency(jsonObject.getString("currency"));
            }
            if (jsonObject.has("status") && !jsonObject.isNull("status")){
                this.setStatus(jsonObject.getString("status"));
            }
            if (jsonObject.has("condition") && !jsonObject.isNull("condition")){
                this.setCondition(jsonObject.getString("condition"));
            }
            if (jsonObject.has("geo") && !jsonObject.isNull("geo")){
                this.setGeoData(new GeoData(jsonObject.getJSONObject("geo")));
            }
            if (jsonObject.has("seller") && !jsonObject.isNull("seller")){
                this.setSeller(new UserData(jsonObject.getJSONObject("seller")));
            }
            if (jsonObject.has("images") && !jsonObject.isNull("images")){
                this.setImagesList(ImageData.buildDataListFromJSONArray(jsonObject.getJSONArray("images")));
            }
            if (jsonObject.has("thumb") && !jsonObject.isNull("thumb")){
                this.setThumb(new ThumbData(jsonObject.getJSONObject("thumb")));
            }
            if (jsonObject.has("favorites_count") && !jsonObject.isNull("favorites_count")){
                this.setFavoriteCount(jsonObject.getInt("favorites_count"));
            }
            if (jsonObject.has("views_count") && !jsonObject.isNull("views_count")){
                this.setViewsCount(jsonObject.getInt("views_count"));
            }
            if (jsonObject.has("views_count") && !jsonObject.isNull("views_count")){
                this.setViewsCount(jsonObject.getInt("views_count"));
            }
            if (jsonObject.has("created_at") && !jsonObject.isNull("created_at")){
                this.setCreatedAt(jsonObject.getString("created_at"));
            }
            if (jsonObject.has("updated_at") && !jsonObject.isNull("updated_at")){
                this.setUpdatedAt(jsonObject.getString("updated_at"));
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    public static ArrayList<ProductData> buildDataListFromJSONArray(JSONArray jsonArray){
        ArrayList<ProductData> dataList = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                dataList.add(new ProductData(jsonArray.getJSONObject(i)));
            }
        }catch(JSONException e){
            e.printStackTrace();
        }

        return dataList;
    }

    public String getAddedDateString(){
        //2016-10-08T11:52:19.204+05:30
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            String subStr = getCreatedAt().substring(0, 10);
            Date date = format.parse(subStr);
            return new SimpleDateFormat("MMM dd yyyy").format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }


}
