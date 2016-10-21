package com.android.oye5.utils;

import android.content.Context;
import android.util.Log;

import com.android.oye5.Oye5App;
import com.android.oye5.preferences.AppPreference;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.File;

import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

public class RestClientUtils {
	private static final String BASE_URL_USER  = "http://user.oye5.com/"; 			//"http://52.89.17.179:8080/";
	private static final String BASE_URL_PRODUCT  = "http://product.oye5.com/";		//"http://52.43.30.248:8080/";
	private static final String BASE_URL_IMAGE  = "http://upload.oye5.com/";		//"http://54.69.168.103:8080/";
	private static final String BASE_URL_CHAT  = "http://chat.oye5.com/";			//"http://52.39.133.116:8080/";

	private static AsyncHttpClient client = new AsyncHttpClient();
	static {
		client.setConnectTimeout(60000);
		client.setTimeout(60000);
		client.setResponseTimeout(60000);
	}

	public static void get(Context ctx, String url, RequestParams params, boolean needAuth, AsyncHttpResponseHandler responseHandler) {
		if (!Utils.isConnected(ctx)) return;
		if( params == null ) {
			params = new RequestParams();
		}
		params.setContentEncoding("UTF-8");
		String serviceUrl = getServiceUrl(ctx, url, needAuth);
		Log.i(RestClientUtils.class.getName(), "url:" + serviceUrl + ", GET params:" + params.toString());
		client.get(serviceUrl, params, responseHandler);
	}
	
	public static void post(Context ctx, String url, JSONObject objParams, boolean needAuth, AsyncHttpResponseHandler responseHandler){
		if (!Utils.isConnected(ctx)) return;
		try{
			if( objParams == null ) {
				objParams = new JSONObject();
			}
			ByteArrayEntity entity = new ByteArrayEntity(objParams.toString().getBytes("UTF-8"));
			entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
			String serviceUrl = getServiceUrl(ctx, url, needAuth);
			Log.i(RestClientUtils.class.getName(), "url:" + serviceUrl + ", POST params:" + objParams.toString());
			client.post(ctx, serviceUrl, entity, "application/json", responseHandler);
		}catch(Exception e){
            e.printStackTrace();
        }
	}

	public static void post(Context ctx, String url, JSONObject objParams, String token, AsyncHttpResponseHandler responseHandler){
		if (!Utils.isConnected(ctx)) return;
		try{
			if( objParams == null ) {
				objParams = new JSONObject();
			}
			ByteArrayEntity entity = new ByteArrayEntity(objParams.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
			String serviceUrl = getServiceUrl(ctx, url, false);
			client.removeHeader("auth");
			client.addHeader("auth", token);
			Log.i(RestClientUtils.class.getName(), "url:" + serviceUrl + ", POST params:" + objParams.toString());
			client.post(ctx, serviceUrl, entity, "application/json", responseHandler);
		}catch(Exception e){}
	}

	public static void post(Context ctx, String url, RequestParams params, boolean needAuth, AsyncHttpResponseHandler responseHandler){
		if (params == null) {
			params = new RequestParams();
		}
		try {
			String serviceUrl = getServiceUrl(ctx, url, needAuth);
			Log.i(RestClientUtils.class.getName(), "url:" + serviceUrl + ", POST params:" + params.toString());
			client.post(serviceUrl, params, responseHandler);
		}catch(Exception e){}
	}

	public static void postAttachment(Context ctx, String url, String fileName, boolean needAuth, AsyncHttpResponseHandler responseHandler){
		RequestParams params = new RequestParams();
		/*MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		File file = new File(fileName);
		entity.addPart("file", new FileBody(file, file.getName(), Utils.getMimeType(fileName), "UTF-8"));*/
		File file = new File(fileName);
		try {
			params.put("file", file);
			client.post(getServiceUrl(ctx, url, needAuth), params, responseHandler);
		}catch(Exception e){}
	}

	public static void put(Context ctx, String url, JSONObject objParams, boolean needAuth, AsyncHttpResponseHandler responseHandler){
		if (!Utils.isConnected(ctx)) return;
		try{
			if (objParams == null) objParams = new JSONObject();
			ByteArrayEntity entity = new ByteArrayEntity(objParams.toString().getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
			String serviceUrl = getServiceUrl(ctx, url, needAuth);
			Log.i(RestClientUtils.class.getName(), "url:" + serviceUrl + ", PUT params:" + objParams.toString());
			client.put(ctx, serviceUrl, entity, "application/json", responseHandler);
		}catch(Exception e){}
	}

	public static void delete(Context ctx, String url, boolean needAuth, AsyncHttpResponseHandler responseHandler){
		if (!Utils.isConnected(ctx)) return;
		String serviceUrl = getServiceUrl(ctx, url, needAuth);
		Log.i(RestClientUtils.class.getName(), "url:" + serviceUrl + ", DELETE");
		client.delete(serviceUrl, responseHandler);
	}

	private static String getServiceUrl(Context ctx, String url, boolean needAuth){
        String baseURL = "";
        if (url.contains("userservice")){
            baseURL = BASE_URL_USER;
        }else if (url.contains("productservice")){
            baseURL = BASE_URL_PRODUCT;
        }else if (url.contains("uploadimage")){
            baseURL = BASE_URL_IMAGE;
        }else if (url.contains("chatservice")){
            baseURL = BASE_URL_CHAT;
        }
		String serviceUrl = baseURL + url;
		client.removeHeader("auth");
		if (needAuth){
			client.addHeader("auth", Oye5App.getInstance().getToken());
		}
		return serviceUrl;
	}
}
