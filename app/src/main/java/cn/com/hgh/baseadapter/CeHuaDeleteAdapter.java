package cn.com.hgh.baseadapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import cn.com.hgh.swipe.BaseSwipeAdapter;
import cn.com.hgh.utils.AbViewHolder;

import com.bumptech.glide.Glide;
import com.xizhi.mezone.b.R;
import com.lianbi.mezone.b.bean.MyProductBean;

public abstract class CeHuaDeleteAdapter extends BaseSwipeAdapter {

	Context mContext;
	LayoutInflater mInflater;

	public CeHuaDeleteAdapter(Context context, boolean isS) {
		mContext = context;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return getData() == null ? 0 : getData().size();
	}

	@Override
	public MyProductBean getItem(int position) {
		return getData().get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getSwipeLayoutResourceId(int position) {
		return R.id.swipes;
	}

	@Override
	public View generateView(int position, ViewGroup parent) {
		View view = mInflater
				.inflate(R.layout.item_setingzhuohaoactivity, null);
		return view;
	}

	@Override
	public void fillValues(final int position, View convertView) {
		TextView iv_lock = AbViewHolder.get(convertView, R.id.btn_dels);
		TextView item_setingzhuohaoactivity_tv_status = AbViewHolder.get(
				convertView, R.id.item_setingzhuohaoactivity_tv_status);
		iv_lock.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onItemLock(position);
			}
		});
		MyProductBean projectItems = getItem(position);
		TextView tv_name = AbViewHolder.get(convertView,
				R.id.item_setingzhuohaoactivity_tv_name);
		TextView tv_leis = AbViewHolder.get(convertView,
				R.id.item_setingzhuohaoactivity_tv_leis);
		TextView tv_price = AbViewHolder.get(convertView,
				R.id.tv_setingzhuohaoactivity_price);
		TextView tv_time = AbViewHolder.get(convertView,
				R.id.tv_setingzhuohaoactivity_time);
		ImageView iv_name = AbViewHolder.get(convertView,
				R.id.imageView_setingzhuohaoactivity_tv_name);
		ImageView iv_leixing = AbViewHolder.get(convertView,
				R.id.item_setingzhuohaoactivity_iv_lei);
		tv_name.setText(projectItems.getProduct_name());
		tv_leis.setText(projectItems.getIndustry_name());
		tv_time.setText(projectItems.getCreate_time());
		if (!TextUtils.isEmpty(projectItems.getPrice())) {
			tv_price.setText(projectItems.getPrice() + "元/"
					+ projectItems.getUnit());
		}
		Glide.with(mContext).load(projectItems.getIcon())
				.error(R.mipmap.defaultimg_11).into(iv_name);
		/**
		 * 产品类型服务类型 1：特殊服务，2：活动，3：优惠，4：团购，5：商品
		 */
		int type = projectItems.getProduct_type();
		switch (type) {
		case 1:
			iv_leixing.setImageResource(R.mipmap.p_zhuti_new_hgh);
			break;
		case 2:
			iv_leixing.setImageResource(R.mipmap.p_huodong_new_hgh);

			break;
		case 3:

			iv_leixing.setImageResource(R.mipmap.p_youhui_new_hgh);
			break;
		case 4:

			iv_leixing.setImageResource(R.mipmap.p_tuangou_new_hgh);
			break;
		case 5:

			iv_leixing.setImageResource(R.mipmap.p_shangpin_new_hgh);
			break;

		}

	}

	/**
	 * 关闭所有侧滑条目
	 * 
	 * @author shaowei.ma
	 * @date 2014年11月14日
	 */
	public void closeAllItems() {
		for (int i = 0, size = getCount(); i < size; i++) {
			closeItem(i);
		}
	}

	public abstract List<? extends MyProductBean> getData();

	public abstract void onItemLock(int p);
}
