package com.nickstephen.lib.bgtasks;

import java.io.File;
import java.lang.ref.WeakReference;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

public class LazyThumbLoader extends AsyncTask<String, Void, Bitmap> {
	protected final WeakReference<ImageView> mImgViewRef;
	
	public LazyThumbLoader(ImageView imgView) {
		mImgViewRef = new WeakReference<ImageView>(imgView);
	}
	
	@Override
	protected Bitmap doInBackground(String... params) {
		File imgFile = new File(params[0]);
		if (!imgFile.exists()) {
			return null;
		} else {
			return BitmapFactory.decodeFile(params[0]);
		}
	}
	
	@Override
	protected void onPostExecute(Bitmap result) {
		ImageView imgView;
		if (result != null && (imgView = mImgViewRef.get()) != null) {
			imgView.setImageBitmap(result);
		}
	}
}
