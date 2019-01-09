package code.ytn.cn.network.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dell on 2018/4/16
 */

public class ReplmentListEntity {
    @SerializedName("status")
    private String status;
    @SerializedName("create_user_name")
    private String createUserName;
    @SerializedName("create_date")
    private String createData;
    @SerializedName("replenish_code")
    private String replenCode;
    @SerializedName("cabinet_name")
    private String cabinetName;
    @SerializedName("id")
    private String id;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getCreateData() {
        return createData;
    }

    public void setCreateData(String createData) {
        this.createData = createData;
    }

    public String getReplenCode() {
        return replenCode;
    }

    public void setReplenCode(String replenCode) {
        this.replenCode = replenCode;
    }

    public String getCabinetName() {
        return cabinetName;
    }

    public void setCabinetName(String cabinetName) {
        this.cabinetName = cabinetName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
