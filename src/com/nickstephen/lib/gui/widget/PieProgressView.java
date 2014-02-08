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
package com.nickstephen.lib.gui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import com.nickstephen.lib.R;
import com.nickstephen.lib.anim.IOnAnimationCompletion;

/**
 * Class - PieProgressView
 * @author Nicholas Stephen (a.k.a. saltisgood)
 *
 */
public class PieProgressView extends View {
	private int mAnimationDurationMS = 2000;
	private boolean mAnimationIsRunning = false;
	private IOnAnimationCompletion mAnimationOnCompletion;
	private boolean mAnimationRunOnce = true;
	private int mAnimationTimeChunkMS = 40;
	private AnimationHandler mAnimHandler;
	private RectF mArea;
	private int mCurrentValue = 0;
	private int mMaxValue = 100;
	private Paint mPaint;
	private int mPaintColor = 0xFFFF0000;
	private float mStartAngle = 0f;
	
	/**
	 * Simple constructor to use when creating a view from code.
	 * @param context The Context the view is running in, through which it can access the current theme, resources, etc.
	 */
	public PieProgressView(Context context) {
		super(context);
	}

	/**
	 * Constructor that is called when inflating a view from XML. This is called when a view is being constructed from 
	 * an XML file, supplying attributes that were specified in the XML file.
	 * @param context The Context the view is running in, through which it can access the current theme, resources, etc.
	 * @param attrs The attributes of the XML tag that is inflating the view.
	 */
	public PieProgressView(Context context, AttributeSet attrs) {
		super(context, attrs);
		parseAttributes(context, attrs);
	}
	
	/**
	 * Perform inflation from XML and apply a class-specific base style. This constructor of View allows subclasses to 
	 * use their own base style when they are inflating. Probably shouldn't be used lightly.
	 * @param context The Context the view is running in, through which it can access the current theme, resources, etc.
	 * @param attrs The attributes of the XML tag that is inflating the view.
	 * @param defStyleAttr An attribute in the current theme that contains a reference to a style resource to apply to 
	 * this view. If 0, no default style will be applied.
	 */
	public PieProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		parseAttributes(context, attrs);
	}
	
	/**
	 * @return the duration of the animation loop (in ms)
	 */
	public int getAnimationDurationMS() {
		return mAnimationDurationMS;
	}
	
	/**
	 * @return the time between animation updates (in ms)
	 */
	public int getAnimationTimeChunkMS() {
		return mAnimationTimeChunkMS;
	}

	/**
	 * @return the max progress value (for when setting manually)
	 */
	public int getMaxValue() {
		return mMaxValue;
	}

	/**
	 * @return the colour being used to paint the pie
	 */
	public int getPaintColor() {
		return mPaintColor;
	}

	/**
	 * @return the orientation of the pie
	 */
	public float getOrientationAngle() {
		return mStartAngle;
	}

	/**
	 * @return the current progress value
	 */
	public int getProgress() {
		return mCurrentValue;
	}

	/**
	 * @return whether the animation is currently running
	 */
	public boolean isAnimationRunning() {
		return mAnimationIsRunning;
	}

	/**
	 * @return whether the animation is set to only run once
	 */
	public boolean isAnimationRunOnce() {
		return mAnimationRunOnce;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		final float drawTo = mStartAngle + (((float)mCurrentValue / (float)mMaxValue) * 360f);
		
		if (mArea == null) {
			mArea = new RectF(0, 0, this.getWidth(), this.getHeight());
		}
		if (mPaint == null) {
			mPaint = new Paint();
		}
		
		canvas.rotate(-90f, mArea.centerX(), mArea.centerY());
		canvas.drawArc(mArea, mStartAngle, drawTo, true, mPaint);
	}

	/**
	 * 
	 * @param context
	 * @param attrs
	 */
	private void parseAttributes(Context context, AttributeSet attrs) {
		TypedArray values = context.obtainStyledAttributes(attrs, R.styleable.PieProgressView);
		
		mAnimationDurationMS = values.getInt(R.styleable.PieProgressView_anim_dur_ms, 2000);
		mAnimationRunOnce = values.getBoolean(R.styleable.PieProgressView_anim_runOnce, true);
		boolean shouldStart = values.getBoolean(R.styleable.PieProgressView_anim_start, false);
		mAnimationTimeChunkMS = values.getInt(R.styleable.PieProgressView_anim_update_ms, 40);
		mCurrentValue = values.getInt(R.styleable.PieProgressView_progress_startValue, 0);
		mMaxValue = values.getInt(R.styleable.PieProgressView_progress_maxValue, 100);
		mPaintColor = values.getColor(R.styleable.PieProgressView_pie_color, 0xFFFF0000);
		mStartAngle = values.getFloat(R.styleable.PieProgressView_pie_orientation, 0f) % 360f;
		
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
		mPaint.setColor(mPaintColor);
		
		if (shouldStart) {
			startAnimation();
		}
	}

	/**
	 * Set the new duration of the animation loop (in ms)
	 */
	public void setAnimationDurationMS(int animationDurationMS) {
		mAnimationDurationMS = animationDurationMS;
	}
	
	/**
	 * Set (or remove) the callback to be used when the animation finishes
	 */
	public void setAnimationOnCompletionListener(IOnAnimationCompletion callback) {
		mAnimationOnCompletion = callback;
	}

	/**
	 * Set whether the animation runs once or loops
	 */
	public void setAnimationRunOnce(boolean animationRunOnce) {
		mAnimationRunOnce = animationRunOnce;
	}

	/**
	 * Set the new duration between animation updates (in ms)
	 */
	public void setAnimationTimeChunkMS(int animationTimeChunkMS) {
		mAnimationTimeChunkMS = animationTimeChunkMS;
	}

	/**
	 * Set the new maximum value of the progress (normally only used for manually progressing)
	 */
	public void setMaxValue(int val) {
		mMaxValue = val;
		this.invalidate();
	}
	
	/**
	 * Set the new colour to be used when drawing the pie 
	 */
	public void setPieColor(int paintColor) {
		mPaintColor = paintColor;
		mPaint.setColor(paintColor);
	}
	
	/**
	 * Set the new value of the orientation of the pie
	 */
	public void setOrientationAngle(float oriAngle) {
		mStartAngle = oriAngle;
	}
	
	/**
	 * Set the new progress of the pie
	 */
	public void setProgress(int val) {
		mCurrentValue = val;
		this.invalidate();
	}
	
	/**
	 * Reset and start the pie animation with the values as currently set
	 */
	public void startAnimation() {
		if (!mAnimationIsRunning) {
			if (mAnimHandler != null) {
				mAnimHandler.removeMessages(AnimationHandler.DRAW);
				mAnimHandler.removeMessages(AnimationHandler.INCREMENT_AND_DRAW);
			} else {
				mAnimHandler = new AnimationHandler();
			}
			mAnimHandler.sendEmptyMessage(AnimationHandler.INCREMENT_AND_DRAW);
			mAnimationIsRunning = true;
		} else {
			mCurrentValue = 0;
		}
	}
	
	/**
	 * Stop the pie animation immediately or at the end of the current loop.
	 * @param endAtLoop Set to true to gracefully end at the end of the current loop, false to immediately stop
	 */
	public void stopAnimation(boolean endAtLoop) {
		if (mAnimationIsRunning) {
			if (endAtLoop) {
				mAnimationRunOnce = true;
			} else {
				mAnimHandler.removeMessages(AnimationHandler.DRAW);
				mAnimHandler.removeMessages(AnimationHandler.INCREMENT_AND_DRAW);
				mAnimationIsRunning = false;
				if (mAnimationOnCompletion != null) { 
					mAnimationOnCompletion.onAnimationCompletion(this);
				}
                mCurrentValue = 0;
			}
		}
	}
	
	protected class AnimationHandler extends Handler {
		public static final int DRAW = 0x10;
		public static final int INCREMENT_AND_DRAW = 0x100;
		
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case INCREMENT_AND_DRAW:
					this.sendEmptyMessageDelayed(INCREMENT_AND_DRAW, mAnimationTimeChunkMS);
					int nextValue = mCurrentValue + (int)(((float) mAnimationTimeChunkMS / (float) mAnimationDurationMS) * mMaxValue); 
					if (nextValue >= mMaxValue) {
						if (mAnimationRunOnce) {
							nextValue = 0;
							this.removeMessages(INCREMENT_AND_DRAW);
							mAnimationIsRunning = false;
							if (mAnimationOnCompletion != null) {
								mAnimationOnCompletion.onAnimationCompletion(PieProgressView.this);
							}
						} else {
							nextValue %= mMaxValue;
						}
					}
					mCurrentValue = nextValue;
				case DRAW:
					PieProgressView.this.invalidate();
					break;
			}
		}
	}
}
