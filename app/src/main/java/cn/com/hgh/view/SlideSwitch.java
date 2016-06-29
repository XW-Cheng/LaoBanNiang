/*
 * Copyright (C) 2015 Quinn Chen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.com.hgh.view;

import cn.com.hgh.utils.AbViewUtil;

import com.xizhi.mezone.b.R;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class SlideSwitch extends View {

	public static final int SHAPE_RECT = 1;
	public static final int SHAPE_CIRCLE = 2;
	private static final int RIM_SIZE = 6;
	private static final int DEFAULT_COLOR_THEME = Color
			.parseColor("#ff00ee00");
	// 3 attributes
	private int color_theme;
	private boolean isOpen;
	private int shape;
	// varials of drawing
	private Paint paint;
	private Rect backRect;
	private Rect frontRect;
	private RectF frontCircleRect;
	private RectF backCircleRect;
	private int alpha;
	private int alpha1;
	private int max_left;
	private int min_left;
	private int frontRect_left;
	private int frontRect_left_begin = RIM_SIZE;
	private int eventStartX;
	private int eventLastX;
	private int diffX = 0;
	private boolean slideable = true;
	private SlideListener listener;
	private TextView textView;
	private Context context;
	EasyDialog ers;
	boolean toRight;
	int width;
	public interface SlideListener {
		public void open();
		public void setUpDate();
		public void close();
	}

	public SlideSwitch(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		listener = null;
		paint = new Paint();
		paint.setAntiAlias(true);
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.slideswitch);
		color_theme = a.getColor(R.styleable.slideswitch_themeColor,
				DEFAULT_COLOR_THEME);
		isOpen = a.getBoolean(R.styleable.slideswitch_isOpen, false);
		shape = a.getInt(R.styleable.slideswitch_shape1, SHAPE_RECT);
		a.recycle();

	}

	public SlideSwitch(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SlideSwitch(Context context) {
		this(context, null);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int width = measureDimension(280, widthMeasureSpec);
		int height = measureDimension(140, heightMeasureSpec);
		if (shape == SHAPE_CIRCLE) {
			if (width < height)
				width = height * 2;
		}
		setMeasuredDimension(width, height);
		initDrawingVal();
	}

	public void initDrawingVal() {
		width = getMeasuredWidth();
		int height = getMeasuredHeight();

		backCircleRect = new RectF();
		frontCircleRect = new RectF();
		frontRect = new Rect();

		backRect = new Rect(0, 0, width, height);
		min_left = RIM_SIZE;
		max_left = width / 2;
		if (isOpen) {
			frontRect_left = max_left;
			alpha = 255;
			alpha1 = 0;
		} else {
			frontRect_left = RIM_SIZE;
			alpha = 0;
			alpha1 = 255;
		}
		frontRect_left_begin = frontRect_left;
	}

	public int measureDimension(int defaultSize, int measureSpec) {
		int result;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);
		if (specMode == MeasureSpec.EXACTLY) {
			result = specSize;
		} else {
			result = defaultSize; // UNSPECIFIED
			if (specMode == MeasureSpec.AT_MOST) {
				result = Math.min(result, specSize);
			}
		}
		return result;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (shape == SHAPE_RECT) {
//			paint.setColor(Color.GRAY);
			paint.setColor(0xffededed);//背景色
			canvas.drawRect(backRect, paint);
			paint.setColor(color_theme);
			paint.setAlpha(alpha);
			canvas.drawRect(backRect, paint);
			frontRect.set(frontRect_left, RIM_SIZE, frontRect_left
					+ getMeasuredWidth() / 2 - RIM_SIZE, getMeasuredHeight()
					- RIM_SIZE);
			paint.setColor(Color.WHITE);
			canvas.drawRect(frontRect, paint);
		} else {
			int radius;
			radius = backRect.height() / 2 - RIM_SIZE;
//			paint.setColor(Color.GRAY);//背景色
			paint.setColor(0xffededed);//背景色

			backCircleRect.set(backRect);
			canvas.drawRoundRect(backCircleRect, radius, radius, paint);
			paint.setColor(Color.BLACK);
			paint.setTextSize(AbViewUtil.sp2px(getContext(), 15));
//          paint.setAlpha(alpha);
			frontRect.set(frontRect_left, RIM_SIZE, frontRect_left
					+ getMeasuredWidth() / 2 - RIM_SIZE, getMeasuredHeight()
					- RIM_SIZE);

			frontCircleRect.set(frontRect);
			paint.setColor(Color.WHITE);
			canvas.drawRoundRect(frontCircleRect, radius, radius, paint);
			if (isOpen) {

//				canvas.drawText("营业中", radius, radius + 23, paint);//没被选中
				paint.setColor(Color.BLACK);
				paint.setTextAlign(Paint.Align.CENTER);  
				canvas.drawText("营业中", width/4-RIM_SIZE, radius + 23, paint);//没被选中
				
			} else {
//				canvas.drawText("休息中", getMeasuredWidth() - radius - 40 * 3,radius + 23, paint);
				paint.setColor(Color.BLACK);
				paint.setTextAlign(Paint.Align.CENTER);  
				canvas.drawText("休息中", width-frontCircleRect.width()/2-RIM_SIZE,radius + 23, paint);		
			}

			if (isOpen) {
				paint.setColor(0xff14c4c4);
				paint.setTextAlign(Paint.Align.CENTER);  
				paint.getTextBounds("休息中", 0, "休息中".length(), frontRect);
				paint.setTextSize(AbViewUtil.sp2px(getContext(), 15));
//              paint.setAlpha(alpha1);
//				canvas.drawText("休息中", getMeasuredWidth() - radius - 40 * 3,radius + 23, paint);
				canvas.drawText("休息中", frontCircleRect.centerX(),radius + 23, paint);

			} else {

				paint.setColor(0xff14c4c4);
				paint.setTextAlign(Paint.Align.CENTER);  
				paint.getTextBounds("营业中", 0, "营业中".length(), frontRect);
				paint.setTextSize(AbViewUtil.sp2px(getContext(), 15));
//				paint.setAlpha(alpha1);
//				canvas.drawText("营业中", radius, radius + 23, paint);
				canvas.drawText("营业中",frontCircleRect.centerX(), radius + 23, paint);

			}

		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (slideable == false)
			return super.onTouchEvent(event);
		int action = MotionEventCompat.getActionMasked(event);
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			eventStartX = (int) event.getRawX();
			break;
		case MotionEvent.ACTION_MOVE:
//			eventLastX = (int) event.getRawX();
//			diffX = eventLastX - eventStartX;
//			int tempX = diffX + frontRect_left_begin;
//			tempX = (tempX > max_left ? max_left : tempX);
//			tempX = (tempX < min_left ? min_left : tempX);
//			if (tempX >= min_left && tempX <= max_left) {
//				frontRect_left = tempX;
//				alpha = (int) (255 * (float) tempX / (float) max_left);
//				invalidateView();
//			}
			break;
		case MotionEvent.ACTION_UP:
			int wholeX = (int) (event.getRawX() - eventStartX);
			frontRect_left_begin = frontRect_left;
			toRight = (frontRect_left_begin > max_left / 2 ? true : false);
			if (Math.abs(wholeX) < 3) {
				toRight = !toRight;
			}
			//得到控件本身点击时x坐标
			float eventx=event.getX();
			if (isOpen) {
				//打开状态
				if(eventx<width/2){		
//				switchBusinessStatus();
				switchStatus();

				}
			}else
			{
				//关闭状态
				if(eventx>width/2){		
//					switchBusinessStatus();
					switchStatus();

				}
			}
			break;
		default:
			break;
		}
		return true;
	}

	/**
	 * draw again
	 */
	private void invalidateView() {
		if (Looper.getMainLooper() == Looper.myLooper()) {
			invalidate();
		} else {
			postInvalidate();
		}
	}

	public void setSlideListener(SlideListener listener) {
		this.listener = listener;
	}

	public void moveToDest(final boolean toRight) {
		ValueAnimator toDestAnim = ValueAnimator.ofInt(frontRect_left,
				toRight ? max_left : min_left);
		toDestAnim.setDuration(500);
		toDestAnim.setInterpolator(new AccelerateDecelerateInterpolator());
//		toDestAnim.start();
		toDestAnim.addUpdateListener(new AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
//				frontRect_left = (Integer) animation.getAnimatedValue();
//				alpha = (int) (255 * (float) frontRect_left / (float) max_left);
//				invalidateView();
			}
		});
		toDestAnim.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				if (toRight) {
					isOpen = true;
					if (listener != null)
						listener.open();
					frontRect_left_begin = max_left;
				} else {
					isOpen = false;
					if (listener != null)
						listener.close();
					frontRect_left_begin = min_left;
				}

			}
		});
	}

	public void setState(boolean isOpen) {
		this.isOpen = isOpen;
		initDrawingVal();
		invalidateView();
		if (listener != null)
			if (isOpen == true) {
				listener.open();
			} else {
				listener.close();
			}
	}
	public void setContext(Context context) {
		this.context = context;
	}
	public void setShapeType(int shapeType) {
		this.shape = shapeType;
	}

	public void setSlideable(boolean slideable) {
		this.slideable = slideable;
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		if (state instanceof Bundle) {
			Bundle bundle = (Bundle) state;
			this.isOpen = bundle.getBoolean("isOpen");
			state = bundle.getParcelable("instanceState");
		}
		super.onRestoreInstanceState(state);
	}

	@Override
	protected Parcelable onSaveInstanceState() {
		Bundle bundle = new Bundle();
		bundle.putParcelable("instanceState", super.onSaveInstanceState());
		bundle.putBoolean("isOpen", this.isOpen);
		return bundle;
	}
	protected void  switchStatus(){
		DialogCommon dialogCommon = new DialogCommon(
				context) {

			@Override
			public void onCheckClick() {
				dismiss();
			}

			@Override
			public void onOkClick() {
				listener.setUpDate();
				moveToDest(toRight);
				dismiss();
			}

		};
		dialogCommon.setTextTitle("确定切换营业状态？");
		dialogCommon.setTv_dialog_common_ok("确定");
		dialogCommon.setTv_dialog_common_cancel("取消");
		dialogCommon.show();
		
	}
	protected void switchBusinessStatus() {
		View layout = LayoutInflater.from(context).inflate(
				R.layout.dialog_delete, null);
		TextView tv_dialogtext = (TextView) layout
				.findViewById(R.id.tv_dialogtext);
		TextView tv_delete_cancle = (TextView) layout
				.findViewById(R.id.tv_delete_cancle);
		TextView tv_delete_ok = (TextView) layout
				.findViewById(R.id.tv_delete_ok);
		tv_dialogtext.setText("确定切换营业状态？");
		ers = showDioag(this, layout);
		tv_delete_cancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
			
				ers.dismiss();
			}
		});
		tv_delete_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				listener.setUpDate();
				moveToDest(toRight);
				ers.dismiss();
			}
		});

	}
	private EasyDialog showDioag(View arg1, View layout) {
		EasyDialog es = new EasyDialog(context)
				.setLayout(layout)
				.setBackgroundColor(
						context.getResources().getColor(
								R.color.white))
				.setLocationByAttachedView(arg1)
				.setAnimationTranslationShow(EasyDialog.DIRECTION_Y, 1000,
						-AbViewUtil.dip2px(context, 1000), 100, -50, 50, 0)
				.setAnimationTranslationDismiss(EasyDialog.DIRECTION_Y, 500, 0,
						-AbViewUtil.dip2px(context, 1000))
				.setGravity(EasyDialog.GRAVITY_TOP)
				.setTouchOutsideDismiss(true)
				.setMatchParent(false)
				.setMarginLeftAndRight(24, 24)
				.setOutsideColor(
						context.getResources().getColor(
								R.color.color_8000)).show();
		return es;

	}
}
