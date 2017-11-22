package com.hemaapp.dtyw.nettask;

import com.hemaapp.dtyw.DtywHttpInformation;
import com.hemaapp.dtyw.DtywNetTask;
import com.hemaapp.dtyw.model.Car;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2016/10/25.
 * 购物车
 */
public class CarListTask extends DtywNetTask {

    public CarListTask(DtywHttpInformation information,
                       HashMap<String, String> params) {
        super(information, params);
        // TODO Auto-generated constructor stub
    }

    public CarListTask(DtywHttpInformation information,
                       HashMap<String, String> params, HashMap<String, String> files) {
        super(information, params, files);
    }

    @Override
    public Object parse(JSONObject jsonObject) throws DataParseException {
        // TODO Auto-generated method stub
        return new Result(jsonObject);
    }

    private class Result extends HemaArrayResult<Car> {

        public Result(JSONObject jsonObject) throws DataParseException {
            super(jsonObject);
        }

        @Override
        public Car parse(JSONObject jsonObject)
                throws DataParseException {
            return new Car(jsonObject);
        }

    }
}
