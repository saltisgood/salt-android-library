package com.nickstephen.lib.misc;

import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

public class BitmapUtil {
	private BitmapUtil() {}
	
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		final int height = options.outHeight;
	    final int width = options.outWidth;
	    int inSampleSize = 1;

	    if (height > reqHeight || width > reqWidth) {

	        // Calculate ratios of height and width to requested height and width
	        final int heightRatio = Math.round((float) height / (float) reqHeight);
	        final int widthRatio = Math.round((float) width / (float) reqWidth);

	        // Choose the smallest ratio as inSampleSize value, this will guarantee
	        // a final image with both dimensions larger than or equal to the
	        // requested height and width.
	        inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
	    }

	    return inSampleSize;
	}
	
	public static Bitmap decodeBitmapFromFileToSize(String filePath, int reqWidth, int reqHeight, boolean maintainAspectRatio) {
		Bitmap original = BitmapFactory.decodeFile(filePath);
		if (original == null) {
			return null;
		}
		if (original.getWidth() == reqWidth || original.getHeight() == reqHeight) {
			return original;
		}
		
		if (maintainAspectRatio) {
			int oriWidth = original.getWidth(), oriHeight = original.getHeight();
			if (oriWidth >= oriHeight) {
				float aRatio = (float)oriHeight / (float)oriWidth;
				reqHeight = (int)(reqWidth * aRatio);
			} else if (oriHeight > oriWidth) {
				float aRatio = (float)oriWidth / (float)oriHeight;
				reqWidth = (int)(reqHeight * aRatio);
			}
		}
		
		Bitmap scaled = Bitmap.createScaledBitmap(original, reqWidth, reqHeight, false);
		if (scaled == null) {
			return original;
		}
		if (scaled != original) {
			original.recycle();
			original = null;
		}
		return scaled;
	}
	
	public static Bitmap decodeSampledBitmapFromFile(String filePath, int reqWidth, int reqHeight) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);
		
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		options.inJustDecodeBounds = false;
		
		return BitmapFactory.decodeFile(filePath, options);
	}
	
	public static Bitmap rotateBitmapFromExif(String filePath, Bitmap bitmap) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);
		
		String orientString;
		try {
			ExifInterface exif = new ExifInterface(filePath);
			orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
		} catch (IOException e) {
			e.printStackTrace();
			return bitmap;
		}
		
		int orientation = (orientString != null) ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;
		int rotationAngle = 0, outHeight = 0, outWidth = 0;
		switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				rotationAngle = 90;
				outHeight = bitmap.getWidth();
				outWidth = bitmap.getHeight();
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				rotationAngle = 180;
				outHeight = bitmap.getHeight();
				outWidth = bitmap.getWidth();
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				rotationAngle = 270;
				outHeight = bitmap.getWidth();
				outWidth = bitmap.getHeight();
				break;
		}
		
		if (rotationAngle == 0) {
			return bitmap;
		}
		
		Matrix matrix = new Matrix();
		matrix.setRotate(rotationAngle, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);
		Bitmap rotateBitmap = Bitmap.createBitmap(bitmap, 0, 0, outWidth, outHeight, matrix, true);
		if (bitmap != rotateBitmap) {
			bitmap.recycle();
		}
		
		return rotateBitmap;
	}
	
	public static Bitmap createScaledRotatedBitmapFromFile(String filePath, int reqWidth, int reqHeight, int orientation) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);
		
		Bitmap bitmap;
		int rotationAngle = 0, outHeight = 0, outWidth = 0;
		if (reqWidth >= reqHeight) {
			if (options.outWidth >= options.outHeight) {
				if (orientation != 1) {
					rotationAngle = 180;
				} else {
					
				}
				bitmap = decodeSampledBitmapFromFile(filePath, reqWidth, reqHeight);
			} else {
				bitmap = decodeSampledBitmapFromFile(filePath, reqHeight, reqWidth);
			}
			outHeight = bitmap.getWidth();
			outWidth = bitmap.getHeight();
		} else {
			if (options.outWidth > options.outHeight) {
				if (orientation != 1) {
					rotationAngle = 90;
				} else {
					rotationAngle = 270;
				}
				bitmap = decodeSampledBitmapFromFile(filePath, reqHeight, reqWidth);
			} else {
				bitmap = decodeSampledBitmapFromFile(filePath, reqWidth, reqHeight);
			}
			outHeight = bitmap.getWidth();
			outWidth = bitmap.getHeight();
		}
		
		if (rotationAngle == 0) {
			return bitmap;
		}
		
		Matrix matrix = new Matrix();
		matrix.setRotate(rotationAngle, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);
		Bitmap rotateBitmap = Bitmap.createBitmap(bitmap, 0, 0, outHeight, outWidth, matrix, true);
		if (bitmap != rotateBitmap) {
			bitmap.recycle();
		}
		
		return rotateBitmap;
	}
}
