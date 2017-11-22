package com.hemaapp.dtyw.nettask;

import com.hemaapp.dtyw.DtywHttpInformation;
import com.hemaapp.dtyw.DtywNetTask;
import com.hemaapp.dtyw.model.OrderGet;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * 订单详情
 * Created by lenovo on 2016/10/31.
 */
public class OrderGetTask extends DtywNetTask {

    public OrderGetTask(DtywHttpInformation information,
                    HashMap<String, String> params) {
        super(information, params);
        // TODO Auto-generated constructor stub
    }
    public OrderGetTask(DtywHttpInformation information,
                    HashMap<String, String> params, HashMap<String, String> files) {
        super(information, params, files);
    }
    @Override
    public Object parse(JSONObject jsonObject) throws DataParseException {
        // TODO Auto-generated method stub
        return new Result(jsonObject);
    }
    private class Result extends HemaArrayResult<OrderGet> {

        public Result(JSONObject jsonObject) throws DataParseException {
            super(jsonObject);
        }

        @Override
        public OrderGet parse(JSONObject jsonObject)
                throws DataParseException {
            return new OrderGet(jsonObject);
        }

    }
}
