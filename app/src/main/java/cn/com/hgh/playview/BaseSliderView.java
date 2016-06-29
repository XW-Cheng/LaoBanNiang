package cn.com.hgh.playview;

import java.io.File;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

/**
 * When you want to make your own slider view, you must extends from this class.
 * BaseSliderView provides some useful methods. Such loadImage, setImage,and so
 * on. I provide two example:
 * {@link com.example.androidimageslider.wedget.viewimage.SliderTypes.daimajia.slider.library.SliderTypes.DefaultSliderView}
 * and
 * {@link cn.com.hgh.playview.imp.example.androidimageslider.wedget.viewimage.SliderTypes.daimajia.slider.library.SliderTypes.TextSliderView}
 * if you want to show progressbar, you just need to set a progressbar id as
 * 
 * @+id/loading_bar.
 */
public abstract class BaseSliderView {

	protected Context mContext;

	private final Bundle mBundle;

	/**
	 * Error place holder image.
	 */
	private int mErrorPlaceHolderRes;

	/**
	 * Empty imageView placeholder.
	 */
	private int mEmptyPlaceHolderRes;

	private String mUrl;
	private File mFile;
	private int mRes;

	public int getmRes() {
		return mRes;
	}

	public void setmRes(int mRes) {
		this.mRes = mRes;
	}

	protected OnSliderClickListener mOnSliderClickListener;

	private boolean mErrorDisappear;

	private String mDescription;
	protected int p;

	protected BaseSliderView(Context context, int i) {
		mContext = context;
		p = i;
		this.mBundle = new Bundle();
	}

	public int getP() {
		return p;
	}

	public void setP(int p) {
		this.p = p;
	}

	/**
	 * the placeholder image when loading image from url or file.
	 * 
	 * @param resId
	 *            Image resource id
	 * @return
	 */
	public BaseSliderView empty(int resId) {
		mEmptyPlaceHolderRes = resId;
		return this;
	}

	/**
	 * determine whether remove the image which failed to download or load from
	 * file
	 * 
	 * @param disappear
	 * @return
	 */
	public BaseSliderView errorDisappear(boolean disappear) {
		mErrorDisappear = disappear;
		return this;
	}

	/**
	 * if you set errorDisappear false, this will set a error placeholder image.
	 * 
	 * @param resId
	 *            image resource id
	 * @return
	 */
	public BaseSliderView error(int resId) {
		mErrorPlaceHolderRes = resId;
		return this;
	}

	/**
	 * the description of a slider image.
	 * 
	 * @param description
	 * @return
	 */
	public BaseSliderView description(String description) {
		mDescription = description;
		return this;
	}

	/**
	 * set a url as a image that preparing to load
	 * 
	 * @param url
	 * @return
	 */
	public BaseSliderView image(String url) {
		if (mFile != null || mRes != 0) {
			throw new IllegalStateException("Call multi image function,"
					+ "you only have permission to call it once");
		}
		mUrl = url;
		return this;
	}

	/**
	 * set a file as a image that will to load
	 * 
	 * @param file
	 * @return
	 */
	public BaseSliderView image(File file) {
		if (mUrl != null || mRes != 0) {
			throw new IllegalStateException("Call multi image function,"
					+ "you only have permission to call it once");
		}
		mFile = file;
		return this;
	}

	public BaseSliderView image(int res) {
		if (mUrl != null || mFile != null) {
			throw new IllegalStateException("Call multi image function,"
					+ "you only have permission to call it once");
		}
		mRes = res;
		return this;
	}

	public String getUrl() {
		return mUrl;
	}

	public boolean isErrorDisappear() {
		return mErrorDisappear;
	}

	public int getEmpty() {
		return mEmptyPlaceHolderRes;
	}

	public int getError() {
		return mErrorPlaceHolderRes;
	}

	public String getDescription() {
		return mDescription;
	}

	public Context getContext() {
		return mContext;
	}

	/**
	 * set a slider image click listener
	 * 
	 * @param l
	 * @return
	 */
	public BaseSliderView setOnSliderClickListener(OnSliderClickListener l) {
		mOnSliderClickListener = l;
		return this;
	}

	/**
	 * when you want to extends this class, you must call this method to bind
	 * click event to your view.
	 * 
	 * @param v
	 */
	protected void bindClickEvent(View v) {
		final BaseSliderView me = this;
		v.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mOnSliderClickListener != null) {
					mOnSliderClickListener.onSliderClick(me);
				}
			}
		});
	}

	/**
	 * the extended class have to implement getView(), which is called by the
	 * adapter, every extended class response to render their own view.
	 * 
	 * @return
	 */
	public abstract View getView();

	public interface OnSliderClickListener {
		public void onSliderClick(BaseSliderView slider);
	}

	/**
	 * when you have some extra information, please put it in this bundle.
	 * 
	 * @return
	 */
	public Bundle getBundle() {
		return mBundle;
	}

	public interface ImageLoadListener {
		public void onStart(BaseSliderView target);

		public void onEnd(boolean result, BaseSliderView target);
	}

}
