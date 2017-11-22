package com.hemaapp.dtyw.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * ΢��֧��
 * 
 * @author lenovo
 * 
 */
public class WxPayment extends XtomObject implements Serializable {
	private String appid;// 公众账号ID
	private String partnerid;// 商户号
	private String prepayid;// 预支付交易会话ID
	private String packageS;// 扩展字段
	private String noncestr;// 随机字符串
	private String timestamp;// 时间戳
	private String sign;// 签名

	public WxPayment(JSONObject jsonObject) throws DataParseException {
		if (jsonObject != null) {
			try {
				appid = get(jsonObject, "appid");
				partnerid = get(jsonObject, "partnerid");
				prepayid = get(jsonObject, "prepayid");
				packageS = get(jsonObject, "package");
				noncestr = get(jsonObject, "noncestr");
				timestamp = get(jsonObject, "timestamp");
				sign = get(jsonObject, "sign");
				log_i(toString());
			} catch (JSONException e) {
				throw new DataParseException(e);
			}
		}
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "WxPayment=[appid=" + appid + ",partnerid=" + partnerid
				+ ",prepayid=" + prepayid + ",packageS=" + packageS + ","
				+ "noncestr=" + noncestr + ",timestamp=" + timestamp + ",sign="
				+ sign + "]";
	}

	public String getAppid() {
		return appid;
	}

	public String getPartnerid() {
		return partnerid;
	}

	public String getPrepayid() {
		return prepayid;
	}

	public String getPackageS() {
		return packageS;
	}

	public String getNoncestr() {
		return noncestr;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public String getSign() {
		return sign;
	}
	
}
