package com.hemaapp.dtyw.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2016/10/17.
 * 规格
 */
public class Property extends XtomObject implements Serializable {
    private String id;
    private String  properyname;
    private ArrayList<Value> value;
    public Property(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                id = get(jsonObject, "id");
                properyname = get(jsonObject, "properyname");
                if (!jsonObject.isNull("value")
                        && !isNull(jsonObject.getString("value"))) {
                    JSONArray jsonList = jsonObject.getJSONArray("value");
                    int size = jsonList.length();
                    value = new ArrayList<Value>();
                    for (int i = 0; i < size; i++)
                        value
                                .add(new Value(jsonList.getJSONObject(i)));
                }
                log_i(toString());
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }

    @Override
    public String toString() {
        return "Property{" +
                "id='" + id + '\'' +
                ", properyname='" + properyname + '\'' +
                ", value=" + value +
                '}';
    }

    public String getId() {
        return id;
    }

    public String getProperyname() {
        return properyname;
    }

    public ArrayList<Value> getValue() {
        return value;
    }
}
