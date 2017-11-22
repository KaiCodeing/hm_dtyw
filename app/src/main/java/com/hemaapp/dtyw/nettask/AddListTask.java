package com.hemaapp.dtyw.nettask;

import com.hemaapp.dtyw.DtywHttpInformation;
import com.hemaapp.dtyw.DtywNetTask;
import com.hemaapp.dtyw.model.AddressOne;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2016/10/21.
 * 一二级列表
 */
public class AddListTask extends DtywNetTask{

    public AddListTask(DtywHttpInformation information,
                           HashMap<String, String> params) {
        super(information, params);
        // TODO Auto-generated constructor stub
    }
    public AddListTask(DtywHttpInformation information,
                           HashMap<String, String> params, HashMap<String, String> files) {
        super(information, params, files);
    }
    @Override
    public Object parse(JSONObject jsonObject) throws DataParseException {
        // TODO Auto-generated method stub
        return  new Result(jsonObject);
    }
    private class Result extends HemaArrayResult<AddressOne> {

        public Result(JSONObject jsonObject) throws DataParseException {
            super(jsonObject);
        }

        @Override
        public AddressOne parse(JSONObject jsonObject)
                throws DataParseException {
            return new AddressOne(jsonObject);
        }

    }
}
