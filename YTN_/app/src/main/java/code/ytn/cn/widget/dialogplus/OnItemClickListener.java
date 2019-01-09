package code.ytn.cn.widget.dialogplus;

import android.view.View;
import android.widget.AdapterView;

/**
 * @author Orhan Obut
 */
public interface OnItemClickListener extends AdapterView.OnItemClickListener {

  public void onItemClick(DialogPlus dialog, Object item, View view, int position);

}
