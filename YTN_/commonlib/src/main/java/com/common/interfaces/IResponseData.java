package com.common.interfaces;

import com.common.network.FProtocol;

public interface IResponseData {

	void resultDataSuccess(int requestCode, String data);

	void resultDataMistake(int requestCode, FProtocol.NetDataProtocol.ResponseStatus responseStatus, String errorMessage);
}
