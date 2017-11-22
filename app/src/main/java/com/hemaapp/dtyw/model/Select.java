package com.hemaapp.dtyw.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2016/10/27.
 * 选择账户
 */
public class Select extends XtomObject implements Serializable {
    private String username;
    private String nickname;
    private String password;
    private String avatar;
    public Select(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                username = get(jsonObject, "username");
                nickname = get(jsonObject, "nickname");
                password = get(jsonObject, "password");
                avatar = get(jsonObject, "avatar");
                log_i(toString());
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }

    public Select( String username, String nickname,String password,String avatar) {

        this.avatar = avatar;
        this.nickname = nickname;
        this.password = password;
        this.username = username;
    }

    @Override
    public String toString() {
        return "Select{" +
                "avatar='" + avatar + '\'' +
                ", username='" + username + '\'' +
                ", nickname='" + nickname + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }
}
