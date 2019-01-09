package code.ytn.cn.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.Spinner;


@SuppressLint("AppCompatCustomView")
public class MySpinner extends Spinner {
    private int mSelection = -1;

    public MySpinner(Context context) {
        super(context);
    }

    public MySpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MySpinner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setSelection(int position) {

        mSelection = position;
        super.setSelection(position);
    }

    @Override
    public void setSelection(int position, boolean animate) {

        mSelection = position;
        super.setSelection(position, animate);
    }

    @Override
    public int getSelectedItemPosition() {
        return mSelection;
    }
}