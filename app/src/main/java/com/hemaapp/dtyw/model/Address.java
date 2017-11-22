package com.hemaapp.dtyw.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2016/10/11.
 */
public class Address  extends XtomObject implements Serializable {
    private String id;
    private String name;
    private ArrayList<Secondtype> secondtypes ;
    public Address(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                id = get(jsonObject, "id");
                name = get(jsonObject, "name");
                if (!jsonObject.isNull("secondtype")
                        && !isNull(jsonObject.getString("secondtype"))) {
                    JSONArray jsonList = jsonObject.getJSONArray("secondtype");
                    int size = jsonList.length();
                    secondtypes = new ArrayList<Secondtype>();
                    for (int i = 0; i < size; i++)
                        secondtypes
                                .add(new Secondtype(jsonList.getJSONObject(i)));
                }
                log_i(toString());
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }

    @Override
    public String toString() {
        return "Address{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", secondtypes=" + secondtypes +
                '}';
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Secondtype> getSecondtypes() {
        return secondtypes;
    }

}
