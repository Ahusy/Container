package code.ytn.cn.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.common.utils.ToastUtil;

import code.ytn.cn.R;


/**
 * 加减数量
 */
public class AddMinuLayout extends LinearLayout implements View.OnClickListener {

	private int num = 1;
	private int mMinNum = 1;
	private int mMaxNum = 5000;
	private TextView result;
	Context context;
	public AddMinuLayout(Context context) {
		super(context);
		initView(context);
	}

	public AddMinuLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public AddMinuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView(context);
	}

	private void initView(Context context) {
		View view = View.inflate(context, R.layout.num_minu_and_add_layout, null);
		TextView minus = (TextView) view.findViewById(R.id.button_minu);
		TextView add = (TextView) view.findViewById(R.id.button_add);
		result = (TextView) view.findViewById(R.id.text_result);
		minus.setOnClickListener(this);
		add.setOnClickListener(this);
		this.context = context;
		addView(view, new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
	}

	public void addTextWatcher(TextWatcher textWatcher) {
		result.addTextChangedListener(textWatcher);
	}

	@Override
	public void onClick(View view) {
		num = Integer.parseInt(result.getText().toString());
		switch (view.getId()) {
			case R.id.button_minu:
				if (num <= mMinNum){
					ToastUtil.shortShow(context,"");
					return;
				}
				if (num > mMinNum) {
					num--;
					result.setText(String.valueOf(num));
				}
				break;
			case R.id.button_add:
				if(num >= mMaxNum){
					Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
					return;
				}
				if (num < mMaxNum) {
					num++;
					result.setText(String.valueOf(num));
				}
				break;
		}
	}

	public void setLimitNum(int minNum, int maxNum) {
		this.num=minNum;
		result.setText(num+"");
		mMinNum = minNum;
		mMaxNum = maxNum;
	}

	public String getResult() {
		return String.valueOf(result.getText().toString());
	}

	public void  setResult(String value){
		result.setText(value);
		invalidate();
	}
}
