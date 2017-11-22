package com.hemaapp.dtyw.nettask;

import com.hemaapp.dtyw.DtywHttpInformation;
import com.hemaapp.dtyw.DtywNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2016/10/26.
 * 删除地址
 */
public class ShipaddressRemoveTask extends DtywNetTask{

    public ShipaddressRemoveTask(DtywHttpInformation information,
                         HashMap<String, String> params) {
        super(information, params);
    }

    public ShipaddressRemoveTask(DtywHttpInformation information,
                         HashMap<String, String> params, HashMap<String, String> files) {
        super(information, params, files);
    }

    @Override
    public Object parse(JSONObject jsonObject) throws DataParseException {
        return new HemaBaseResult(jsonObject);
    }


}
