package code.ytn.cn.utils;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：验证所有输入框是否填写完
 */
public class InputCompleteWatcher implements TextWatcher {
	private InputCompleteListener listener;
	private List<TextView> views;

	public InputCompleteWatcher(InputCompleteListener listener) {
		super();
		this.listener = listener;
	}

	public interface InputCompleteListener {
		boolean onTextChange(boolean isFinish);
	}
	
	public void addEditText(EditText et) {
		if(views == null) {
			views = new ArrayList<TextView>();
		}
		et.addTextChangedListener(this);
		views.add(et);
	}

	public void removeAll(){
		if(views!=null&&views.size()>0){
			for(TextView v:views){
				v.removeTextChangedListener(this);
			}
			views.clear();
		}

	}
	
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		
	}

	
	@Override
	public void afterTextChanged(Editable s) {
		check();
	}


	public void check() {
		boolean isFinish = true;
		if(views != null && views.size() > 0) {
			for(TextView et : views) {
				String str = et.getText().toString().trim();
				if(TextUtils.isEmpty(str)) {
					isFinish = false;
					break;
				}
			}
		}else {
			isFinish = false;
		}
		
		if(listener != null) {
			listener.onTextChange(isFinish);
		}
	}
}
