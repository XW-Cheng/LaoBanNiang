package com.lianbi.mezone.b.ui;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import cn.com.hgh.baseadapter.BaseAdapterHelper;
import cn.com.hgh.baseadapter.QuickAdapter;
import cn.com.hgh.playview.BaseSliderView;
import cn.com.hgh.playview.PagerIndicator;
import cn.com.hgh.playview.BaseSliderView.OnSliderClickListener;
import cn.com.hgh.playview.SliderLayout;
import cn.com.hgh.playview.imp.TextSliderView;
import cn.com.hgh.utils.AbViewUtil;
import cn.com.hgh.utils.MathExtend;
import cn.com.hgh.utils.Result;
import cn.com.hgh.utils.SpannableuUtills;

import com.alibaba.fastjson.JSON;
import com.xizhi.mezone.b.R;
import com.lianbi.mezone.b.bean.Ades_ImageEs;
import com.lianbi.mezone.b.bean.MyLiCaiBean;
import com.lianbi.mezone.b.httpresponse.MyResultCallback;

/**
 * 
 * @author guanghui.han 定期活期理财
 */
public class RegularDemandManagementActivity extends BaseActivity implements
		OnSliderClickListener {
	LinearLayout adeslltview_llt;
	SliderLayout maincaishendaofragment_photos;
	ProgressBar adeslltview_siderlayout_progressBar;
	ListView regulardemandmanagementactivity_list;
	ArrayList<Ades_ImageEs> listAdes = new ArrayList<Ades_ImageEs>();
	ArrayList<MyLiCaiBean> mDatas = new ArrayList<MyLiCaiBean>();
	/**
	 * 标题
	 */
	String title;
	/**
	 * 是否定期
	 */
	boolean isRegular = false;

	/**
	 * 广告
	 */
	private ArrayList<Ades_ImageEs> ades_ImageEs = new ArrayList<Ades_ImageEs>();

	String type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.regulardemandmanagementactivity, HAVETYPE);
		initView();
	}

	/**
	 * 初始化View
	 */
	protected void initView() {
		setPageTitle("定期理财页面");
		regulardemandmanagementactivity_list = (ListView) findViewById(R.id.regulardemandmanagementactivity_list);
		titleInit();
		initAdesView();
		getData();
		getAadver();
		initListAdapter();
	}

	private void getData() {
		MyLiCaiBean bean = new MyLiCaiBean();
		bean.setAmount(100f);
		bean.setDate("2015 2 5");
		bean.setDeadline(50);
		bean.setDescription("你好我是描述");
		bean.setId(74);
		bean.setLable("d");
		bean.setName("顶起理财");
		bean.setRate(5.0);
		MyLiCaiBean bean1 = new MyLiCaiBean();
		bean1.setAmount(100);
		bean1.setDate("2015 2 5");
		bean1.setDeadline(50);
		bean1.setDescription("你好我是描述");
		bean1.setId(74);
		bean1.setLable("h");
		bean1.setName("顶起理财");
		bean1.setRate(5.0);
		mDatas.add(bean1);
		mDatas.add(bean);
	}

	/**
	 * 获取广告
	 */
	public void getAadver() {
//		okHttpsImp.getAdvert("4", new MyResultCallback<String>() {
//
//			@Override
//			public void onResponseResult(Result result) {
//				maincaishendaofragment_photos.removeAllSliders();
//				String resString = result.getData();
//				try {
//					JSONObject jsonObject = new JSONObject(resString);
//					resString = jsonObject.getString("advertList");
//					ades_ImageEs = (ArrayList<Ades_ImageEs>) JSON.parseArray(
//							resString, Ades_ImageEs.class);
//					if (ades_ImageEs != null && ades_ImageEs.size() > 0) {
//						for (int i = 0; i < ades_ImageEs.size(); i++) {
//							TextSliderView textSliderView = new TextSliderView(
//									RegularDemandManagementActivity.this, i,5);
//							textSliderView
//									.image(ades_ImageEs.get(i).getImage())
//									.error(R.drawable.adshouye);
//							textSliderView
//									.setOnSliderClickListener(RegularDemandManagementActivity.this);
//							maincaishendaofragment_photos
//									.addSlider(textSliderView);
//						}
//					} else {
//						for (int i = 0; i < 3; i++) {
//							TextSliderView textSliderView = new TextSliderView(
//									RegularDemandManagementActivity.this, i,5);
//							textSliderView.image(R.drawable.adshouye);
//							textSliderView
//									.setOnSliderClickListener(RegularDemandManagementActivity.this);
//							maincaishendaofragment_photos
//									.addSlider(textSliderView);
//						}
//					}
//					maincaishendaofragment_photos
//							.setPresetIndicatorV(SliderLayout.PresetIndicators.Center_Bottom);
//					adeslltview_siderlayout_progressBar
//							.setVisibility(View.GONE);
//					maincaishendaofragment_photos.setVisibility(View.VISIBLE);
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
//			}
//
//			@Override
//			public void onResponseFailed(String msg) {
//				maincaishendaofragment_photos.removeAllSliders();
//				for (int i = 0; i < 3; i++) {
//					TextSliderView textSliderView = new TextSliderView(
//							RegularDemandManagementActivity.this, i,5);
//					textSliderView.image(R.drawable.adshouye);
//					textSliderView
//							.setOnSliderClickListener(RegularDemandManagementActivity.this);
//					maincaishendaofragment_photos.addSlider(textSliderView);
//				}
//				maincaishendaofragment_photos
//						.setPresetIndicatorV(SliderLayout.PresetIndicators.Center_Bottom);
//				adeslltview_siderlayout_progressBar.setVisibility(View.GONE);
//				maincaishendaofragment_photos.setVisibility(View.VISIBLE);
//			}
//		});
	}

	/**
	 * 初始化title
	 */
	private void titleInit() {
		title = getIntent().getStringExtra("title");
		setPageTitle(title);
		if (title.equals("定期理财产品")) {
			type = "d";
			isRegular = true;
		} else {
			type = "h";
			isRegular = false;
		}
	}

	QuickAdapter<MyLiCaiBean> mAdapter;

	/**
	 * 初始化list Adapter
	 */
	private void initListAdapter() {
		mAdapter = new QuickAdapter<MyLiCaiBean>(this,
				R.layout.regulardemandmanagementactivity_list_item, mDatas) {

			@Override
			protected void convert(BaseAdapterHelper helper, MyLiCaiBean item) {
				String price = MathExtend.roundNew(item.getRate(), 4);
				String one = price.substring(0, price.indexOf("."));
				String two = price
						.substring(price.indexOf("."), price.length()) + "%";
				TextView mTextView = helper
						.getView(R.id.regulardemandmanagementactivity_list_item_yearlv);
				SpannableuUtills.setSpannableu(mTextView, one, two);

				helper.setText(
						R.id.regulardemandmanagementactivity_list_item_name,
						item.getName());
				helper.setText(
						R.id.regulardemandmanagementactivity_list_item_dealline,
						item.getDeadline() + "个月");
			}
		};
		// 设置适配器
		regulardemandmanagementactivity_list.setAdapter(mAdapter);
		AbViewUtil.setListViewHeight(regulardemandmanagementactivity_list);
	}

	/**
	 * 初始化广告视图
	 */
	private void initAdesView() {
		adeslltview_siderlayout_progressBar = (ProgressBar) findViewById(R.id.adeslltview_siderlayout_progressBar);
		maincaishendaofragment_photos = (SliderLayout) findViewById(R.id.adeslltview_siderlayout);
		maincaishendaofragment_photos
				.setPresetTransformer(SliderLayout.Transformer.Default);
		adeslltview_llt = (LinearLayout) findViewById(R.id.adeslltview_llt);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, screenWidth / 4);
		adeslltview_llt.setLayoutParams(params);
	}

	@Override
	public void onSliderClick(BaseSliderView slider) {
		if (ades_ImageEs != null && ades_ImageEs.size() > 0) {
			String url = ades_ImageEs.get(slider.getP()).getUrl();
			Intent intent = new Intent(this, WebActivty.class);
			intent.putExtra(WebActivty.T, ades_ImageEs.get(slider.getP())
					.getName());
			intent.putExtra(WebActivty.U, url);
			intent.putExtra("Re", true);
			startActivity(intent);
		}
	}
}
