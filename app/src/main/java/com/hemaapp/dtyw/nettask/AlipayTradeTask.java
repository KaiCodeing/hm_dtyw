/*
 * Copyright (C) 2014 The Android Client Of SCMS Project
 * 
 *     The BeiJing PingChuanJiaHeng Technology Co., Ltd.
 * 
 * Author:Yang ZiTian
 * You Can Contact QQ:646172820 Or Email:mail_yzt@163.com
 */
package com.hemaapp.dtyw.nettask;

import com.hemaapp.dtyw.DtywHttpInformation;
import com.hemaapp.dtyw.DtywNetTask;
import com.hemaapp.dtyw.model.AlipayTrade;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * 获取支付宝交易签名
 */
public class AlipayTradeTask extends DtywNetTask {

	public AlipayTradeTask(DtywHttpInformation information,
			HashMap<String, String> params) {
		super(information, params);
	}

	public AlipayTradeTask(DtywHttpInformation information,
			HashMap<String, String> params, HashMap<String, String> files) {
		super(information, params, files);
	}

	@Override
	public Object parse(JSONObject jsonObject) throws DataParseException {
		return new Result(jsonObject);
	}

	private class Result extends HemaArrayResult<AlipayTrade> {

		public Result(JSONObject jsonObject) throws DataParseException {
			super(jsonObject);
		}

		@Override
		public AlipayTrade parse(JSONObject jsonObject)
				throws DataParseException {
			return new AlipayTrade(jsonObject);
		}

	}
}
