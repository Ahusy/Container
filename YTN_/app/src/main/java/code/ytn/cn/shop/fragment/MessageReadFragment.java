package code.ytn.cn.shop.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.common.adapter.CommonAdapter;
import com.common.adapter.CommonViewHolder;
import com.common.network.FProtocol;
import com.common.utils.ToastUtil;
import com.common.viewinject.annotation.ViewInject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.IdentityHashMap;
import java.util.List;

import code.ytn.cn.R;
import code.ytn.cn.base.BaseFragment;
import code.ytn.cn.login.utils.UserCenter;
import code.ytn.cn.network.Constants;
import code.ytn.cn.network.Parsers;
import code.ytn.cn.network.entity.Entity;
import code.ytn.cn.network.entity.MessageEntity;
import code.ytn.cn.shop.controller.MessageDetailActivity;
import code.ytn.cn.shop.view_.SwipeMenuLayout;
import code.ytn.cn.utils.TimeUtil;
import code.ytn.cn.utils.ViewInjectUtils;

import static code.ytn.cn.common.CommonConstant.REQUEST_NET_ONE;
import static code.ytn.cn.common.CommonConstant.REQUEST_NET_SUCCESS;
import static code.ytn.cn.common.CommonConstant.REQUEST_NET_TWO;

/**
 * 消息（已读）
 */

public class MessageReadFragment extends BaseFragment {

    @ViewInject(R.id.frag_message_all_lv)
    private ListView lv;
    List<MessageEntity.MessageListEntity> newMessageList = new ArrayList<>();
    private LocalBroadcastManager instance;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_message_all, container, false);
        ViewInjectUtils.inject(this, view);
        queryMessage();
        registerReceiver();
        return view;
    }

    @Override
    protected void parseData(int requestCode, String data) {
        super.parseData(requestCode, data);
        closeProgressDialog();
        MessageEntity messageEntity = Parsers.getMessageEntity(data);
        switch (requestCode) {
            case REQUEST_NET_ONE:
                if (messageEntity != null) {
                    List<MessageEntity.MessageListEntity> listEntities = messageEntity.getListEntities();
                    newMessageList.clear();
                    for (int i = 0; i < listEntities.size(); i++) {
                        if (listEntities.get(i).getMsgCode().equals("2")) {
                            newMessageList.add(listEntities.get(i));
                        }
                    }
                    lv.setAdapter(new CommonAdapter<MessageEntity.MessageListEntity>(getActivity(), newMessageList, R.layout.item_frag_message) {
                        @Override
                        public void convert(CommonViewHolder holder, MessageEntity.MessageListEntity messageListEntity, int position, View convertView) {
                            holder.getView(R.id.frag_mess_dot).setVisibility(messageListEntity.getMsgCode().equals("2") ? View.INVISIBLE : View.VISIBLE);
                            ((SwipeMenuLayout) holder.getConvertView()).setIos(true).setLeftSwipe(true);
                            holder.getView(R.id.btn_zd).setVisibility(View.GONE);

                            holder.setText(R.id.frag_mess_content, messageListEntity.getMsgContent());
                            holder.setText(R.id.frag_mess_title, messageListEntity.getMsgTitle());
                            try {
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:m:s");
                                Date time = simpleDateFormat.parse(messageListEntity.getSendTime());
                                holder.setText(R.id.frag_mess_time, TimeUtil.getFriendlytime(time));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            // 跳转详情
                            holder.setOnClickListener(R.id.rl_content, v ->
                                    startActivity(new Intent(getActivity(), MessageDetailActivity.class).putExtra("id", messageListEntity.getMsgId())));

//                            holder.setOnClickListener(R.id.btn_zd, v -> {
//                                //在ListView里，点击侧滑菜单上的选项时，如果想让侧滑菜单同时关闭，调用这句话
//                                ((SwipeMenuLayout) holder.getConvertView()).quickClose();
//                            });
                            holder.setOnClickListener(R.id.btn_delete, v -> {
                                //在ListView里，点击侧滑菜单上的选项时，如果想让侧滑菜单同时关闭，调用这句话
                                ((SwipeMenuLayout) holder.getConvertView()).quickClose();
                                //删除操作
                                deleteMsg(messageListEntity.getMsgId());
                                newMessageList.remove(position);
                                notifyDataSetChanged();
                                // 标记通知
                                Intent intent = new Intent("msg");
                                intent.putExtra("change", "msg_read");
                                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                            });
                        }
                    });
                }
                break;
            case REQUEST_NET_TWO:
                Entity result = Parsers.getResult(data);
                if (result.getResultCode().equals(REQUEST_NET_SUCCESS)) {
                    ToastUtil.showToast(getActivity(), "删除成功");
                }
                break;
        }
    }

    // 查询列表
    private void queryMessage() {
        showProgressDialog();
        IdentityHashMap<String, String> params = new IdentityHashMap<>();
        params.put("worker_id", UserCenter.getUserId(getActivity()));
        requestHttpData(Constants.Urls.URL_POST_QUERY_MESSAGE_LIST, REQUEST_NET_ONE, FProtocol.HttpMethod.POST, params);
    }

    // 删除
    private void deleteMsg(String id) {
        IdentityHashMap<String, String> params = new IdentityHashMap<>();
        params.put("id", id);
        requestHttpData(Constants.Urls.URL_POST_DELETE_MESSAGE, REQUEST_NET_TWO, FProtocol.HttpMethod.POST, params);
    }

    // 注册广播接收器
    private void registerReceiver(){
        instance = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("msg");
        instance.registerReceiver(mAdDownLoadReceiver,intentFilter);
    }

    private BroadcastReceiver mAdDownLoadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String change = intent.getStringExtra("change");
            if ("msg_all".equals(change) || "msg_uRead".equals(change)){
                new Handler().post(() -> queryMessage());
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        queryMessage();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        instance.unregisterReceiver(mAdDownLoadReceiver);
    }

}