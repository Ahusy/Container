package code.ytn.cn.network.entity;

import com.google.gson.annotations.SerializedName;

/**
 * 实体基类
 */
public class Entity {

	@SerializedName("result_code")
	private String resultCode;
	@SerializedName("result_msg")
	private String resultMsg;

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
}
