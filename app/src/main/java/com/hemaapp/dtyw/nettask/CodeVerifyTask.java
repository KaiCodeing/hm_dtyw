package com.hemaapp.dtyw.nettask;

import com.hemaapp.dtyw.DtywHttpInformation;
import com.hemaapp.dtyw.DtywNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;
/**
 * 随机码验证
 * @author lenovo
 *
 */
public class CodeVerifyTask extends DtywNetTask {

	public CodeVerifyTask(DtywHttpInformation information,
						  HashMap<String, String> params) {
		super(information, params);
	}

	public CodeVerifyTask(DtywHttpInformation information,
			HashMap<String, String> params, HashMap<String, String> files) {
		super(information, params, files);
	}

	@Override
	public Object parse(JSONObject jsonObject) throws DataParseException {
		return new Result(jsonObject);
	}
	private class Result extends HemaArrayResult<String> {

		public Result(JSONObject jsonObject) throws DataParseException {
			super(jsonObject);
		}

		@Override
		public String parse(JSONObject jsonObject) throws DataParseException {
			try {
				return get(jsonObject, "temp_token");
			} catch (JSONException e) {
				throw new DataParseException(e);
			}
		}

	}
}
