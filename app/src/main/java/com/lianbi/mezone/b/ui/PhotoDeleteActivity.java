package com.lianbi.mezone.b.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.xizhi.mezone.b.R;

/**
 * 图片删除
 * 
 * @time 上午10:57:15
 * @date 2016-1-15
 * @author hongyu.yang
 * 
 */
public class PhotoDeleteActivity extends BaseActivity {
	private ImageView img_photo_delete;
	private RelativeLayout photo_relativeLayout;
	private Bitmap bitmap;
	private Button photo_bt_exit, photo_bt_del, photo_bt_enter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_delete, NOTYPE);
		bitmap = getIntent().getParcelableExtra("image");
		initView();
		setLisenter();
	}

	/**
	 * 初始化View
	 */
	private void initView() {
		setPageTitleVisibility(View.GONE);
		img_photo_delete = (ImageView) findViewById(R.id.img_photo_delete);
		photo_relativeLayout = (RelativeLayout) findViewById(R.id.photo_relativeLayout);
		photo_bt_exit = (Button) findViewById(R.id.photo_bt_exit);
		photo_bt_del = (Button) findViewById(R.id.photo_bt_del);
		photo_bt_enter = (Button) findViewById(R.id.photo_bt_enter);
		photo_relativeLayout.setBackgroundColor(0x70000000);
		img_photo_delete.setImageBitmap(bitmap);
	}

	/**
	 * 添加监听
	 */
	private void setLisenter() {
		photo_bt_exit.setOnClickListener(this);
		photo_bt_del.setOnClickListener(this);
		photo_bt_enter.setOnClickListener(this);
	}

	@Override
	protected void onChildClick(View view) {
		super.onChildClick(view);
		switch (view.getId()) {
		case R.id.photo_bt_exit:
			finish();
			break;
		case R.id.photo_bt_del:
			setResult(RESULT_OK);
			finish();
			break;
		case R.id.photo_bt_enter:
			finish();
			break;
		}
	}
}
