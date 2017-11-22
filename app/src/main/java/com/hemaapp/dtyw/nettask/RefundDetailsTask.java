package com.hemaapp.dtyw.nettask;

import com.hemaapp.dtyw.DtywHttpInformation;
import com.hemaapp.dtyw.DtywNetTask;
import com.hemaapp.dtyw.model.RefundDetails;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2016/11/1.
 */
public class RefundDetailsTask extends DtywNetTask {
    public RefundDetailsTask(DtywHttpInformation information,
                              HashMap<String, String> params) {
        super(information, params);
    }

    @Override
    public Object parse(JSONObject jsonObject) throws DataParseException {
        return new Result(jsonObject);
    }

    private class Result extends HemaArrayResult<RefundDetails> {

        public Result(JSONObject jsonObject) throws DataParseException {
            super(jsonObject);
        }
        @Override
        public RefundDetails parse(JSONObject jsonObject) throws DataParseException {
            return new RefundDetails(jsonObject);
        }
    }
}
