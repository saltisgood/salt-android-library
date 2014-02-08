package com.nickstephen.lib.misc;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class FileIO {
	protected FileIO() {}
	
	/**
	 * Copy a file. Does not throw exceptions, instead check the return value.
	 * @param source The source file to copy
	 * @param destination The destination file to copy to
	 * @return 0 on success, < 0 for various exceptions
	 */
	public static int bufferedCopy(String source, String destination) {
		try {
			FileOutputStream fsout = new FileOutputStream(destination);
			FileInputStream fsin = new FileInputStream(source);
			byte[] buff = new byte[1024];
			int len;
			while ((len = fsin.read(buff)) > 0) {
				fsout.write(buff, 0, len);
			}
			fsout.close();
			fsin.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return -2;
		} catch (IOException e) {
			e.printStackTrace();
			return -3;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		
		return 0;
	}
	
	public static String getRealPathFromUri(Context context, Uri uri) {
		Cursor cursor = null;
		try { 
			String[] proj = { MediaStore.Images.Media.DATA };
			cursor = context.getContentResolver().query(uri, proj, null, null, null);
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}

    /**
     * A hacky fix to get the location of the last image saved to the gallery. Mainly for S3s.
     * @param context
     * @return
     */
    public static String getRealPathFromLastPic(Context context) {
        String[] fileProj = {
                MediaStore.Images.ImageColumns._ID, MediaStore.Images.ImageColumns.DATA
        };
        String fileSort = MediaStore.Images.ImageColumns._ID + " DESC";
        Cursor cursor = null;
        String imgPath = null;
        try {
            cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, fileProj, null, null, fileSort);
            cursor.moveToFirst();
            imgPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATA));
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return imgPath;
    }
}
