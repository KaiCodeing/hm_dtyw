package com.hemaapp.dtyw.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2016/10/12.
 * 公告
 */
public class Bulletin extends XtomObject implements Serializable {
    private String id;
    private String name;
    private String jumptype;
    private String jumpid;
    private String jumpurl;
    public Bulletin(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                id = get(jsonObject, "id");
                name = get(jsonObject, "name");
                jumptype = get(jsonObject, "jumptype");
                jumpid = get(jsonObject, "jumpid");
                jumpurl = get(jsonObject, "jumpurl");
                log_i(toString());
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }

    @Override
    public String toString() {
        return "Bulletin{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", jumptype='" + jumptype + '\'' +
                ", jumpid='" + jumpid + '\'' +
                ", jumpurl='" + jumpurl + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public String getJumpid() {
        return jumpid;
    }

    public String getJumptype() {
        return jumptype;
    }

    public String getJumpurl() {
        return jumpurl;
    }

    public String getName() {
        return name;
    }
}
