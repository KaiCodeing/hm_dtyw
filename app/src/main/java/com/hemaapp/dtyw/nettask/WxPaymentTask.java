package com.hemaapp.dtyw.nettask;

import com.hemaapp.dtyw.DtywHttpInformation;
import com.hemaapp.dtyw.DtywNetTask;
import com.hemaapp.dtyw.model.WxPayment;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;
/**
 * ΢微信支付
 * @author lenovo
 *
 */
public class WxPaymentTask extends DtywNetTask {

	public WxPaymentTask(DtywHttpInformation information,
			HashMap<String, String> params) {
		super(information, params);
	}

	public WxPaymentTask(DtywHttpInformation information,
			HashMap<String, String> params, HashMap<String, String> files) {
		super(information, params, files);
	}

	@Override
	public Object parse(JSONObject jsonObject) throws DataParseException {
		return new Result(jsonObject);
	}

	private class Result extends HemaArrayResult<WxPayment> {

		public Result(JSONObject jsonObject) throws DataParseException {
			super(jsonObject);
		}

		@Override
		public WxPayment parse(JSONObject jsonObject)
				throws DataParseException {
			return new WxPayment(jsonObject);
		}

	}
}
