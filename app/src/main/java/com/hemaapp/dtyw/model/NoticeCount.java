package com.hemaapp.dtyw.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2016/10/17.
 */
public class NoticeCount extends XtomObject implements Serializable {
    private String num;//系统通知未读数
//    private String reply_notice_count;//评论通知未读数
    public NoticeCount(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                num = get(jsonObject, "num");
            //    reply_notice_count = get(jsonObject, "reply_notice_count");

                log_i(toString());
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }

    @Override
    public String toString() {
        return "NoticeCount{" +
                "num='" + num + '\'' +
                '}';
    }

    public String getNum() {
        return num;
    }
}
