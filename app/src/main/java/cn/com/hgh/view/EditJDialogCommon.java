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
import cn.com.hgh.gridpasswordview.GridPasswordView;
import cn.com.hgh.utils.ScreenUtils;

import com.xizhi.mezone.b.R;

public abstract class EditJDialogCommon extends Dialog {

	private GridPasswordView gridPasswordView;
	private Context context;

	public EditJDialogCommon(Context context) {
		super(context, R.style.DialogStyle_4);
		this.context = context;
		initView();
	}

	private void initView() {
		int screenWidth = ScreenUtils.getScreenWidth(context);
		View view = View.inflate(context, R.layout.editjdialogcommon, null);
		setContentView(view);
		gridPasswordView = (GridPasswordView) view
				.findViewById(R.id.editjdialogcommon_gpv_customUi_chang_old);
		TextView tv_dialog_common_cancel = (TextView) view
				.findViewById(R.id.editjdialogcommon_tv_dialog_common_cancel);
		TextView tv_dialog_common_ok = (TextView) view
				.findViewById(R.id.editjdialogcommon_tv_dialog_common_ok);
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

	public String getInputPass() {
		return gridPasswordView.getPassWord();
	}

	public abstract void onCheckClick();

	public abstract void onOkClick();
}