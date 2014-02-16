package com.nickstephen.lib.misc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import com.nickstephen.lib.http.ByteArrayOutputStreamProgress;
import com.nickstephen.lib.http.IWriteListener;

/**
 * A collection of helper static methods that are sometimes useful.
 * @author Nick Stephen (a.k.a saltisgood)
 *
 */
public class StatMethods {
	/**
	 * A char array of all possible hex values in lower case
	 */
	private static final char[] hexArray = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    private StatMethods() {}

	/**
	 * Create and display a generic alert message popup with custom title and message
	 * @param ctxt The context to display the popup with
	 * @param title The title of the popup
	 * @param mesg The message content in the popup
	 */
	public static void AlertMesg(Context ctxt, String title, String mesg){
		AlertDialog.Builder builder = new AlertDialog.Builder(ctxt);
    	builder.setTitle(title);
    	builder.setMessage(mesg);
    	builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();}});
    	
    	AlertDialog alert = builder.create();
    	alert.show();
	}
	
	/**
	 * Create and display a popup box that can be used to get a yes/no response. 
	 * If the posButton or negButton parameters are null they will just cancel the 
	 * dialog box.
	 * @param ctxt The context to use to construct the popup
	 * @param title The title of the popup box
	 * @param mesg The message to display in the popup
	 * @param posButton The OnClickListener to run on pressing "Yes" (Optional)
	 * @param negButton The OnClickListener to run on pressing "No" (Optional)
	 */
	public static void QuestionBox(Context ctxt, String title, String mesg, DialogInterface.OnClickListener posButton, DialogInterface.OnClickListener negButton) {
		AlertDialog.Builder builder = new AlertDialog.Builder(ctxt);
		builder.setTitle(title);
		builder.setMessage(mesg);
		if (posButton == null)
			posButton = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
				}
			};
		if (negButton == null)
			negButton = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
				}
			};
		
		builder.setPositiveButton("Yes", posButton);
		builder.setNegativeButton("No", negButton);
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	/**
	 * A very generic alert popup on a general error
	 * @param ctxt The context to be used to create the popup
	 */
	public static void DefaultError(Context ctxt){
		AlertMesg(ctxt, "Error!", "An error occurred");
	}
	
	/**
	 * Make and show a toast message with a given text message for a particular duration.
	 * dur should be either Toast.LENGTH_SHORT or Toast.LENGTH_LONG
	 * @param ctxt The context to be used to create the toast
	 * @param text The text to display as the body of the toast
	 * @param dur The duration of the toast (see above)
	 */
	public static void hotBread(Context ctxt, CharSequence text, int dur){
		Toast toast = Toast.makeText(ctxt, text, dur);
		toast.show();
	}
	
	/**
	 * Check whether a string is null, empty or perhaps even just whitespace
	 * @param obj The string to test
	 * @return True if the string is null or empty, false otherwise
	 */
	public static Boolean IsStringNullOrEmpty(String obj)
	{
		if (obj == null || obj.compareTo("") == 0 || obj.trim().compareTo("") == 0)
			return true;
		return false;
	}
	
	/**
	 * Convert a byte array to a string of hexadecimal chars
	 * @param arr The byte array to convert
	 * @return The string representation of the hex values
	 */
	public static String bytesToHex(byte[] arr) {
		char[] hexChars = new char[arr.length * 2];
		int v;
		for (int i = 0; i < arr.length; i++) {
			v = arr[i] & 0xFF;
			hexChars[2 * i] = hexArray[v >>> 4];
			hexChars[2 * i + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}
	
	/**
	 * Convert an InputStream to a string.
	 * Note that the method doesn't rewind the stream before reading and will
	 * read to the end of the stream.
	 * @param is The InputStream to read from
	 * @return The string representation
	 */
	public static String inputStreamToString(InputStream is){
		String line = "";
		StringBuilder total = new StringBuilder();
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		try {
			while ((line = rd.readLine()) != null) {
				total.append(line + "\n");
			}
		} catch (IOException e) {
			return total.toString();
		}
		return total.toString();
	}

	/**
	 * Check whether the internet is currently available
	 * @param ctxt The context to be used to get connectivity info
	 * @param showAlert True to show an error toast if not connected, false suppresses this
	 * @return True if there is network access, false otherwise
	 */
	public static Boolean isNetworkAvailable(Context ctxt, Boolean showAlert) {
		ConnectivityManager connMgr = (ConnectivityManager)ctxt.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo nInfo = connMgr.getActiveNetworkInfo();
		if (nInfo == null || !nInfo.isConnected()){
			if (showAlert) {
				StatMethods.hotBread(ctxt, "Must be connected to a network", Toast.LENGTH_LONG);
			}
			return false;
		}
		return true;
	}

	/**
	 * Convert an InputStream to a byte array.
	 * Note that the method doesn't rewind the stream and will
	 * read to its end
	 * @param is The InputStream to convert
	 * @return The byte array representation
	 */
	public static byte[] inputStreamToBytes(InputStream is, IWriteListener wListener, int flags) {
		ByteArrayOutputStreamProgress bs = new ByteArrayOutputStreamProgress(wListener, flags);
		//ByteArrayOutputStream bs = new ByteArrayOutputStream();
		byte[] buff = new byte[1000];
		
		int status;
		try {
			while ((status = is.read(buff)) != -1) {
				bs.write(buff, 0, status);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] ret = bs.toByteArray();
		try {
			bs.close();
		} catch (IOException e) {
			// I DON'T CARE
			e.printStackTrace();
		}
		return ret;
	}
	
	/**
	 * Check whether the SD-Card (or similar) is available for write access
	 * @return True if available, false if not
	 */
	public static Boolean getExternalWriteable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Check whether the SD-Card (or similar) is available for read access
	 * @return True if read is available, false if not
	 */
	public static Boolean getExternalReadable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			return true;
		}
		return false;
	}

	/**
	 * Check whether the Camera is available
	 * @param ctxt A generic context to use
	 * @return True if the camera is available, false otherwise
	 */
	public static Boolean IsCameraAvailable(Context ctxt) {
		PackageManager packageManager = ctxt.getPackageManager();
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
		return list.size() > 0;
	}
	
	public static Boolean isVideoCameraAvailable(Context ctxt) {
		PackageManager packageManager = ctxt.getPackageManager();
		Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
		return list.size() > 0;
	}
	
	public static boolean checkCameraHardware(Context ctxt) {
		if (ctxt.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
			return true;
		} else {
			return false;
		}
	}
}
