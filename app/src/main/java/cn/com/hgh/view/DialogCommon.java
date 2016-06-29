package cn.com.hgh.view;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.xizhi.mezone.b.R;

public abstract class DialogCommon extends Dialog {

	private TextView tv_dialog_common_title;
	private Context context;

	public DialogCommon(Context context) {
		super(context, R.style.DialogStyle_4);
		this.context = context;
		initView();
	}

	TextView tv_dialog_common_cancel;
	TextView tv_dialog_common_ok;

	public void setTv_dialog_common_cancel(String tv_dialog_common_cancel) {
		this.tv_dialog_common_cancel.setText(tv_dialog_common_cancel);
	}

	public void setTv_dialog_common_cancelV(int v) {
		this.tv_dialog_common_cancel.setVisibility(v);
		this.v.setVisibility(v);
	}

	public void setTv_dialog_common_ok(String tv_dialog_common_ok) {
		this.tv_dialog_common_ok.setText(tv_dialog_common_ok);
	}

	View v;

	private void initView() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenWidth = dm.widthPixels;
		View view = View.inflate(context, R.layout.dialog_common, null);
		setContentView(view);
		v = view.findViewById(R.id.line_dialog_common_cancel);
		tv_dialog_common_title = (TextView) view
				.findViewById(R.id.tv_dialog_common_title);
		tv_dialog_common_cancel = (TextView) view
				.findViewById(R.id.tv_dialog_common_cancel);
		tv_dialog_common_ok = (TextView) view
				.findViewById(R.id.tv_dialog_common_ok);
		tv_dialog_common_ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onOkClick();
			}
		});

		tv_dialog_common_cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onCheckClick();
			}
		});
		Window win = getWindow();
		WindowManager.LayoutParams lp = win.getAttributes();
		lp.gravity = Gravity.CENTER;
		lp.width = (int) (screenWidth * 0.8);
		lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
	}

	public void setTextTitle(String txtString) {
		tv_dialog_common_title.setText(txtString);
	}

	public abstract void onCheckClick();

	public abstract void onOkClick();
}