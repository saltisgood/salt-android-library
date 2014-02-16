/**
 * The Stephen Android Library
 *
 * Author: Nicholas Stephen (a.k.a. saltisgood) - salt_is_good@hotmail.com
 *
 * For general use in all of my Android projects and available for use
 * by anyone else given recognition and leaving this header on all 
 * source files. Feel free to use, distribute and modify these files
 * and let me know if you find them useful. :)
 */
package com.nickstephen.lib.anim;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

/**
 * Class - OrientationListener
 * @author Nicholas Stephen (a.k.a. saltisgood)
 *
 */
public class OrientationListener extends OrientationEventListener {
	protected final int LAND_DOWN = 3;
	protected final int LAND_UP = 1;
	protected final int PORTRAIT_DOWN = 2;
	protected final int PORTRAIT_UP = 0;
	protected final int THRESHOLD = 15;
	
	protected int mCurrentOrientation = PORTRAIT_UP;
	protected List<View> mViews;
	
	private int mAnimationDurationMS = 250;
	private boolean mIsRunning = true;
	
	public OrientationListener(Context context) {
		super(context);
		
		mViews = new ArrayList<View>();
	}
	
	public OrientationListener(Context context, int rate) {
		super(context, rate);
	}
	
	public OrientationListener(Context context, int rate, List<View> views) {
		super(context, rate);
		
		if (views == null) {
			mViews = new ArrayList<View>();
		} else {
			mViews = views;
		}
	}
	
	/**
	 * @param context
	 */
	public OrientationListener(Context context, List<View> views) {
		super(context);
		
		if (views == null) {
			mViews = new ArrayList<View>();
		} else {
			mViews = views;
		}
	}

	public void addViewToAnimate(View view) {
		if (mViews != null) {
			mViews.add(view);
		} else {
			mViews = new ArrayList<View>();
			mViews.add(view);
		}
	}
	
	@Override
	public void disable() {
		super.disable();
		
		mIsRunning = false;
	}
	
	@Override
	public void enable() {
		super.enable();
		
		mIsRunning = true;
	}
	
	private boolean isLandscape(int orientation) {
		return Math.abs(orientation - 270) <= THRESHOLD;
	}
	
	private boolean isLandscapeDown(int orientation) {
		return Math.abs(orientation - 90) <= THRESHOLD;
	}
	
	private boolean isPortrait(int orientation) {
		return Math.abs(orientation % 360) <= THRESHOLD;
	}
	
	private boolean isPortraitDown(int orientation) {
		return Math.abs(orientation - 180) <= THRESHOLD;
	}
	
	/**
	 * Handle the config change events. 
	 * @param rotationStart The rotation to start from
	 * @param rotationEnd The rotation to end at
	 */
	private void onConfigChange(float rotationStart, float rotationEnd) {
		if (mIsRunning && mViews != null) {
			for (View view : mViews) {
				Animation anim = new RotateAnimation(rotationStart, rotationEnd, (float) view.getWidth() / 2, (float) view.getHeight() / 2);
				anim.setDuration(mAnimationDurationMS);
				anim.setFillAfter(true);
				anim.setRepeatCount(0);
				view.startAnimation(anim);
			}
		}
	}
	
	/** (non-Javadoc)
	 * @see android.view.OrientationEventListener#onOrientationChanged(int)
	 */
	@Override
	public void onOrientationChanged(int orientation) {
		if (orientation == OrientationEventListener.ORIENTATION_UNKNOWN)
			return;
		else if (isLandscape(orientation)) {
			switch (mCurrentOrientation) {
				case PORTRAIT_UP:
					onConfigChange(0.0f, 90.0f);
					break;
				case PORTRAIT_DOWN:
					onConfigChange(180.0f, 90.0f);
					break;
				case LAND_DOWN:
					onConfigChange(-90.0f, 90.0f);
					break;
			}
			mCurrentOrientation = LAND_UP;
		} else if (isPortrait(orientation)) {
			switch (mCurrentOrientation) {
				case LAND_UP:
					onConfigChange(90.0f, 0.0f);
					break;
				case LAND_DOWN:
					onConfigChange(-90.0f, 0.0f);
					break;
				case PORTRAIT_DOWN:
					onConfigChange(180.0f, 0.0f);
					break;
			}
			mCurrentOrientation = PORTRAIT_UP;
		} else if (isLandscapeDown(orientation)) {
			switch (mCurrentOrientation) {
				case PORTRAIT_UP:
					onConfigChange(0.0f, -90.0f);
					break;
				case PORTRAIT_DOWN:
					onConfigChange(-180.0f, -90.0f);
					break;
				case LAND_UP:
					onConfigChange(90.0f, -90.0f);
					break;
			}
			mCurrentOrientation = LAND_DOWN;
		} else if (isPortraitDown(orientation) && mCurrentOrientation != PORTRAIT_DOWN) {
			switch (mCurrentOrientation) {
				case LAND_UP:
					onConfigChange(90.0f, 180.0f);
					break;
				case LAND_DOWN:
					onConfigChange(-90.0f, -180.0f);
					break;
				case PORTRAIT_UP:
					onConfigChange(0.0f, 180.0f);
					break;
			}
			mCurrentOrientation = PORTRAIT_DOWN;
		}
	}
	
	/**
	 * Temporarily pause the orientation listener. This doesn't unregister the base OrientationEventListener
	 * but instead just ignores any orientation changes until it's resumed. So I guess it's meant for 
	 * temporary pausing and you don't want to actually quit it. Call {@link #resume} to resume event
	 * processing.
	 */
	public void pause() {
		mIsRunning = false;
	}

	/**
	 * Resume doing orientation change events after pausing. This should only be called if you've previously
	 * called {@link #pause()} rather than {@link #disable}.
	 */
	public void resume() {
		mIsRunning = true;
	}
	
	public void setViewsToAnimate(List<View> views) {
		mViews = views;
	}
}
