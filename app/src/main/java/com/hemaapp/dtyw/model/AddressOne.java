package com.hemaapp.dtyw.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2016/10/21.
 * 一级地址列表
 */
public class AddressOne extends XtomObject implements Serializable {
    private String id;
    private String name;
    private boolean check;
    private ArrayList<TypeChlid> children;
    public AddressOne(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                id = get(jsonObject, "id");
                name = get(jsonObject, "name");
                check = false;
                if (!jsonObject.isNull("children")
                        && !isNull(jsonObject.getString("children"))) {
                    JSONArray jsonList = jsonObject.getJSONArray("children");
                    int size = jsonList.length();
                    children = new ArrayList<TypeChlid>();
                    for (int i = 0; i < size; i++)
                        children
                                .add(new TypeChlid(jsonList.getJSONObject(i)));
                }
                log_i(toString());
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }

    @Override
    public String toString() {
        return "AddressOne{" +
                "children=" + children +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public ArrayList<TypeChlid> getChildren() {
        return children;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
