package cn.com.hgh.view;

import cn.com.hgh.utils.IsContainEmoji;
import android.content.Context;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 不支持Emoji表情的Editext
 * 
 * @time 上午10:13:57
 * @date 2015-11-30
 * @author hongyu.yang
 * 
 */
public class ContainsEmojiEditText extends EditText {
	// 输入表情前的光标位置
	private int cursorPos;
	// 输入表情前EditText中的文本
	private String inputAfterText;
	// 是否重置了EditText的内容
	private boolean resetText;

	private Context mContext;

	public ContainsEmojiEditText(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
		initEditText();

	}

	public ContainsEmojiEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		initEditText();

	}

	// 初始化edittext 控件
	private void initEditText() {
		addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start,
					int before, int count) {
				if (!resetText) {
					cursorPos = getSelectionEnd();
					// 这里用s.toString()而不直接用s是因为如果用s，
					// 那么，inputAfterText和s在内存中指向的是同一个地址，s改变了，
					// inputAfterText也就改变了，那么表情过滤就失败了
					inputAfterText = s.toString();
				}

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				try {

					if (!resetText) {
						if (count >= 2) {// 表情符号的字符长度最小为2
							CharSequence input = s.subSequence(cursorPos,
									cursorPos + count);
							if (IsContainEmoji.containsEmoji(input.toString())) {
								resetText = true;
								Toast.makeText(mContext, "不支持输入Emoji表情符号",
										Toast.LENGTH_SHORT).show();
								// 是表情符号就将文本还原为输入表情符号之前的内容
								setText(inputAfterText);
								CharSequence text = getText();
								if (text instanceof Spannable) {
									Spannable spanText = (Spannable) text;
									Selection.setSelection(spanText,
											text.length());
								}
							}
						}
					} else {
						resetText = false;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void afterTextChanged(Editable editable) {

			}
		});
	}

}
