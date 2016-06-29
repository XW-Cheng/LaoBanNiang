package cn.com.hgh.playview.imp;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import cn.com.hgh.playview.BaseSliderView;

import com.bumptech.glide.Glide;
import com.xizhi.mezone.b.R;

/**
 * 具体布局
 */
public class TextSliderView extends BaseSliderView {
	public Context mContext;

	public TextSliderView(Context context, int i) {
		super(context, i);
		mContext = context;
	}

	@Override
	public View getView() {
		ImageView v = new ImageView(mContext);
		v.setScaleType(ScaleType.FIT_XY);
		Glide.with(mContext).load(getUrl()).error(R.mipmap.adshouye).into(v);
		bindClickEvent(v);
		return v;
	}
}
