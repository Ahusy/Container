package code.ytn.cn.shop.view.PickTime.listener;


import code.ytn.cn.shop.view.PickTime.bean.DateBean;
import code.ytn.cn.shop.view.PickTime.bean.DateType;


public interface WheelPickerListener {
     void onSelect(DateType type, DateBean bean);
}
