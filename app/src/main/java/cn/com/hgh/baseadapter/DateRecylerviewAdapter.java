package cn.com.hgh.baseadapter;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.com.hgh.utils.AbViewUtil;

import com.xizhi.mezone.b.R;
import com.lianbi.mezone.b.bean.DateAndColor;

public class DateRecylerviewAdapter extends
		Adapter<DateRecylerviewAdapter.ViewHolder> {
	private ArrayList<DateAndColor> mDataset;
	private OnItemMonthClickListener listener;
	private Activity activity;
	private int numDay;
	private int clickPosition = -1;

	public int getNumDay() {
		return numDay;
	}

	public void setNumDay(int numDay) {
		this.numDay = numDay;
	}

	public int getClickPosition() {
		return clickPosition;
	}

	public void setOnItemClickListener(OnItemMonthClickListener listener) {
		this.listener = listener;
	}

	/**
	 * 设置回调监听
	 * 
	 * @param listener
	 */
	public interface OnItemMonthClickListener {
		void onItemMonthClickListener(View view, int position);
	}

	public void setClickPosition(int clickPosition) {
		this.clickPosition = clickPosition;
	}

	public DateRecylerviewAdapter(ArrayList<DateAndColor> arrayList,
			Activity activity) {
		mDataset = arrayList;
		this.activity = activity;
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {

		public TextView mTextView;

		public ViewHolder(View itemView) {
			super(itemView);

		}
	}

	@Override
	public int getItemCount() {
		return mDataset.size();
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, final int position) {
		holder.mTextView.setText(mDataset.get(position).getDay());
		if (position > numDay) {
			GradientDrawable drawable = (GradientDrawable) holder.mTextView
					.getBackground();
			drawable.setStroke((int)AbViewUtil.dip2px(activity,2), activity.getResources()
					.getColor(R.color.color_efefef), 0, 0);
			drawable.setColor(Color.parseColor("#fbfbfb"));
			holder.mTextView.setTextColor(Color.parseColor("#efefef"));
			holder.mTextView.setBackgroundDrawable(drawable);
		} else if (clickPosition == position) {
			GradientDrawable drawable = (GradientDrawable) holder.mTextView
					.getBackground();
			drawable.setStroke(2,
					activity.getResources().getColor(R.color.colores_news_01),
					0, 0);
			drawable.setColor(Color.parseColor("#ff3c25"));
			holder.mTextView.setTextColor(Color.parseColor("#ffffff"));
			holder.mTextView.setBackgroundDrawable(drawable);
		} else {
			GradientDrawable drawable = (GradientDrawable) holder.mTextView
					.getBackground();
			int fillColor = mDataset.get(position).getColorId();
			int textColor = mDataset.get(position).getTextcolor();
			drawable.setColor(fillColor);
			drawable.setStroke((int)AbViewUtil.dip2px(activity,2), holder.mTextView
					.getResources().getColor(R.color.color_efefef), 0, 0);
			holder.mTextView.setTextColor(textColor);
			holder.mTextView.setBackgroundDrawable(drawable);
		}
		holder.mTextView.setText(mDataset.get(position).getDay() + "");
		if (listener != null) {
			holder.mTextView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					listener.onItemMonthClickListener(v, position);
				}
			});
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = View
				.inflate(parent.getContext(), R.layout.item_month, null);
		ViewHolder holder = new ViewHolder(view);
		holder.mTextView = (TextView) view.findViewById(R.id.text_month);
		return holder;
	}

}
