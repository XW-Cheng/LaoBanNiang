package cn.com.hgh.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xizhi.mezone.b.R;
import com.lianbi.mezone.b.ui.MainActivity;

public class ErWeMaDialog extends Dialog implements android.view.View.OnClickListener {

	private String message = "";
	private LinearLayout dialog_ll;
	private Bitmap bitmap;
	private ImageView mImageView;
//	private MainActivity mMain;
	public ErWeMaDialog(Context context) {
		this(context, R.style.DialogStyle_1);
//		mMain = (MainActivity) context;
	}

	public ErWeMaDialog(Context context, int theme) {
		super(context, theme);
		setContentView(R.layout.dialog_erweima);
//		mMain = (MainActivity) context;
		initialViews();
	}
	
	
	

	private void initialViews() {
		dialog_ll = (LinearLayout) findViewById(R.id.dialog_ll);
		mImageView = (ImageView) findViewById(R.id.image);
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
		mImageView.setImageBitmap(bitmap);
		dialog_ll.setOnClickListener(this);
	}

	
	
	
	public void setColor() {
	}

	public String getMessage() {
		return message;
	}

	@Override
	public void onClick(View v) {
//		mMain.getCount();
		dismiss();
	}

}
