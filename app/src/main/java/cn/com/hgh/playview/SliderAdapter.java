package cn.com.hgh.playview;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * A slider adapter
 */
public class SliderAdapter extends PagerAdapter {

	private final Context mContext;
	private ArrayList<BaseSliderView> mImageContents;

	public void setmImageContents(ArrayList<BaseSliderView> mImageContents) {
		this.mImageContents.clear();
		this.mImageContents.addAll(mImageContents);
		notifyDataSetChanged();
	}

	public SliderAdapter(Context context) {
		mContext = context;
		mImageContents = new ArrayList<BaseSliderView>();
	}

	public ArrayList<BaseSliderView> getmImageContents() {
		return mImageContents;
	}

	public <T extends BaseSliderView> void addSlider(T slider) {
		mImageContents.add(slider);
		notifyDataSetChanged();
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	public <T extends BaseSliderView> void removeSlider(T slider) {
		if (mImageContents.contains(slider)) {
			mImageContents.remove(slider);
			notifyDataSetChanged();
		}
	}

	public void removeSliderAt(int position) {
		if (mImageContents.size() < position) {
			mImageContents.remove(position);
			notifyDataSetChanged();
		}
	}

	public void removeAllSliders() {
		mImageContents.clear();
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mImageContents.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		BaseSliderView b = mImageContents.get(position);
		View v = b.getView();
		container.addView(v);
		return v;
	}

}
