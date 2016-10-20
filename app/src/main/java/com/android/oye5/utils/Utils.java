package com.android.oye5.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
	public static String convertStreamToString(InputStream inputStream) throws IOException {
    	if (inputStream != null) {
    		StringBuilder sb = new StringBuilder();
    		String line;
    		try {
    			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
    			while ((line = reader.readLine()) != null) {
    				sb.append(line).append("\n");
    			}
    		} finally {
    			inputStream.close();
    		}
    		return sb.toString();
    	} else {
    		return "";
    	}
    }
	public static String md5(String input) {  
		try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1,messageDigest);
            String md5 = number.toString(16);
            while (md5.length() < 32) {
            	md5 = "0" + md5;
            }
            return md5;
        } catch(NoSuchAlgorithmException e) {
            return null;
        }  
	}

	public static boolean isConnected(Context context) {
		try {
			ConnectivityManager connectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connectivityManager
					.getActiveNetworkInfo();
			return networkInfo != null && networkInfo.isConnected();
		} catch (NullPointerException e) {
			return false;
		}
	}
	
	public static String loadJSONFromAsset(Context ctx, String filename) throws IOException {
    	String json = null;
        try {
            InputStream is = ctx.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

	public static void CreateWorkDirectories(String sPath, boolean bDelOldFiles){
    	File file = new File(sPath);
    	File[] children = file.listFiles();
		if (children == null) {
			file.mkdir();
		} else if (bDelOldFiles){
			Calendar today = Calendar.getInstance();		// Get today as a Calendar  
	        today.add(Calendar.DATE, -7);  			// Subtract 1 day 
	        long _7DaysAgo = today.getTimeInMillis();  
	        
			for (int i=0; i<children.length; i++){
				if (children[i].lastModified() < _7DaysAgo) {
					//Log.e(GlobalConstant.TAG, children[i].getAbsolutePath() + ", " + children[i].getName());
					children[i].delete();
				}
			}
		}
    }
	
	public static String getRealPathFromURI(Activity activity, Uri contentUri) {

        // can post image
        String [] proj={MediaStore.Images.Media.DATA};
        @SuppressWarnings("deprecation")
		Cursor cursor = activity.managedQuery( contentUri,
                        proj, // Which columns to return
                        null,       // WHERE clause; which rows to return (all rows)
                        null,       // WHERE clause selection arguments (none)
                        null); // Order-by clause (ascending by name)
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
	}

	@SuppressWarnings("NewApi")
	public static String getRealPathFromURIKitKat(Activity activity, Uri contentUri) {

		// Will return "image:x*"
		String wholeID = DocumentsContract.getDocumentId(contentUri);

		// Split at colon, use second item in the array
		String id = wholeID.split(":")[1];

		String[] column = { MediaStore.Images.Media.DATA };

		// where id is equal to
		String sel = MediaStore.Images.Media._ID + "=?";

		Cursor cursor = activity.getContentResolver().
				query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
						column, sel, new String[]{ id }, null);

		String filePath = "";

		int columnIndex = cursor.getColumnIndex(column[0]);

		if (cursor.moveToFirst()) {
			filePath = cursor.getString(columnIndex);
		}

		cursor.close();

		return filePath;
	}

	public synchronized static int GetExifOrientation(String filepath) 	{
	    int degree = 0;
	    ExifInterface exif = null;
	    
	    try    {
	        exif = new ExifInterface(filepath);
	    } catch (IOException e)  {
	        Log.e("StylePhoto", "cannot read exif");
	        e.printStackTrace();
	    }
	    
	    if (exif != null) {
	        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
	        
	        if (orientation != -1) {
	            // We only recognize a subset of orientation tag values.
	            switch(orientation) {
	                case ExifInterface.ORIENTATION_ROTATE_90:
	                    degree = 90;
	                    break;
	                    
	                case ExifInterface.ORIENTATION_ROTATE_180:
	                    degree = 180;
	                    break;
	                    
	                case ExifInterface.ORIENTATION_ROTATE_270:
	                    degree = 270;
	                    break;
	            }
	        }
	    }
	    
	    return degree;
	}
	
	public synchronized static Bitmap GetRotatedBitmap(Bitmap bitmap, int degrees) 	{
	    if ( degrees != 0 && bitmap != null )     {
	        Matrix m = new Matrix();
	        m.setRotate(degrees, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2 );
	        try {
	            Bitmap b2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
	            if (bitmap != b2) {
	            	bitmap.recycle();
	            	bitmap = b2;
	            }
	        } catch (OutOfMemoryError ex) {
	            // We have no memory to rotate. Return the original bitmap.
	        }
	    }
	    
	    return bitmap;
	}
	
	@SuppressWarnings("deprecation")
	public synchronized static Bitmap getSafeDecodeBitmap(String strFilePath, int maxSize) {
		try {
			if (strFilePath == null)
				return null;
			// Max image size
			int IMAGE_MAX_SIZE = maxSize;
			
	    	File file = new File(strFilePath);
	    	if (file.exists() == false) {
	    		//DEBUG.SHOW_ERROR(TAG, "[ImageDownloader] SafeDecodeBitmapFile : File does not exist !!");
	    		return null;
	    	}
	    	
	    	BitmapFactory.Options bfo 	= new BitmapFactory.Options();
	    	bfo.inJustDecodeBounds 		= true;
	    	
			BitmapFactory.decodeFile(strFilePath, bfo);
	        
			if (IMAGE_MAX_SIZE > 0) 
		        if(bfo.outHeight * bfo.outWidth >= IMAGE_MAX_SIZE * IMAGE_MAX_SIZE) {
		        	bfo.inSampleSize = (int)Math.pow(2, (int)Math.round(Math.log(IMAGE_MAX_SIZE 
		        						/ (double) Math.max(bfo.outHeight, bfo.outWidth)) / Math.log(0.5)));
		        }
	        bfo.inJustDecodeBounds = false;
	        bfo.inPurgeable = true;
	        bfo.inDither = true;
	        
	        final Bitmap bitmap = BitmapFactory.decodeFile(strFilePath, bfo);
	    	
	        int degree = GetExifOrientation(strFilePath);
	        
	    	return GetRotatedBitmap(bitmap, degree);
		}
		catch(OutOfMemoryError ex)
		{
			ex.printStackTrace();
			
			return null;
		}
	}
	
	public static Drawable getImageDrawableFromAssetFile(Context context, String assetPath){
		try 
		{
		    InputStream ims = context.getAssets().open(assetPath);
		    Drawable drawable = Drawable.createFromStream(ims, null);
		    return drawable;
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static final String getFormattedString(int number){
		return NumberFormat.getNumberInstance(Locale.US).format(number);
	}
	
	public static void showKeyboard(Context ctx, boolean bShow, EditText edtBox){
		if (bShow){
			InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.showSoftInput(edtBox, InputMethodManager.SHOW_IMPLICIT);
		}else{
			InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(edtBox.getWindowToken(), 0);
		}
	}

	public static void hideKeyboard(Activity activity){
		View view = activity.getCurrentFocus();
		if (view != null) {
			InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}

	public static void forceKeyboardHidden(Context ctx, View view){
		InputMethodManager imm = (InputMethodManager)ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	public static void goToBrowser(Context ctx, String url){
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(url));
		ctx.startActivity(i);
	}
	
	public static boolean isEmailValid(String email) {
	    boolean isValid = false;

	    String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
	    CharSequence inputStr = email;

	    Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
	    Matcher matcher = pattern.matcher(inputStr);
	    if (matcher.matches()) {
	        isValid = true;
	    }
	    return isValid;
	}
		
	public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
              int count=is.read(bytes, 0, buffer_size);
              if(count==-1)
                  break;
              os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }
	
	public static void copyFile(String srcPath, String dstPath) throws IOException {
		File src = new File(srcPath);
		File dst = new File(dstPath);
	    InputStream in = new FileInputStream(src);
	    OutputStream out = new FileOutputStream(dst);

	    // Transfer bytes from in to out
	    byte[] buf = new byte[1024];
	    int len;
	    while ((len = in.read(buf)) > 0) {
	        out.write(buf, 0, len);
	    }
	    in.close();
	    out.close();
	}

	public static final String setFirstCapitalLetters(String str){
		if (str == null || str.equals("")) return "";

		String words[] = str.split(" ");
		String retStr = "";
		for (int i = 0; i < words.length; i++){
			retStr +=  " " + words[i].substring(0, 1).toUpperCase() + words[i].substring(1, words[i].length());
		}

		if (!retStr.equals("")) retStr = retStr.substring(1);
		return retStr;
	}

	public static String getFileExtension(String fileName){
		String extension = "";

		int i = fileName.lastIndexOf('.');
		if (i > 0) {
			extension = fileName.substring(i+1);
		}

		return extension;
	}

	public static String getMimeType(String url) {
		String type = null;
		String extension = MimeTypeMap.getFileExtensionFromUrl(url);
		if (extension != null) {
			type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
		}
		return type;
	}

	public static boolean isTextEllipsed(TextView txtView){
		Layout l = txtView.getLayout();
		if (l != null) {
			int lines = l.getLineCount();
			if (lines > 0)
				if (l.getEllipsisCount(lines-1) > 0) return true;
					//Log.d(TAG, "Text is ellipsized");
		}

		return false;
	}
	/**
	 *
	 * @param oldTime	in ms
	 * @param newTime	in ms
	 * @return
	 */
	public static long calcDiffMinutes(long oldTime, long newTime){
		long _1min = 1000L*60L;
		return (newTime - oldTime) /_1min;
	}

	public static ComponentName isServiceExisted(Context context, String className)	{
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

		List<ActivityManager.RunningServiceInfo> serviceList =
				activityManager.getRunningServices(Integer.MAX_VALUE);

		if(!(serviceList.size() > 0))
		{
			return null;
		}

		for(int i = 0; i < serviceList.size(); i++)
		{
			ActivityManager.RunningServiceInfo serviceInfo = serviceList.get(i);
			ComponentName serviceName = serviceInfo.service;

			if(serviceName.getClassName().equals(className))
			{
				return serviceName;
			}
		}
		return null;
	}

	public static final String getDeviceID(Context ctx){
		//return Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID);
        return "1234";
	}

	private static final int ONE_MINUTES = 1000 * 60 * 1;

	/**
	 * To check which of current locations got by GPS and Network is best.
	 * @param location : last location
	 * @param currentBestLocation
	 * @return
	 */
	public static boolean isBetterLocation(Location location, Location currentBestLocation) {
		if (currentBestLocation == null) {
			return true;
		}

		long timeDelta = location.getTime() - currentBestLocation.getTime();
		boolean isSignificantlyNewer = timeDelta > ONE_MINUTES;
		boolean isSignificantlyOlder = timeDelta < -ONE_MINUTES;
		boolean isNewer = timeDelta > 0;

		if (isSignificantlyNewer) {
			return true;
		} else if (isSignificantlyOlder) {
			return false;
		}

		int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
		boolean isLessAccurate = accuracyDelta > 0;
		boolean isMoreAccurate = accuracyDelta < 0;
		boolean isSignificantlyLessAccurate = accuracyDelta > 200;

		boolean isFromSameProvider = isSameProvider(location.getProvider(),
				currentBestLocation.getProvider());

		if (isMoreAccurate) {
			return true;
		} else if (isNewer && !isLessAccurate) {
			return true;
		} else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
			return true;
		}
		return false;
	}

	private static boolean isSameProvider(String provider1, String provider2) {
		if (provider1 == null) {
			return provider2 == null;
		}
		return provider1.equals(provider2);
	}

	public static boolean CheckGPSOpen(Context mContext)
	{
		LocationManager locationManager = (LocationManager) mContext.getSystemService(mContext.LOCATION_SERVICE);
		return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}
}
