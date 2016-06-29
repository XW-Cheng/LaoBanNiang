package com.lianbi.mezone.b.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cn.com.hgh.utils.ContentUtils;
import cn.com.hgh.utils.MathExtend;
import cn.com.hgh.utils.Result;
import cn.com.hgh.utils.SpannableuUtills;

import com.bumptech.glide.Glide;
import com.xizhi.mezone.b.R;
import com.lianbi.mezone.b.bean.Goods_SumbitBean;
import com.lianbi.mezone.b.httpresponse.MyResultCallback;

/**
 * 商品确认下单
 * 
 * @time 下午4:52:03
 * @date 2016-1-13
 * @author hongyu.yang
 * 
 */
public class GoodsMallDetailActivity extends BaseActivity {
	private TextView tv_goodsmall_detail_shopping_name,
			tv_goodsmall_detail_name, tv_goodsmall_detail_phone,
			tv_goodsmall_detail_address, tv_goodsmall_detail_shop_name,
			tv_goodsmall_detail_single_price, tv_goodsmall_detail_num,
			tv_goodsmall_detail_sum_price, tv_goodsmall_detail_sumbit;
	private ImageView img_goodsmall_detail;
	Goods_SumbitBean goods_SumbitBean;
	String businessName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goods_mall_detail, NOTYPE);
		goods_SumbitBean = (Goods_SumbitBean) getIntent().getSerializableExtra(
				"goods_SumbitBean");
		businessName = getIntent().getStringExtra("businessName");
		initView();
		if (goods_SumbitBean != null) {
			tv_goodsmall_detail_shopping_name.setText(businessName);
			tv_goodsmall_detail_name.setText(userShopInfoBean.getNikeName());
			tv_goodsmall_detail_phone.setText(userShopInfoBean.getPhone());
			tv_goodsmall_detail_address.setText(userShopInfoBean.getAddress());
			tv_goodsmall_detail_num.setText(goods_SumbitBean.getNum());
			Glide.with(this).load(goods_SumbitBean.getImageUrl())
					.error(R.mipmap.source_default).into(img_goodsmall_detail);
			tv_goodsmall_detail_shop_name.setText(goods_SumbitBean.getpName());
			tv_goodsmall_detail_sum_price.setText("¥"
					+ goods_SumbitBean.getOrderPrice());
			SpannableuUtills.setSpannableu(tv_goodsmall_detail_single_price,
					"¥", goods_SumbitBean.getPrice(), "/元");
		}
		setLisenter();
	}

	/**
	 * 初始化View
	 */
	private void initView() {
		setPageTitle("确认下单");
		tv_goodsmall_detail_shopping_name = (TextView) findViewById(R.id.tv_goodsmall_detail_shopping_name);
		tv_goodsmall_detail_name = (TextView) findViewById(R.id.tv_goodsmall_detail_name);
		tv_goodsmall_detail_phone = (TextView) findViewById(R.id.tv_goodsmall_detail_phone);
		tv_goodsmall_detail_address = (TextView) findViewById(R.id.tv_goodsmall_detail_address);
		tv_goodsmall_detail_shop_name = (TextView) findViewById(R.id.tv_goodsmall_detail_shop_name);
		tv_goodsmall_detail_single_price = (TextView) findViewById(R.id.tv_goodsmall_detail_single_price);
		tv_goodsmall_detail_num = (TextView) findViewById(R.id.tv_goodsmall_detail_num);
		tv_goodsmall_detail_sum_price = (TextView) findViewById(R.id.tv_goodsmall_detail_sum_price);
		tv_goodsmall_detail_sumbit = (TextView) findViewById(R.id.tv_goodsmall_detail_sumbit);
		img_goodsmall_detail = (ImageView) findViewById(R.id.img_goodsmall_detail);
		String totalprice = MathExtend.roundNew(100.56, 2);
		tv_goodsmall_detail_sum_price.setText(totalprice);
	}

	/**
	 * 添加监听
	 */
	private void setLisenter() {
		tv_goodsmall_detail_sumbit.setOnClickListener(this);
	}

	@Override
	protected void onChildClick(View view) {
		switch (view.getId()) {
		case R.id.tv_goodsmall_detail_sumbit:
			okHttpsImp.addProductSourceOrder(
					goods_SumbitBean.getProductSrouceId(),
					userShopInfoBean.getBusinessId(),
					goods_SumbitBean.getBuyBusinessId(),
					goods_SumbitBean.getOrderPrice(),
					userShopInfoBean.getName(), userShopInfoBean.getPhone(),
					userShopInfoBean.getAddress(),
					goods_SumbitBean.getAmount(), goods_SumbitBean.getPrice(),
					goods_SumbitBean.getNum(), new MyResultCallback<String>() {

						@Override
						public void onResponseResult(Result result) {
							ContentUtils.showMsg(GoodsMallDetailActivity.this,
									"下单成功");
							setResult(RESULT_OK);
							finish();
						}

						@Override
						public void onResponseFailed(String msg) {
							ContentUtils.showMsg(GoodsMallDetailActivity.this,
									"下单失败");
						}
					});
			break;
		}
	}
}
