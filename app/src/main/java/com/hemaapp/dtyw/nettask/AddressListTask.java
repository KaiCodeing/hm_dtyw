package com.hemaapp.dtyw.nettask;

import com.hemaapp.dtyw.DtywHttpInformation;
import com.hemaapp.dtyw.DtywNetTask;
import com.hemaapp.dtyw.model.Address;
import com.hemaapp.hm_FrameWork.result.HemaPageArrayResult;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2016/10/11.
 * 地区列表
 *
 */
public class AddressListTask extends DtywNetTask {


    public AddressListTask(DtywHttpInformation information,
                        HashMap<String, String> params) {
        super(information, params);
        // TODO Auto-generated constructor stub
    }
    public AddressListTask(DtywHttpInformation information,
                        HashMap<String, String> params, HashMap<String, String> files) {
        super(information, params, files);
    }
    @Override
    public Object parse(JSONObject jsonObject) throws DataParseException {
        // TODO Auto-generated method stub
        return  new Result(jsonObject);
    }
    private class Result extends HemaPageArrayResult<Address> {

        public Result(JSONObject jsonObject) throws DataParseException {
            super(jsonObject);
        }

        @Override
        public Address parse(JSONObject jsonObject)
                throws DataParseException {
            return new Address(jsonObject);
        }

    }

}
