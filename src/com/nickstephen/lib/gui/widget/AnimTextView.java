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

import java.util.Random;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import com.nickstephen.lib.R;

/**
 * Class - AnimTextView
 * @author Nicholas Stephen (a.k.a. saltisgood)
 *
 */
public class AnimTextView extends View {
	private int mAnimationDurationMS = 2000;
	private int mAnimationWaitDurationMS = 5000;
	private AnimationHandler mAnimationHandler;
	private boolean mShuffleText = true;
	private boolean mAnimationIsRunning = false;
	private int mAnimationTimeChunkMS = 40;
	private CharSequence[] mTextVals;
	private int mTextColour = 0xFF000000;
	private Rect mSharedRect = new Rect();
	
	private float mTextOffsetX;
	private Paint mTextPaint1;
	private int mTextValIndex1 = 0;
	private int mTextAlpha1 = 255;
	private Paint mTextPaint2;
	private int mTextValIndex2 = 1;
	private int mTextAlpha2 = 0;
	
	private float mTextMaxTop;
	
	/**
	 * Simple constructor to use when creating a view from code.
	 * @param context The Context the view is running in, through which it can access the current theme, resources, etc.
	 */
	public AnimTextView(Context context) {
		super(context);
		
		mTextPaint1 = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DEV_KERN_TEXT_FLAG);
		mTextPaint1.setTextAlign(Align.CENTER);
		mTextPaint1.setAlpha(mTextAlpha1);
		mTextPaint1.setTextSize(50.0f);
		mTextMaxTop = -mTextPaint1.getFontMetrics().top;
		
		mTextPaint2 = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DEV_KERN_TEXT_FLAG);
		mTextPaint2.setTextAlign(Align.CENTER);
		mTextPaint2.setAlpha(mTextAlpha2);
	}

	/**
	 * Constructor that is called when inflating a view from XML. This is called when a view is being constructed from 
	 * an XML file, supplying attributes that were specified in the XML file.
	 * @param context The Context the view is running in, through which it can access the current theme, resources, etc.
	 * @param attrs The attributes of the XML tag that is inflating the view.
	 */
	public AnimTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		mTextPaint1 = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DEV_KERN_TEXT_FLAG);
		mTextPaint1.setTextAlign(Align.CENTER);
		mTextPaint1.setAlpha(mTextAlpha1);
		
		mTextPaint2 = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DEV_KERN_TEXT_FLAG);
		mTextPaint2.setTextAlign(Align.CENTER);
		mTextPaint2.setAlpha(mTextAlpha2);
		
		parseAttributes(context, attrs);
		
		mTextMaxTop = -mTextPaint1.getFontMetrics().top;
		if (mShuffleText) {
			Random r = new Random();
			mTextValIndex2 = r.nextInt(mTextVals.length - 1) + 1;
		}
	}
	
	/**
	 * Perform inflation from XML and apply a class-specific base style. This constructor of View allows subclasses to 
	 * use their own base style when they are inflating. Probably shouldn't be used lightly.
	 * @param context The Context the view is running in, through which it can access the current theme, resources, etc.
	 * @param attrs The attributes of the XML tag that is inflating the view.
	 * @param defStyleAttr An attribute in the current theme that contains a reference to a style resource to apply to 
	 * this view. If 0, no default style will be applied.
	 */
	public AnimTextView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		
		mTextPaint1 = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DEV_KERN_TEXT_FLAG);
		mTextPaint1.setTextAlign(Align.CENTER);
		mTextPaint1.setAlpha(mTextAlpha1);
		
		mTextPaint2 = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DEV_KERN_TEXT_FLAG);
		mTextPaint2.setTextAlign(Align.CENTER);
		mTextPaint2.setAlpha(mTextAlpha2);
		
		parseAttributes(context, attrs);
		
		mTextMaxTop = -mTextPaint1.getFontMetrics().top;
	}
	
	public boolean isAnimationRunning() {
		return mAnimationIsRunning;
	}
	
	public void startAnimation(boolean immediate) {
		if (!mAnimationIsRunning) {
			if (mAnimationHandler != null) {
				mAnimationHandler.removeMessages(AnimationHandler.DRAW);
				mAnimationHandler.removeMessages(AnimationHandler.FADE_AND_DRAW);
			} else {
				mAnimationHandler = new AnimationHandler();
			}
			
			if (immediate) {
				mAnimationHandler.sendEmptyMessage(AnimationHandler.FADE_AND_DRAW);
			} else {
				mAnimationHandler.sendEmptyMessageDelayed(AnimationHandler.FADE_AND_DRAW, mAnimationWaitDurationMS);
			}
			mAnimationIsRunning = true;
		}
	}
	
	public void pauseAnimation() {
		if (!mAnimationIsRunning && mAnimationHandler != null) {
			mAnimationHandler.removeMessages(AnimationHandler.DRAW);
			mAnimationHandler.removeMessages(AnimationHandler.FADE_AND_DRAW);
		}
	}
	
	private void parseAttributes(Context context, AttributeSet attrs) {
		TypedArray values = context.obtainStyledAttributes(attrs, R.styleable.AnimTextView);
		
		mTextVals = values.getTextArray(R.styleable.AnimTextView_textArray);
		if (mTextVals != null && mTextVals.length == 1) {
			mTextVals = new String[] { mTextVals[0].toString(), mTextVals[0].toString() };
		} else if (mTextVals == null) {
			mTextVals = new String[] { "", "" };
		}
		
		mAnimationDurationMS = values.getInt(R.styleable.AnimTextView_anim_dur_ms, 2000);
		mAnimationWaitDurationMS = values.getInt(R.styleable.AnimTextView_anim_waitDur_ms, 5000);
		
		mShuffleText = values.getBoolean(R.styleable.AnimTextView_anim_shuffle, true);
		mTextColour = values.getColor(R.styleable.AnimTextView_android_textColor, 0xFF000000);
		mTextPaint1.setColor(mTextColour);
		mTextPaint2.setColor(mTextColour);
		
		float fontSize = values.getDimension(R.styleable.AnimTextView_android_textSize, 50.0f);
		mTextPaint1.setTextSize(fontSize);
		mTextPaint2.setTextSize(fontSize);
		
		if (values.getBoolean(R.styleable.AnimTextView_anim_start, true)) {
			startAnimation(true); 
		}
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width, height;
		
		mTextPaint1.getTextBounds(mTextVals[mTextValIndex1].toString(), 0, mTextVals[mTextValIndex1].length(), mSharedRect);
		int textMaxWidth = mSharedRect.width();
		
		mTextPaint2.getTextBounds(mTextVals[mTextValIndex2].toString(), 0, mTextVals[mTextValIndex2].length(), mSharedRect);
		
		textMaxWidth = Math.max(textMaxWidth, mSharedRect.width());
		mTextOffsetX = textMaxWidth / 2;
		
		switch (MeasureSpec.getMode(widthMeasureSpec)) {
			case MeasureSpec.EXACTLY:
				width = MeasureSpec.getSize(widthMeasureSpec);
				break;
			case MeasureSpec.AT_MOST:
				width = Math.min(MeasureSpec.getSize(widthMeasureSpec), textMaxWidth);
				break;
			case MeasureSpec.UNSPECIFIED:
			default:
				width = textMaxWidth;
				break;
		}
		
		switch (MeasureSpec.getMode(heightMeasureSpec)) {
			case MeasureSpec.EXACTLY:
				height = MeasureSpec.getSize(heightMeasureSpec);
				break;
			case MeasureSpec.AT_MOST:
				height = Math.min(MeasureSpec.getSize(heightMeasureSpec), (int) mTextMaxTop);
				break;
			case MeasureSpec.UNSPECIFIED:
			default:
				height = (int) mTextMaxTop;
				break;
		}
		
		this.setMeasuredDimension(width, height);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		/* Paint p = new Paint();
		p.setColor(0xFF00FF00);
		p.setStrokeWidth(5.0f);
		canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), p); */
		
		mTextPaint1.setAlpha(mTextAlpha1);
		mTextPaint2.setAlpha(mTextAlpha2);
		
		canvas.drawText(mTextVals[mTextValIndex1].toString(), mTextOffsetX, mTextMaxTop - mTextPaint1.descent(), mTextPaint1);
		canvas.drawText(mTextVals[mTextValIndex2].toString(), mTextOffsetX, mTextMaxTop - mTextPaint2.descent(), mTextPaint2);
	}
	
	protected class AnimationHandler extends Handler {
		public static final int DRAW = 0x10;
		public static final int FADE_AND_DRAW = 0x100;
		
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case FADE_AND_DRAW:
					int diff = (int) (((float) mAnimationTimeChunkMS / (float) mAnimationDurationMS) * 255);
					
					if (mTextAlpha1 == 255 && mTextVals.length > 1) {
						int prevIndex1 = mTextValIndex1, prevIndex2 = mTextValIndex2;
						
						//mTextAlpha1 = diff;
						//mTextAlpha2 = 255 - diff;
                        mTextAlpha1 = 0;
                        mTextAlpha2 = 255;
						mTextValIndex2 = prevIndex1;
						
						if (mShuffleText && mTextVals.length > 2) {
							Random r = new Random();
							for (int i = 0; i < 100; i++) {
								int ran = r.nextInt(mTextVals.length);
								if (ran != prevIndex1 && ran != prevIndex2) {
									mTextValIndex1 = ran;
									break;
								}
							}
						} else if (mTextVals.length == 2) {
							mTextValIndex1 = prevIndex2;
						} else {
							if (prevIndex1 == (mTextVals.length - 1)) {
								mTextValIndex1 = 0;
							} else {
								mTextValIndex1++;
							}
						}
						
						AnimTextView.this.requestLayout();
						this.sendEmptyMessageDelayed(FADE_AND_DRAW, mAnimationWaitDurationMS);
					} else {
						mTextAlpha1 = Math.min(mTextAlpha1 + diff, 255);
						mTextAlpha2 = Math.max(mTextAlpha2 - diff, 0);
						
						this.sendEmptyMessageDelayed(FADE_AND_DRAW, mAnimationTimeChunkMS);
					}
					
					mTextPaint1.setAlpha(mTextAlpha1);
					mTextPaint2.setAlpha(mTextAlpha2);
					
				case DRAW:
					AnimTextView.this.invalidate();
					break;
			}
		}
	}
}
