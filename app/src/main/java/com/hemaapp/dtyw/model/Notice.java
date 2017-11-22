package com.hemaapp.dtyw.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import xtom.frame.XtomObject;
import xtom.frame.exception.DataParseException;

/**
 * Created by lenovo on 2016/10/14.
 */
public class Notice extends XtomObject implements Serializable {
    private String id;
    private String keytype;//上传操作类型
    private String keyid;//关联模块主键id
    private String content;//通知内容
    private String nickname;//昵称
    private String avatar;//头像
    private String client_id;//通知所属用户主键id
    private String from_id;//通知来源用户主键id
    private String looktype;//标记位 1未读 2已读 3可扩展为已同意状态等
    private String regdate;//通知日期
    private String goodsid;//商品id
    private String cartid;//购物车id
    public Notice(JSONObject jsonObject) throws DataParseException {
        if (jsonObject != null) {
            try {
                id = get(jsonObject, "id");
                keytype = get(jsonObject, "keytype");
                keyid = get(jsonObject, "keyid");
                content = get(jsonObject, "content");
                nickname = get(jsonObject, "nickname");
                avatar = get(jsonObject, "avatar");
                client_id = get(jsonObject, "client_id");
                from_id = get(jsonObject, "from_id");
                looktype = get(jsonObject, "looktype");
                regdate = get(jsonObject, "regdate");
                goodsid = get(jsonObject, "goodsid");
                cartid = get(jsonObject, "cartid");
                log_i(toString());
            } catch (JSONException e) {
                throw new DataParseException(e);
            }
        }
    }

    @Override
    public String toString() {
        return "Notice{" +
                "avatar='" + avatar + '\'' +
                ", id='" + id + '\'' +
                ", keytype='" + keytype + '\'' +
                ", keyid='" + keyid + '\'' +
                ", content='" + content + '\'' +
                ", nickname='" + nickname + '\'' +
                ", client_id='" + client_id + '\'' +
                ", from_id='" + from_id + '\'' +
                ", looktype='" + looktype + '\'' +
                ", regdate='" + regdate + '\'' +
                ", goodsid='" + goodsid + '\'' +
                ", cartid='" + cartid + '\'' +
                '}';
    }

    public String getCartid() {
        return cartid;
    }

    public String getGoodsid() {
        return goodsid;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getClient_id() {
        return client_id;
    }

    public String getContent() {
        return content;
    }

    public String getFrom_id() {
        return from_id;
    }

    public String getId() {
        return id;
    }

    public String getKeyid() {
        return keyid;
    }

    public String getKeytype() {
        return keytype;
    }

    public String getLooktype() {
        return looktype;
    }

    public String getNickname() {
        return nickname;
    }

    public String getRegdate() {
        return regdate;
    }
}
