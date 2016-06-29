package cn.com.hgh.playview.imp;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import cn.com.hgh.playview.BaseSliderView;
import cn.com.hgh.utils.AbViewUtil;

/**
 * 具体布局
 */
public class GuiderSliderView extends BaseSliderView {
	public Context mContext;

	public GuiderSliderView(Context context, int i) {
		super(context, i);
		mContext = context;
	}

	@Override
	public View getView() {
		ImageView v = new ImageView(mContext);
		v.setScaleType(ScaleType.CENTER_CROP);
		v.setImageBitmap(AbViewUtil.readBitMap(mContext, getmRes()));
		bindClickEvent(v);
		return v;
	}
}
