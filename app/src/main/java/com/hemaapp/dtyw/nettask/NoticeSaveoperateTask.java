package com.hemaapp.dtyw.nettask;

import com.hemaapp.dtyw.DtywHttpInformation;
import com.hemaapp.dtyw.DtywNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * 消息操作
 * @author lenovo
 *
 */
public class NoticeSaveoperateTask extends DtywNetTask {

	public NoticeSaveoperateTask(DtywHttpInformation information,
			HashMap<String, String> params) {
		super(information, params);
	}

	public NoticeSaveoperateTask(DtywHttpInformation information,
			HashMap<String, String> params, HashMap<String, String> files) {
		super(information, params, files);
	}

	@Override
	public Object parse(JSONObject jsonObject) throws DataParseException {
		return new HemaBaseResult(jsonObject);
	}

}
