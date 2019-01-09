package code.ytn.cn.shop.controller;

import android.os.Bundle;
import android.widget.TextView;

import com.common.network.FProtocol;
import com.common.viewinject.annotation.ViewInject;

import java.util.IdentityHashMap;

import code.ytn.cn.R;
import code.ytn.cn.base.ToolBarActivity;
import code.ytn.cn.network.Constants;
import code.ytn.cn.network.Parsers;
import code.ytn.cn.network.entity.MessageEntity;
import code.ytn.cn.utils.ViewInjectUtils;

import static code.ytn.cn.common.CommonConstant.ID;
import static code.ytn.cn.common.CommonConstant.REQUEST_NET_ONE;

/**
 * 消息详情
 */
public class MessageDetailActivity extends ToolBarActivity {

    @ViewInject(R.id.message_detail_title)
    private TextView msgDetailTitle;
    @ViewInject(R.id.message_detail_time)
    private TextView msgDetailTime;
    @ViewInject(R.id.message_detail_content)
    private TextView msgDetailContent;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);
        ViewInjectUtils.inject(this);
        setCenterTitle("消息中心");
        id = getIntent().getStringExtra(ID);
        // 查询详情
        messageDetail();
    }

    @Override
    protected void parseData(int requestCode, String data) {
        super.parseData(requestCode, data);
        MessageEntity.MessageListEntity detailEntity = Parsers.getMessageDetailEntity(data);
        switch (requestCode) {
            case REQUEST_NET_ONE:
                if (detailEntity != null) {
                    msgDetailTitle.setText(detailEntity.getMsgTitle());
                    msgDetailContent.setText(detailEntity.getMsgContent());
                    msgDetailTime.setText(detailEntity.getSendTime());
                }
                break;
        }
    }

    // 查询详情
    private void messageDetail() {
        showProgressDialog();
        IdentityHashMap<String, String> params = new IdentityHashMap<>();
        params.put("id", id);
        requestHttpData(Constants.Urls.URL_POST_MESSAGE_DETAIL, REQUEST_NET_ONE, FProtocol.HttpMethod.POST, params);
    }
}
