package code.ytn.cn.network.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by dell on 2018/3/9
 */

public class CounterEntity {
    @SerializedName("result_code")
    private String resultCode;
    @SerializedName("result_msg")
    private String resultMsg;

    @SerializedName("cabinet_list")
    private List<CounterListEntity> counterListEntities;

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public List<CounterListEntity> getCounterListEntities() {
        return counterListEntities;
    }

    public void setCounterListEntities(List<CounterListEntity> counterListEntities) {
        this.counterListEntities = counterListEntities;
    }
}
