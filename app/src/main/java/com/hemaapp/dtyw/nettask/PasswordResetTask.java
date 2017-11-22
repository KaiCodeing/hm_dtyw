package com.hemaapp.dtyw.nettask;

import com.hemaapp.dtyw.DtywHttpInformation;
import com.hemaapp.dtyw.DtywNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;


/**
 * 密码重设
 * @author lenovo
 *
 */
public class PasswordResetTask extends DtywNetTask {

	public PasswordResetTask(DtywHttpInformation information,
			HashMap<String, String> params) {
		super(information, params);
	}

	public PasswordResetTask(DtywHttpInformation information,
			HashMap<String, String> params, HashMap<String, String> files) {
		super(information, params, files);
	}

	@Override
	public Object parse(JSONObject jsonObject) throws DataParseException {
		return new HemaBaseResult(jsonObject);
	}

}
