package cn.com.hgh.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import cn.com.hgh.utils.EditTextUtills;

import com.xizhi.mezone.b.R;

public class MyContainsViewJJ extends LinearLayout {
	EditText et_publish_goods_num1, et_publish_goods_num_unit1,
			et_publish_goods_price1, et_publish_goods_price_unit1;
	ImageView img_publish_goods_num1;
	/**
	 * 点击事件回调
	 */
	private ImOnclick imOnclick;
	/**
	 * 加减号
	 */
	boolean isPlus = false;

	UnitChange unitChange;
	protected String unit = "hgh";

	public UnitChange getUnitChange() {
		return unitChange;
	}

	public void setUnitChange(UnitChange unitChange) {
		this.unitChange = unitChange;
	}

	public interface UnitChange {
		void change(String unitC);
	}

	public ImOnclick getImOnclick() {
		return imOnclick;
	}

	public void setImOnclick(ImOnclick imOnclick) {
		this.imOnclick = imOnclick;
	}

	public String getEt_publish_goods_num1() {
		return et_publish_goods_num1.getText().toString().trim();
	}

	public void setEt_publish_goods_num1(EditText et_publish_goods_num1) {
		this.et_publish_goods_num1 = et_publish_goods_num1;
	}

	public String getEt_publish_goods_num_unit1() {
		return et_publish_goods_num_unit1.getText().toString().trim();
	}

	public void setEt_publish_goods_num_unit1(String et_publish_goods_num_unit1) {
		if (!TextUtils.isEmpty(unit)) {
			if (!unit.equals(et_publish_goods_num_unit1)) {
				this.et_publish_goods_num_unit1
						.setText(et_publish_goods_num_unit1);
				unit = et_publish_goods_num_unit1;

			}

		}
	}

	public String getEt_publish_goods_price1() {
		return et_publish_goods_price1.getText().toString().trim();
	}

	public void setEt_publish_goods_price1(EditText et_publish_goods_price1) {
		this.et_publish_goods_price1 = et_publish_goods_price1;
	}

	public String getEt_publish_goods_price_unit1() {
		return et_publish_goods_price_unit1.getText().toString().trim();
	}

	public void setEt_publish_goods_price_unit1(
			EditText et_publish_goods_price_unit1) {
		this.et_publish_goods_price_unit1 = et_publish_goods_price_unit1;
	}

	public MyContainsViewJJ(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);

	}

	public MyContainsViewJJ(Context context) {
		super(context);
		initView(context);
	}

	/**
	 * 减号
	 */
	public void setImg_publish_goods_num1J() {
		isPlus = true;
		img_publish_goods_num1
				.setImageResource(R.mipmap.publish_goodes_plusj);
	}

	/**
	 * 加好
	 */
	public void setImg_publish_goods_num1JJ() {
		isPlus = false;
		img_publish_goods_num1.setImageResource(R.mipmap.publish_goodes_plus);
	}

	private void initView(final Context context) {
		View v = View.inflate(context, R.layout.view_publish_goods, this);
		img_publish_goods_num1 = (ImageView) v
				.findViewById(R.id.img_publish_goods_num1);
		img_publish_goods_num1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (imOnclick != null) {
					if (!isPlus) {
						imOnclick.imOnclick(isPlus);
					} else {
						imOnclick.imOnclick(isPlus);
					}
				}
			}
		});
		et_publish_goods_num1 = (EditText) v
				.findViewById(R.id.et_publish_goods_num1);
		et_publish_goods_num_unit1 = (EditText) v
				.findViewById(R.id.et_publish_goods_num_unit1);

		et_publish_goods_num_unit1
				.setOnFocusChangeListener(new OnFocusChangeListener() {

					@Override
					public void onFocusChange(View arg0, boolean arg1) {
						if (!arg1) {
							if (unitChange != null) {
								String unit = et_publish_goods_num_unit1
										.getText().toString().trim();
								if (unit.length() > 0) {
									unitChange.change(unit);
								}
							}
						}
					}
				});
		et_publish_goods_price1 = (EditText) v
				.findViewById(R.id.et_publish_goods_price1);
		et_publish_goods_price_unit1 = (EditText) v
				.findViewById(R.id.et_publish_goods_price_unit1);
		EditTextUtills.setPricePoint(et_publish_goods_price1);
	}

	/**
	 * 清空数据
	 */
	public void clear() {
		et_publish_goods_num1.setText("");
		// et_publish_goods_num_unit1.setText("");
		et_publish_goods_price1.setText("");
		// et_publish_goods_price_unit1.setText("");
		et_publish_goods_num1.setHint("请输入数量");
		// et_publish_goods_num_unit1.setHint("单位");
		et_publish_goods_price1.setHint("请输入价格");
		// et_publish_goods_price_unit1.setHint("单位");
	}

	/**
	 * 设置数据
	 */
	public void setText(String num, String nunit, String p, String punit) {
		if (TextUtils.isEmpty(num)) {
			et_publish_goods_num1.setText("");
			et_publish_goods_num1.setHint("请输入数量");
		} else {
			et_publish_goods_num1.setText(num);
		}
		// if (TextUtils.isEmpty(nunit)) {
		// et_publish_goods_num_unit1.setText("");
		// et_publish_goods_num_unit1.setHint("单位");
		// } else {
		//
		// et_publish_goods_num_unit1.setText(nunit);
		// }
		if (TextUtils.isEmpty(p)) {
			et_publish_goods_price1.setText("");
			et_publish_goods_price1.setHint("请输入价格");
		} else {
			et_publish_goods_price1.setText(p);

		}
		// if (TextUtils.isEmpty(punit)) {
		// et_publish_goods_price_unit1.setText("");
		// et_publish_goods_price_unit1.setHint("单位");
		// } else {
		//
		// et_publish_goods_price_unit1.setText(punit);
		// }
	}

	public interface ImOnclick {
		void imOnclick(boolean isPlus);
	}
}
