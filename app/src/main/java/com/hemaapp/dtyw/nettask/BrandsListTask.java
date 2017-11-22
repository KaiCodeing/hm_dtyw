package com.hemaapp.dtyw.nettask;

import com.hemaapp.dtyw.DtywHttpInformation;
import com.hemaapp.dtyw.DtywNetTask;
import com.hemaapp.dtyw.model.Brands;
import com.hemaapp.hm_FrameWork.result.HemaPageArrayResult;

import org.json.JSONObject;

import java.util.HashMap;

import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2016/10/12.
 * 品牌列表
 */
public class BrandsListTask extends DtywNetTask {
    public BrandsListTask(DtywHttpInformation information,
                            HashMap<String, String> params) {
        super(information, params);
    }

    @Override
    public Object parse(JSONObject jsonObject) throws DataParseException {
        return new Result(jsonObject);
    }

    private class Result extends HemaPageArrayResult<Brands> {

        public Result(JSONObject jsonObject) throws DataParseException {
            super(jsonObject);
        }

        @Override
        public Brands parse(JSONObject jsonObject) throws DataParseException {
            return new Brands(jsonObject);
        }

    }
}
