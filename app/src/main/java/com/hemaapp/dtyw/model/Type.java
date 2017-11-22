package com.hemaapp.dtyw.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2016/10/20.
 * 类型列表
 */
public class Type extends XtomObject implements Serializable {
    private String id, name;
    private ArrayList<TypeChlid> children;
    private boolean check;
    public Type(JSONObject jsonObject) throws DataParseException {
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
        return "Type{" +
                "check=" + check +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", children=" + children +
                '}';
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public boolean isCheck() {
        return check;
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




}
