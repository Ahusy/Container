package code.ytn.cn.home.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.common.adapter.BaseAdapterNew;
import com.common.adapter.ViewHolder;

import java.util.List;

import code.ytn.cn.R;
import code.ytn.cn.network.entity.AreaEntity;


/**
 * 地区
 */
public class LanguageAreaAdapter extends BaseAdapterNew<AreaEntity> {

	public LanguageAreaAdapter(Context context, List<AreaEntity> mDatas) {
		super(context, mDatas);
	}

	@Override
	protected int getResourceId(int resId) {
		return R.layout.item_language_area;
	}

	@Override
	protected void setViewData(View convertView, int position) {
		AreaEntity areaEntity = getItem(position);
		if (areaEntity != null) {
			TextView tvLanguageAreaName = ViewHolder.get(convertView, R.id.tv_language_area_name);
			tvLanguageAreaName.setText(areaEntity.getName());
			if (areaEntity.isSelected()) {
				tvLanguageAreaName.setSelected(true);
			} else {
				tvLanguageAreaName.setSelected(false);
			}
		}
	}
}
