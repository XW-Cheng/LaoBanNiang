package cn.com.hgh.indexscortlist;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

import com.xizhi.mezone.b.R;

public class ClearEditText extends EditText implements OnFocusChangeListener,
		TextWatcher {
	/**
	 * 删除按钮的引用
	 */
	private Drawable mClearDrawable;

	public ClearEditText(Context context) {
		this(context, null);
	}

	public ClearEditText(Context context, AttributeSet attrs) {
		// 这里构造方法也很重要，不加这个很多属性不能再XML里面定义
		this(context, attrs, android.R.attr.editTextStyle);
	}

	public ClearEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		// 获取EditText的DrawableRight,假如没有设置我们就使用默认的图片
		mClearDrawable = getCompoundDrawables()[2];
		if (mClearDrawable == null) {
			mClearDrawable = getResources().getDrawable(
					R.mipmap.register_phone_delete);
		}
		mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(),
				mClearDrawable.getIntrinsicHeight());
		setClearIconVisible(false);
		setOnFocusChangeListener(this);
		addTextChangedListener(this);
	}

	/**
	 * 因为我们不能直接给EditText设置点击事件，所以我们用记住我们按下的位置来模拟点击事件 当我们按下的位置 在 EditText的宽度 -
	 * 图标到控件右边的间距 - 图标的宽度 和 EditText的宽度 - 图标到控件右边的间距之间我们就算点击了图标，竖直方向没有考虑
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (getCompoundDrawables()[2] != null) {
			if (event.getAction() == MotionEvent.ACTION_UP) {
				boolean touchable = event.getX() > (getWidth()
						- getPaddingRight() - mClearDrawable
							.getIntrinsicWidth())
						&& (event.getX() < ((getWidth() - getPaddingRight())));
				if (touchable) {
					this.setText("");
				}
			}
		}

		return super.onTouchEvent(event);
	}

	/**
	 * 当ClearEditText焦点发生变化的时候，判断里面字符串长度设置清除图标的显示与隐藏
	 */
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus) {
			setClearIconVisible(getText().length() > 0);
		} else {
			setClearIconVisible(false);
		}
	}

	/**
	 * 设置清除图标的显示与隐藏，调用setCompoundDrawables为EditText绘制上去
	 * 
	 * @param visible
	 */
	protected void setClearIconVisible(boolean visible) {
		Drawable right = visible ? mClearDrawable : null;
		setCompoundDrawables(getCompoundDrawables()[0],
				getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
	}

	/**
	 * 是否开启价格限制
	 */
	private boolean openPrice = false;

	public boolean isOpenPrice() {
		return openPrice;
	}

	public void setOpenPrice(boolean openPrice) {
		this.openPrice = openPrice;
	}

	/**
	 * 当输入框里面内容发生变化的时候回调的方法
	 */
	@Override
	public void onTextChanged(CharSequence s, int start, int count, int after) {
		setClearIconVisible(s.length() > 0);
		if (maxlenth > 0 && s.length() > maxlenth) {
			s = s.toString().subSequence(0, maxlenth);
			this.setText(s);
			this.setSelection(s.length());
		}
		if (openPrice) {

			if (s.toString().contains(".")) {
				if (s.length() - 1 - s.toString().indexOf(".") > 2) {
					s = s.toString().subSequence(0,
							s.toString().indexOf(".") + 3);
					this.setText(s);
					this.setSelection(s.length());
				}
			}
			if (s.toString().trim().substring(0).equals(".")) {
				s = "0" + s;
				this.setText(s);
				this.setSelection(2);
			}

			if (s.toString().startsWith("0")
					&& s.toString().trim().length() > 1) {
				if (!s.toString().substring(1, 2).equals(".")) {
					this.setText(s.subSequence(0, 1));
					this.setSelection(1);
					return;
				}
			}
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	int maxlenth = -1;

	public int getMaxlenth() {
		return maxlenth;
	}

	public void setMaxlenth(int maxlenth) {
		this.maxlenth = maxlenth;
	}

	@Override
	public void afterTextChanged(Editable s) {

	}

}
