package com.lianbi.mezone.b.ui;

import java.text.DecimalFormat;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import cn.com.hgh.baseadapter.BaseAdapterHelper;
import cn.com.hgh.baseadapter.QuickAdapter;
import cn.com.hgh.utils.AbViewUtil;
import cn.com.hgh.utils.ContentUtils;
import cn.com.hgh.utils.MoneyFlag;
import cn.com.hgh.utils.Result;
import cn.com.hgh.utils.SpannableuUtills;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.xizhi.mezone.b.R;
import com.lianbi.mezone.b.bean.Goods_SumbitBean;
import com.lianbi.mezone.b.bean.PriceUnitBean;
import com.lianbi.mezone.b.bean.ProductSourceListBean;
import com.lianbi.mezone.b.httpresponse.MyResultCallback;

/**
 * 货源详情
 * 
 * @time 下午3:27:30
 * @date 2016-1-13
 * @author hongyu.yang
 * 
 */
public class GoodsDetailActivity extends BaseActivity {
	private ImageView img_goods_detail, img_goods_detail_minus,
			img_goods_detail_plus;
	private TextView tv_goods_detail_name, tv_goods_detail_content,
			tv_goods_detail_price, tv_goods_detail_sure;
	EditText et_goods_detail_num;
	private int number, maxAmount;
	ListView listView_goods_detail_jibie;
	private ProductSourceListBean bean;
	private ArrayList<PriceUnitBean> listPrice = new ArrayList<PriceUnitBean>();
	private ArrayList<PriceUnitBean> listAmount = new ArrayList<PriceUnitBean>();
	private int lowPrice;
	String productSourceId;
	protected String businessName;
	private final int REQUEST_GOODSORDERBUY = 5001;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goods_detail, HAVETYPE);
		productSourceId = getIntent().getStringExtra("productSourceId");
		initView();
		initListAdapter();
		setLisenter();
		getPrductSourcedeById();
	}

	/**
	 * 获取详情
	 */
	private void getPrductSourcedeById() {
		okHttpsImp.getPrductSourcedeById(productSourceId,
				new MyResultCallback<String>() {

					@Override
					public void onResponseResult(Result result) {
						String resString = result.getData();
						try {
							JSONObject jsonObjectPro = new JSONObject(resString);
							String resProduct = jsonObjectPro
									.getString("productsrouce");
							ProductSourceListBean beans = JSON.parseObject(
									resProduct, ProductSourceListBean.class);
							JSONObject jsonObjectbusinessinfo = jsonObjectPro
									.getJSONObject("businessinfo");
							JSONObject jsonObject2 = new JSONObject(
									jsonObjectbusinessinfo.getString("data"));
							businessName = jsonObject2
									.getString("businessName");
							if (beans != null) {
								bean = beans;
								updateWidget(bean);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}

					@Override
					public void onResponseFailed(String msg) {

					}
				});
	}

	/**
	 * 更新界面
	 * 
	 * @param bean2
	 */
	protected void updateWidget(ProductSourceListBean bean) {
		tv_goods_detail_name.setText(bean.getProduct_source_title());
		tv_goods_detail_content.setText(bean.getDetail());
		try {
			String p = bean.getPrice();// 50-hh,100-gg
			String[] dd = p.split(",");
			if (dd.length > 0) {
				for (int i = 0; i < dd.length; i++) {
					String[] pp = dd[i].split("-");
					PriceUnitBean bean1 = new PriceUnitBean();
					if (i == 0) {
						bean1.setS(true);
					}
					bean1.setPrice(pp[0]);
					bean1.setPriceUnit(pp[1]);
					listPrice.add(bean1);
				}
			}
			String amount = bean.getUnit();// 50-hh,100-gg
			String[] amountD = amount.split(",");
			if (amountD.length > 0) {
				for (int i = 0; i < amountD.length; i++) {
					String[] pp = amountD[i].split("-");
					PriceUnitBean bean2 = new PriceUnitBean();
					bean2.setAmount(pp[0]);
					bean2.setAmountUnit(pp[1]);
					listAmount.add(bean2);
				}
			}
			if (listPrice.size() > 0) {
				SpannableuUtills.setSpannableu(tv_goods_detail_price,
						MoneyFlag.MONEYFAAG + listPrice.get(0).getPrice(), "/"
								+ listPrice.get(0).getPriceUnit());
				curPrice = Double.parseDouble(listPrice.get(0).getPrice());
			}
			if (listAmount.size() > 0) {
				number = Integer.parseInt(listAmount.get(0).getAmount());
				sStrSatrNum = number;
				lowPrice = Integer.parseInt(listAmount.get(0).getAmount());
				maxAmount = Integer.parseInt(listAmount.get(
						listAmount.size() - 1).getAmount());
				et_goods_detail_num.setText(number + "");
			}
		} catch (Exception e) {
		}
		String imgUrl = bean.getImage();
		Glide.with(this).load(imgUrl).error(R.mipmap.adshouye)
				.into(img_goods_detail);
		mAdapter.replaceAll(listPrice);
		AbViewUtil.setListViewHeight(listView_goods_detail_jibie);
	}

	/**
	 * 初始化View
	 */
	private void initView() {
		setPageTitle("货源详情");
		listView_goods_detail_jibie = (ListView) findViewById(R.id.listView_goods_detail_jibie);
		img_goods_detail = (ImageView) findViewById(R.id.img_goods_detail);
		img_goods_detail_minus = (ImageView) findViewById(R.id.img_goods_detail_minus);
		img_goods_detail_plus = (ImageView) findViewById(R.id.img_goods_detail_plus);
		tv_goods_detail_name = (TextView) findViewById(R.id.tv_goods_detail_name);
		tv_goods_detail_content = (TextView) findViewById(R.id.tv_goods_detail_content);
		tv_goods_detail_price = (TextView) findViewById(R.id.tv_goods_detail_price);
		et_goods_detail_num = (EditText) findViewById(R.id.et_goods_detail_num);
		tv_goods_detail_sure = (TextView) findViewById(R.id.tv_goods_detail_sure);
		LinearLayout.LayoutParams layoutParams = (LayoutParams) img_goods_detail
				.getLayoutParams();
		layoutParams.width = screenWidth;
		layoutParams.height = screenWidth / 2;
	}

	/**
	 * 当前起批量
	 */
	int sStrSatrNum;
	QuickAdapter<PriceUnitBean> mAdapter;

	/**
	 * 初始化list Adapter
	 */
	private void initListAdapter() {
		mAdapter = new QuickAdapter<PriceUnitBean>(this,
				R.layout.item_goods_detail_jibie, listPrice) {

			@Override
			protected void convert(final BaseAdapterHelper helper,
					final PriceUnitBean item) {
				final int position = helper.getPosition();
				helper.setText(R.id.item_goods_detail_jibie_tv_fen,
						listAmount.get(position).getAmount() + "/"
								+ listAmount.get(position).getAmountUnit());
				TextView item_goods_detail_jibie_tv_price = helper
						.getView(R.id.item_goods_detail_jibie_tv_price);
				// 价格
				SpannableuUtills.setSpannableu(
						item_goods_detail_jibie_tv_price, MoneyFlag.MONEYFAAG
								+ item.getPrice(), "/" + item.getPriceUnit());

				ImageView iv = helper.getView(R.id.item_goods_detail_jibie_iv);
				if (item.isS()) {
					iv.setImageResource(R.mipmap.message_checked);
				} else {
					iv.setImageResource(R.mipmap.message_unchecked);
				}
				iv.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// 价格
						SpannableuUtills.setSpannableu(tv_goods_detail_price,
								MoneyFlag.MONEYFAAG + item.getPrice(), "/"
										+ item.getPriceUnit());
						sStrSatrNum = Integer.parseInt(listAmount.get(position)
								.getAmount());
						number = sStrSatrNum;
						et_goods_detail_num.setText(sStrSatrNum + "");
						int s = listPrice.size();
						for (int i = 0; i < s; i++) {
							listPrice.get(i).setS(false);
						}
						item.setS(true);
						mAdapter.replaceAll(listPrice);
					}
				});

			}
		};
		// 设置适配器
		listView_goods_detail_jibie.setAdapter(mAdapter);

	}

	/**
	 * 当前价格
	 */
	double curPrice;

	/**
	 * 添加监听
	 */
	private void setLisenter() {
		img_goods_detail_minus.setOnClickListener(this);
		img_goods_detail_plus.setOnClickListener(this);
		tv_goods_detail_sure.setOnClickListener(this);
		et_goods_detail_num.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				String charMy = s.toString().trim();
				if (charMy.length() != 0) {
					int max = Integer.parseInt(charMy);
					caculate(max);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}

	/**
	 * 計算價格
	 */
	public void caculate(int max) {
		if (max >= sStrSatrNum) {
			number = max;
			int sss = listPrice.size();
			int jjj = 0;
			for (int j = 0; j < sss; j++) {
				int num = Integer.parseInt(listAmount.get(j).getAmount());
				if (max < num) {
					break;
				}
				jjj = j;
			}
			for (int i = 0; i < sss; i++) {
				listPrice.get(i).setS(false);
			}
			listPrice.get(jjj).setS(true);
			curPrice = Double.parseDouble(listPrice.get(jjj).getPrice());
			SpannableuUtills.setSpannableu(tv_goods_detail_price,
					MoneyFlag.MONEYFAAG + listPrice.get(jjj).getPrice(), "/"
							+ listPrice.get(jjj).getPriceUnit());
		} else {
			number = max;
			int sss = listPrice.size();
			int jjj = 0;
			for (int j = 0; j < sss; j++) {
				int num = Integer.parseInt(listAmount.get(j).getAmount());
				if (max < num) {
					break;
				}
				jjj = j;
			}
			for (int i = 0; i < sss; i++) {
				listPrice.get(i).setS(false);
			}
			listPrice.get(jjj).setS(true);
			curPrice = Double.parseDouble(listPrice.get(jjj).getPrice());
			SpannableuUtills.setSpannableu(tv_goods_detail_price,
					MoneyFlag.MONEYFAAG + listPrice.get(jjj).getPrice(), "/"
							+ listPrice.get(jjj).getPriceUnit());
		}
		mAdapter.replaceAll(listPrice);
	}

	@Override
	protected void onChildClick(View view) {
		if (bean == null) {
			return;
		}
		switch (view.getId()) {
		case R.id.img_goods_detail_minus:
			if (number <= lowPrice) {
				return;
			} else {
				number--;
				caculate(number);
			}
			et_goods_detail_num.setText(number + "");
			break;
		case R.id.img_goods_detail_plus:
			number++;
			caculate(number);
			et_goods_detail_num.setText(number + "");
			break;
		case R.id.tv_goods_detail_sure:
			double price = number * curPrice;
			DecimalFormat df = new DecimalFormat("######0.00");
			String inputString = et_goods_detail_num.getText().toString()
					.trim();
			String numm = "";
			if (!TextUtils.isEmpty(inputString)) {
				int inputNum = Integer.parseInt(inputString);
				if (inputNum < Integer.parseInt(listAmount.get(0).getAmount())) {
					ContentUtils.showMsg(this, "起批数量小于最低标准");
					et_goods_detail_num.setText(listAmount.get(0).getAmount());
					return;
				} else {
					numm = et_goods_detail_num.getText().toString().trim();
				}
			} else {
				ContentUtils.showMsg(this, "请输入数量");
				return;
			}
			String orderPrice = df.format(price) + "";
			String curPriceS = df.format(curPrice) + "";
			Goods_SumbitBean goods_SumbitBean = new Goods_SumbitBean(
					bean.getProduct_source_id(), bean.getBusiness_id(),
					orderPrice, orderPrice, curPriceS, numm, bean.getImage(),
					bean.getProduct_source_title());
			Intent intent_goods_detail1 = new Intent(this,
					GoodsMallDetailActivity.class);
			intent_goods_detail1.putExtra("goods_SumbitBean", goods_SumbitBean);
			intent_goods_detail1.putExtra("businessName", businessName);
			startActivityForResult(intent_goods_detail1, REQUEST_GOODSORDERBUY);
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case REQUEST_GOODSORDERBUY:
				finish();
				break;
			}
		}
	}
}
