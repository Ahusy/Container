package code.ytn.cn.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.common.utils.StringUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import code.ytn.cn.R;
import code.ytn.cn.network.entity.ChooseGoodsListEntity;
import code.ytn.cn.network.entity.DataEntity;
import code.ytn.cn.network.entity.ItemResultEntity;
import code.ytn.cn.shop.adapter.DialogAdjustAdapter;
import code.ytn.cn.shop.adapter.DialogValetCommitAdapter;
import code.ytn.cn.shop.adapter.WeekSpinnerAdapter;
import code.ytn.cn.widget.ClearEditText;
import code.ytn.cn.widget.PasswordInputView;


/**
 * 弹窗
 */
@SuppressLint("InflateParams")
public class DialogUtils {

    private static AlertDialog dialog;

    public static void showRecommendInfoDialog(Context context, String number, String name, String phone, View.OnClickListener okClickListener, View.OnClickListener cancelClickListener) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_recommender_info, null);
        if (dialog == null) {
            dialog = new AlertDialog.Builder(context).setView(view).create();

        }
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
        TextView recommendNumber = (TextView) view.findViewById(R.id.recommend_number);
        TextView recommendName = (TextView) view.findViewById(R.id.recommend_name);
        TextView recommendPhone = (TextView) view.findViewById(R.id.recommend_phone);
        TextView btnOk = (TextView) view.findViewById(R.id.btn_ok);
        TextView btnCancel = (TextView) view.findViewById(R.id.btn_cancel);
        recommendNumber.setText(number);
        recommendName.setText(name);
        recommendPhone.setText(phone);
        btnOk.setOnClickListener(okClickListener);
        btnCancel.setOnClickListener(cancelClickListener);
        dialog.setCancelable(true);
        dialog.show();
        setAlertDialogWidth(context);
    }


    /*
    * content 和contentSsb一个只能填一个*/
//    SpannableStringBuilder contentSsb,
    public static void showTwoBtnDialog(Context context,String title, String content, View.OnClickListener okClickListener, View.OnClickListener cancelClickListener) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_two_button, null);
        dialog = new AlertDialog.Builder(context).setView(view).create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
        TextView titleTv = (TextView) view.findViewById(R.id.dialog_title);
        TextView contentTv = (TextView) view.findViewById(R.id.dialog_content);
        TextView btnOk = (TextView) view.findViewById(R.id.btn_ok);
        TextView btnCancel = (TextView) view.findViewById(R.id.btn_cancel);

        titleTv.setText(title);
        if (!StringUtil.isEmpty(content)) {
            contentTv.setText(content);
        }
//        if (contentSsb != null) {
//            contentTv.setText(contentSsb);
//        }

        btnOk.setOnClickListener(okClickListener);
        btnCancel.setOnClickListener(cancelClickListener);
        dialog.setCancelable(true);
        dialog.show();
        setAlertDialogWidth(context);
    }

    /*
    * content 和contentSsb一个只能填一个*/
    public static void showTwoBtnExDialog(Context context, String title, String content, SpannableStringBuilder contentSsb, View.OnClickListener okClickListener, View.OnClickListener cancelClickListener) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_two_button, null);
        dialog = new AlertDialog.Builder(context).setView(view).create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
        TextView titleTv = (TextView) view.findViewById(R.id.dialog_title);
        TextView contentTv = (TextView) view.findViewById(R.id.dialog_content);
        TextView btnOk = (TextView) view.findViewById(R.id.btn_ok);
        TextView btnCancel = (TextView) view.findViewById(R.id.btn_cancel);

        titleTv.setText(title);
        if (!StringUtil.isEmpty(content)) {
            contentTv.setText(content);
        }
        if (contentSsb != null) {
            contentTv.setText(contentSsb);
        }
        btnOk.setOnClickListener(okClickListener);
        btnCancel.setOnClickListener(cancelClickListener);
        dialog.setCancelable(true);
        dialog.show();
        setAlertDialogWidth(context);
    }


    public static void showOneBtnDialog(Context context, String content, View.OnClickListener onClickListener) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_one_button, null);
        dialog = new AlertDialog.Builder(context).setView(view).create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
        TextView titleTv = (TextView) view.findViewById(R.id.dialog_onebtn_title);
        TextView contentTv = (TextView) view.findViewById(R.id.dialog_onebtn_content);
        TextView btnTv = (TextView) view.findViewById(R.id.dialog_onebtn_btn);

//        titleTv.setText(title);
        contentTv.setText(content);
//        btnTv.setText(btn);

        btnTv.setOnClickListener(onClickListener);

        dialog.setCancelable(false);
        dialog.show();
        setAlertDialogWidth(context);
    }

    public static void showFriendDialog(Context context, String avatarUrl, String name, String code, String gapNum, String friendNum) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_friend, null);
        dialog = new AlertDialog.Builder(context).setView(view).create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }

        SimpleDraweeView avatar = (SimpleDraweeView) view.findViewById(R.id.dialog_friend_avatar);
        TextView nameTv = (TextView) view.findViewById(R.id.dialog_friend_name);
        TextView codeTv = (TextView) view.findViewById(R.id.dialog_friend_code);
        TextView gapTv = (TextView) view.findViewById(R.id.dialog_friend_gap);
        TextView friendTv = (TextView) view.findViewById(R.id.dialog_friend_friend);

        ImageUtils.setSmallImg(avatar, avatarUrl);
        nameTv.setText(name);
        codeTv.setText(code);
        gapTv.setText(gapNum);
        friendTv.setText(friendNum);
        dialog.setCancelable(true);
        dialog.show();
        setAlertDialogWidth(context);
    }

    public static void showPwdInputDialog(Context context, DialogEtClickListenner dialogEtClickListenner, View.OnClickListener onClickListener) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_password, null);
        dialog = new AlertDialog.Builder(context).setView(view).create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }

        ImageView closeView = (ImageView) view.findViewById(R.id.dialog_pwd_close);
        PasswordInputView pwdInputView = (PasswordInputView) view.findViewById(R.id.dialog_pwd_pwd);
        TextView setBtn = (TextView) view.findViewById(R.id.dialog_pwd_set);
        TextView confirmBtn = (TextView) view.findViewById(R.id.dialog_pwd_confirm);
        TextView forgetBtn = (TextView) view.findViewById(R.id.dialog_pwd_forget);

        closeView.setOnClickListener(onClickListener);
//
//        setBtn.setOnClickListener(v -> context.startActivity(new Intent(context, IntegralPwdActivity.class)));
//
//        forgetBtn.setOnClickListener(v -> context.startActivity(new Intent(context, IntegralPwdActivity.class)));

        confirmBtn.setOnClickListener(v -> dialogEtClickListenner.onClick(v, pwdInputView));

        dialog.setCancelable(false);
        dialog.show();
        setAlertDialogWidth(context);
    }

    public interface DialogEtClickListenner {
        void onClick(View v, EditText editText);
    }


    public static void showQueryOrderDialog(Context context, DialogEtQueryClickListenner dialogEtQueryClickListenner , View.OnClickListener onClickListener) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_query_button, null);
        dialog = new AlertDialog.Builder(context).setView(view).create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }

        ClearEditText orderNum = (ClearEditText) view.findViewById(R.id.plase_order_num);
        ClearEditText orderUser = (ClearEditText) view.findViewById(R.id.plase_order_user);
        TextView closeBtn = (TextView) view.findViewById(R.id.dialog_close);
        TextView queryBtn = (TextView) view.findViewById(R.id.dialog_query);

        closeBtn.setOnClickListener(onClickListener);

        queryBtn.setOnClickListener(v -> dialogEtQueryClickListenner.onClick(v, orderNum,orderUser));

        dialog.setCancelable(false);
        dialog.show();
        setAlertDialogWidth(context);
    }

    public interface DialogEtQueryClickListenner {
        void onClick(View v, EditText editText1,EditText editText2);
    }

    public static void showQueryRepetoryDialog(Context context, DialogRepQueryClickListenner dialogEtQueryClickListenner , View.OnClickListener onClickListener) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_query_repetory, null);
        dialog = new AlertDialog.Builder(context).setView(view).create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }

        ClearEditText editText = (ClearEditText) view.findViewById(R.id.query_repetory_et);
        TextView closeBtn = (TextView) view.findViewById(R.id.query_repetory_cancel);
        TextView queryBtn = (TextView) view.findViewById(R.id.query_repetory_ok);

        closeBtn.setOnClickListener(onClickListener);

        queryBtn.setOnClickListener(v -> dialogEtQueryClickListenner.onClick(v, editText));

        dialog.setCancelable(false);
        dialog.show();
        setAlertDialogWidth(context);
    }

    public interface DialogRepQueryClickListenner {
        void onClick(View v, EditText editText);
    }

    // 代客下单 提交订单
    @SuppressLint("SetTextI18n")
    public static void showCommit(Context context, List<ItemResultEntity.ItemResultListEntity> entities, String phone, String total, String dscTotal, String couponName, View.OnClickListener okClickListener, View.OnClickListener cancelClickListener) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_order_comit, null);
        dialog = new AlertDialog.Builder(context).setView(view).create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        ListView dialogLv = (ListView) view.findViewById(R.id.dialog_commit_order_lv);
        TextView dialogPhone = (TextView) view.findViewById(R.id.dialog_commit_order_phone);
        TextView dialogPrice = (TextView) view.findViewById(R.id.dialog_commit_order_price);
        TextView dialogCancel = (TextView) view.findViewById(R.id.dialog_commit_order_cancel);
        TextView dialogCommit = (TextView) view.findViewById(R.id.dialog_commit_order_commit);
        TextView dialogCouponName = (TextView) view.findViewById(R.id.dialog_commit_order_couponName);
        TextView dialogTotal = (TextView) view.findViewById(R.id.dialog_commit_order_dscTotal);
        if (!StringUtil.isEmpty(phone)){
            dialogPhone.setText(phone);
        }
        if (!StringUtil.isEmpty(total)){
            dialogPrice.setText("合计:￥"+total);
        }
        if (!StringUtil.isEmpty(couponName)){
            dialogCouponName.setText(couponName);
        }
        if (!dscTotal.equals("0.0") && !dscTotal.equals("0.00")){
            dialogTotal.setText("总优惠金额:￥"+dscTotal);
        }else{
            dialogTotal.setText("");
        }
//        dialogdTotal.setText(dscTotal != null && !dscTotal.equals("") ? dscTotal : "");
        dialogCancel.setOnClickListener(cancelClickListener);
        dialogCommit.setOnClickListener(okClickListener);
        dialog.setCancelable(true);
        dialog.show();
//        setAlertDialogWidth(context);
        DialogValetCommitAdapter dialogAdapter = new DialogValetCommitAdapter(context,entities);
        dialogLv.setAdapter(dialogAdapter);

    }

    // 商品调价
    public static void showAdjustComit(Context context, List<ChooseGoodsListEntity> entities,View.OnClickListener okClickListener, View.OnClickListener cancelClickListener) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_goods_adjust_price, null);
        dialog = new AlertDialog.Builder(context).setView(view).create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        ListView dialogLv = (ListView) view.findViewById(R.id.dialog_goods_adjust_price_lv);
        TextView dialogCancel = (TextView) view.findViewById(R.id.dialog_goods_adjust_price_cancel);
        TextView dialogCommit = (TextView) view.findViewById(R.id.dialog_goods_adjust_price_comit);

        dialogCancel.setOnClickListener(cancelClickListener);
        dialogCommit.setOnClickListener(okClickListener);
        dialog.setCancelable(false);
        dialog.show();
//        setAlertDialogWidth(context);
        DialogAdjustAdapter dialogAdapter = new DialogAdjustAdapter(context,entities);
        dialogLv.setAdapter(dialogAdapter);

    }

    // 扫码商品调价
    public static void showSaoAdjustPrice(Context context, String img, String name, String stantPrice, String counterPrice, String price, DialogAdjustClickListenner dialogAdjustClickListenner, View.OnClickListener cancelClickListener) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(context).inflate(R.layout.dialog_sao_adjust_price_item, null);
        dialog = new AlertDialog.Builder(context).setView(view).create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        SimpleDraweeView sdv = (SimpleDraweeView) view.findViewById(R.id.sao_adjust_price_img);
        TextView goodsName = (TextView) view.findViewById(R.id.sao_adjust_price_name);
        TextView goodsStand = (TextView) view.findViewById(R.id.sao_adjust_price_stand_price);
        TextView goodsCounter = (TextView) view.findViewById(R.id.sao_adjust_price_counter_price);
        EditText goodsPrice = (EditText) view.findViewById(R.id.sao_adjust_price_etxt);
        TextView goodsMsg = (TextView) view.findViewById(R.id.sao_adjust_price_msg);
        TextView cancel = (TextView) view.findViewById(R.id.sao_adjust_price_cancel);
        TextView commit = (TextView) view.findViewById(R.id.sao_adjust_price_comit);

        ImageUtils.setSmallImg(sdv,img);
        goodsName.setText(name);
        goodsStand.setText(stantPrice);
        goodsCounter.setText(counterPrice);
        goodsPrice.setText(price);
        goodsMsg.setText("");

        cancel.setOnClickListener(cancelClickListener);
        commit.setOnClickListener(v -> dialogAdjustClickListenner.onClick(v,goodsPrice,goodsMsg));
        dialog.setCancelable(true);
        dialog.show();
//        setAlertDialogWidth(context);

    }

    public interface DialogAdjustClickListenner{
        void onClick(View v,EditText editText,TextView textView);
    }

    // 消单查询
    public static void showOrderMissDialog(Context context, String mTitle,DialogOrderMissBeginClickListenner dialogOrderMissBeginClickListenner ,DialogOrderMissOverClickListenner dialogOrderMissOverClickListenner,DialogOrderMissClickListenner dialogOrderMissClickListenner, View.OnClickListener onClickListener) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_order_miss, null);
        dialog = new AlertDialog.Builder(context).setView(view).create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }

        TextView title = (TextView) view.findViewById(R.id.order_miss_title);
        TextView begin = (TextView) view.findViewById(R.id.dialog_order_miss_begin);
        TextView over = (TextView) view.findViewById(R.id.dialog_order_miss_over);
        TextView cancel = (TextView) view.findViewById(R.id.dialog_order_miss_cancel);
        TextView sure = (TextView) view.findViewById(R.id.dialog_order_miss_sure);
        title.setText(mTitle);
        cancel.setOnClickListener(onClickListener);

        begin.setOnClickListener(v -> dialogOrderMissBeginClickListenner.onClick(v, begin));
        over.setOnClickListener(v -> dialogOrderMissOverClickListenner.onClick(v, over));
        sure.setOnClickListener(v -> dialogOrderMissClickListenner.onClick(v, begin , over));
        dialog.setCancelable(false);
        dialog.show();
        setAlertDialogWidth(context);
    }

    public interface DialogOrderMissBeginClickListenner {
        void onClick(View v, TextView begin);
    }

    public interface DialogOrderMissOverClickListenner {
        void onClick(View v, TextView over);
    }

    public interface DialogOrderMissClickListenner {
        void onClick(View v, TextView begin , TextView over);
    }

    // 周排行查询
    public static void showWeekDialog(Context context,String weeks, List<DataEntity> entities, DialogWeekSureListener dialogWeekSureListener, View.OnClickListener cancelClickListener) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_query_week, null);
        dialog = new AlertDialog.Builder(context).setView(view).create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        Spinner spinner = (Spinner) view.findViewById(R.id.dialog_spinner);
        TextView dialogCancel = (TextView) view.findViewById(R.id.dialog_query_week_cancel);
        TextView dialogSure = (TextView) view.findViewById(R.id.dialog_query_week_sure);

        dialogCancel.setOnClickListener(cancelClickListener);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String week = entities.get(position).getWeek();
                String start = entities.get(position).getStart();
                String end = entities.get(position).getEnd();
                dialogSure.setOnClickListener(v -> dialogWeekSureListener.onClick(v,week,start,end));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        dialog.setCancelable(false);
        dialog.show();
//        setAlertDialogWidth(context);
        WeekSpinnerAdapter dialogadapter = new WeekSpinnerAdapter(context,entities,weeks);
        spinner.setAdapter(dialogadapter);

    }

    public interface DialogWeekSureListener {
        void onClick(View v, String name,String start,String end);
    }

    public static void closeDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    private static void setAlertDialogWidth(Context context) {
        if (dialog.getWindow() != null) {
            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            params.width = CommonTools.dp2px(context, 270f);
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setAttributes(params);
        }
    }
}
