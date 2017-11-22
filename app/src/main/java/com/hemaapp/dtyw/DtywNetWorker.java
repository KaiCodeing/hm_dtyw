package com.hemaapp.dtyw;

import android.content.Context;

import com.hemaapp.dtyw.nettask.AdListTask;
import com.hemaapp.dtyw.nettask.AddListTask;
import com.hemaapp.dtyw.nettask.AlipayTradeTask;
import com.hemaapp.dtyw.nettask.BankListTask;
import com.hemaapp.dtyw.nettask.BrandsListTask;
import com.hemaapp.dtyw.nettask.BrandsTwTask;
import com.hemaapp.dtyw.nettask.BulletinListTask;
import com.hemaapp.dtyw.nettask.CarListTask;
import com.hemaapp.dtyw.nettask.CartAddTask;
import com.hemaapp.dtyw.nettask.CartSaveoperateTask;
import com.hemaapp.dtyw.nettask.ClientAddTask;
import com.hemaapp.dtyw.nettask.ClientLoginTask;
import com.hemaapp.dtyw.nettask.ClientLoginoutTask;
import com.hemaapp.dtyw.nettask.ClientVerifyTask;
import com.hemaapp.dtyw.nettask.CodeGetTask;
import com.hemaapp.dtyw.nettask.CodeVerifyTask;
import com.hemaapp.dtyw.nettask.CollectListTask;
import com.hemaapp.dtyw.nettask.DistrictAllListTask;
import com.hemaapp.dtyw.nettask.FeeaccountRemoveTask;
import com.hemaapp.dtyw.nettask.FileUploadTask;
import com.hemaapp.dtyw.nettask.GetCartgoodsnumTask;
import com.hemaapp.dtyw.nettask.GetDefaultaddTask;
import com.hemaapp.dtyw.nettask.GetFeeaccountTask;
import com.hemaapp.dtyw.nettask.GoodsBuyTask;
import com.hemaapp.dtyw.nettask.GoodsGetTask;
import com.hemaapp.dtyw.nettask.GoodsListTask;
import com.hemaapp.dtyw.nettask.GoodsOperateTask;
import com.hemaapp.dtyw.nettask.InitTask;
import com.hemaapp.dtyw.nettask.InstallserviceListTask;
import com.hemaapp.dtyw.nettask.NoreadNoticeCountTask;
import com.hemaapp.dtyw.nettask.NoticeListTask;
import com.hemaapp.dtyw.nettask.NoticeSaveoperateTask;
import com.hemaapp.dtyw.nettask.NoticeTask;
import com.hemaapp.dtyw.nettask.OrderAddTask;
import com.hemaapp.dtyw.nettask.OrderGetTask;
import com.hemaapp.dtyw.nettask.OrderListTask;
import com.hemaapp.dtyw.nettask.OrderNumTask;
import com.hemaapp.dtyw.nettask.PasswordResetTask;
import com.hemaapp.dtyw.nettask.PayListTask;
import com.hemaapp.dtyw.nettask.RefundDetailsTask;
import com.hemaapp.dtyw.nettask.ReplyAddTask;
import com.hemaapp.dtyw.nettask.ReplyListTask;
import com.hemaapp.dtyw.nettask.RequirementGetTask;
import com.hemaapp.dtyw.nettask.RequirementListTask;
import com.hemaapp.dtyw.nettask.SetDefaultaddTask;
import com.hemaapp.dtyw.nettask.ShipaddressAddTask;
import com.hemaapp.dtyw.nettask.ShipaddressListTask;
import com.hemaapp.dtyw.nettask.ShipaddressRemoveTask;
import com.hemaapp.dtyw.nettask.ShipaddressSaveTask;
import com.hemaapp.dtyw.nettask.ThirdUrlTask;
import com.hemaapp.dtyw.nettask.TypeListTask;
import com.hemaapp.dtyw.nettask.UnionTradeTask;
import com.hemaapp.dtyw.nettask.WxPaymentTask;
import com.hemaapp.dtyw.nettask.orderReturnTask;
import com.hemaapp.dtyw.nettask.pricerangeListTask;
import com.hemaapp.hm_FrameWork.HemaNetWorker;
import com.hemaapp.hm_FrameWork.HemaUtil;

import java.util.HashMap;

import xtom.frame.util.XtomDeviceUuidFactory;
import xtom.frame.util.XtomSharedPreferencesUtil;

/**
 * Created by lenovo on 2016/9/6.
 */
public class DtywNetWorker extends HemaNetWorker {
    /**
     * 实例化网络请求工具类
     *
     * @param mContext
     */
    private Context mContext;

    public DtywNetWorker(Context mContext) {

        super(mContext);
        this.mContext = mContext;


    }

    @Override
    public void clientLogin() {

        DtywHttpInformation information = DtywHttpInformation.CLIENT_LOGIN;
        HashMap<String, String> params = new HashMap<String, String>();
        String username = XtomSharedPreferencesUtil.get(mContext, "username");
        params.put("username", username);// 用户登录名 手机号或邮箱
        String password = XtomSharedPreferencesUtil.get(mContext, "password");
        params.put("password", password); // 登陆密码 服务器端存储的是32位的MD5加密串
        params.put("devicetype", "2"); // 用户登录所用手机类型 1：苹果 2：安卓（方便服务器运维统计）
        String version = HemaUtil.getAppVersionForSever(mContext);
        params.put("lastloginversion", version);// 登陆所用的系统版本号,记录用户的登录版本，方便服务器运维统计

        DtywNetTask task = new ClientLoginTask(information, params);
        executeTask(task);

    }

    @Override
    public boolean thirdSave() {
        return false;
    }

    /**
     * @param
     * @param
     * @方法名称: inIt
     * @功能描述: TODO初始化
     * @返回值: void
     */
    public void inIt() {
        DtywHttpInformation information = DtywHttpInformation.INIT;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("lastloginversion", HemaUtil.getAppVersionForSever(mContext));// 版本号
        params.put("device_sn", XtomDeviceUuidFactory.get(mContext));
        DtywNetTask netTask = new InitTask(information, params);
        executeTask(netTask);
    }

    /**
     * 登录
     *
     * @param username
     * @param password
     */
    public void clientLogin(String username, String password) {
        DtywHttpInformation information = DtywHttpInformation.CLIENT_LOGIN;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", username);// 版本号
        params.put("password", password);
        params.put("devicetype", "2"); // 用户登录所用手机类型 1：苹果 2：安卓（方便服务器运维统计）
        String version = HemaUtil.getAppVersionForSever(mContext);
        params.put("lastloginversion", version);// 登陆所用的系统版本号,记录用户的登录版本，方便服务器运维统计
        DtywNetTask netTask = new ClientLoginTask(information, params);
        executeTask(netTask);
    }

    /**
     * @param username
     * @param code
     * @方法名称: codeVerify
     * @功能描述: TODO验证验证码
     * @返回值: void
     */
    public void codeVerify(String username, String code) {
        DtywHttpInformation information = DtywHttpInformation.CODE_VERIFY;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", username);
        params.put("code", code);
        DtywNetTask task = new CodeVerifyTask(information, params);
        executeTask(task);
    }

    /**
     * @param username
     * @方法名称: clientVerify
     * @功能描述: TODO验证用户是否注册过
     * @返回值: void
     */
    public void clientVerify(String username) {
        DtywHttpInformation information = DtywHttpInformation.CLIENT_VERIFY;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", username);

        DtywNetTask task = new ClientVerifyTask(information, params);
        executeTask(task);
    }

    /**
     * @param username
     * @方法名称: codeGet
     * @功能描述: TODO发送验证码
     * @返回值: void
     */
    public void codeGet(String username) {
        DtywHttpInformation information = DtywHttpInformation.CODE_GET;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", username);
        DtywNetTask task = new CodeGetTask(information, params);
        executeTask(task);
    }

    /**
     * addlistall_list
     * 地区列表
     */
    public void addListAllList() {
        DtywHttpInformation information = DtywHttpInformation.ADDLISTALL_LIST;
        HashMap<String, String> params = new HashMap<String, String>();
        DtywNetTask task = new DistrictAllListTask(information, params);
        executeTask(task);
    }

    /**
     * @param token
     * @param keytype
     * @param keyid
     * @param orderby
     * @param temp_file
     * @方法名称: fileUpload
     * @功能描述: TODO上传文件
     * @返回值: void
     */
    public void fileUpload(String token, String keytype, String keyid, String orderid,
                           String duration, String orderby, String content, String temp_file) {
        DtywHttpInformation information = DtywHttpInformation.FILE_UPLOAD;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("keytype", keytype);
        params.put("keyid", keyid);
        params.put("orderby", orderby);
        params.put("duration", duration);
        params.put("content", content);
        params.put("orderid", orderid);
        HashMap<String, String> files = new HashMap<String, String>();
        files.put("temp_file", temp_file);
        DtywNetTask task = new FileUploadTask(information, params, files);
        executeTask(task);
    }

    /**
     * @param temp_token
     * @param username
     * @param password
     * @方法名称: clientAdd
     * @功能描述: TODO 用户注册
     * @返回值: void
     */
    public void clientAdd(String temp_token, String username, String password,
                          String nickname, String location) {
        DtywHttpInformation information = DtywHttpInformation.CLIENT_ADD;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("temp_token", temp_token);
        params.put("username", username);
        params.put("password", password);
        params.put("nickname", nickname);
        params.put("location", location);
        DtywNetTask task = new ClientAddTask(information, params);
        executeTask(task);
    }

    /**
     * @param temp_token
     * @param keytype
     * @param new_password
     * @方法名称: passwordReset
     * @功能描述: TODO修改密码
     * @返回值: void
     */
    public void passwordReset(String temp_token, String keytype,
                              String new_password) {
        DtywHttpInformation information = DtywHttpInformation.PASSWORD_RESET;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("temp_token", temp_token);
        params.put("keytype", keytype);
        params.put("new_password", new_password);
        DtywNetTask task = new PasswordResetTask(information, params);
        executeTask(task);
    }

    /**
     * 广告页
     */
    public void adList(String keytype) {
        DtywHttpInformation information = DtywHttpInformation.AD_LIST;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("keytype", keytype);
        DtywNetTask task = new AdListTask(information, params);
        executeTask(task);
    }

    /**
     * 公告
     */
    public void bulletinList() {
        DtywHttpInformation information = DtywHttpInformation.BULLETIN_LIST;
        HashMap<String, String> params = new HashMap<String, String>();

        DtywNetTask task = new BulletinListTask(information, params);
        executeTask(task);
    }

    /**
     * 品牌
     */
    public void brandsList(String keytype, String page) {
        DtywHttpInformation information = DtywHttpInformation.BRANDS_LIST;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("keytype", keytype);
        params.put("page", page);
        DtywNetTask task = new BrandsListTask(information, params);
        executeTask(task);
    }

    /**
     * 商品
     */
    public void goodsList(String keytype, String brandid, String typeid, String orderby
            , String place, String priceid, String keyword, String page) {
        DtywHttpInformation information = DtywHttpInformation.GOODS_LIST;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("keytype", keytype);
        params.put("brandid", brandid);
        params.put("typeid", typeid);
        params.put("orderby", orderby);
        params.put("place", place);
        params.put("priceid", priceid);
        params.put("keyword", keyword);
        params.put("page", page);
        DtywNetTask task = new GoodsListTask(information, params);
        executeTask(task);
    }

    /**
     * 消息列表
     */
    public void noticeList(String token, String noticetype, String page) {
        DtywHttpInformation information = DtywHttpInformation.NOTICE_LIST;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("noticetype", noticetype);
        params.put("page", page);
        DtywNetTask task = new NoticeListTask(information, params);
        executeTask(task);
    }

    /**
     * 消息操作
     */
    public void noticeSaveoperate(String token, String id, String operatetype) {
        DtywHttpInformation information = DtywHttpInformation.NOTICE_SAVEOPERATE;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("id", id);
        params.put("operatetype", operatetype);
        DtywNetTask task = new NoticeSaveoperateTask(information, params);
        executeTask(task);
    }

    /**
     * 未读通知数
     */
    public void noreadNoticeCount(String token) {
        DtywHttpInformation information = DtywHttpInformation.NOREAD_UNREAD;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        DtywNetTask task = new NoreadNoticeCountTask(information, params);
        executeTask(task);
    }

    /**
     * 获取商品详情
     */
    public void goodsGet(String token, String id) {
        DtywHttpInformation information = DtywHttpInformation.GOODS_GET;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("id", id);
        DtywNetTask task = new GoodsGetTask(information, params);
        executeTask(task);
    }

    /**
     * 安装服务
     */
    public void installserviceList(String token, String address,String goodsid) {
        DtywHttpInformation information = DtywHttpInformation.INSTALLSERVICE_LIST;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("address", address);
        params.put("goodsid", goodsid);
        DtywNetTask task = new InstallserviceListTask(information, params);
        executeTask(task);
    }

    /**
     * 评论列表
     */
    public void replyList(String keytype, String keyid, String page) {
        DtywHttpInformation information = DtywHttpInformation.REPLY_LIST;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("keytype", keytype);
        params.put("keyid", keyid);
        params.put("page", page);
        DtywNetTask task = new ReplyListTask(information, params);
        executeTask(task);
    }

    /**
     * 收藏操作
     */
    public void goodsOperate(String token, String keytype, String id) {
        DtywHttpInformation information = DtywHttpInformation.GOODS_OPERATE;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("keytype", keytype);
        params.put("id", id);
        DtywNetTask task = new GoodsOperateTask(information, params);
        executeTask(task);
    }

    /**
     * 加入购物车
     */
    public void cartAdd(String token, String buycount, String keyid, String propertyid, String serviceid) {
        DtywHttpInformation information = DtywHttpInformation.CART_ADD;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("buycount", buycount);
        params.put("keyid", keyid);
        params.put("propertyid", propertyid);
        params.put("serviceid", serviceid);
        DtywNetTask task = new CartAddTask(information, params);
        executeTask(task);
    }

    /**
     * 购物车中商品数量
     */
    public void getCartgoodsnum(String token) {
        DtywHttpInformation information = DtywHttpInformation.GET_CARTGOODSNUM;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        DtywNetTask task = new GetCartgoodsnumTask(information, params);
        executeTask(task);
    }

    /**
     * BrandsTw 全部品牌
     */
    public void brandsTw() {
        DtywHttpInformation information = DtywHttpInformation.BRANDS_TW;
        HashMap<String, String> params = new HashMap<String, String>();

        DtywNetTask task = new BrandsTwTask(information, params);
        executeTask(task);
    }

    /**
     * 类型列表
     */
    public void typeList() {
        DtywHttpInformation information = DtywHttpInformation.ALLTYPE_LIST;
        HashMap<String, String> params = new HashMap<String, String>();
        DtywNetTask task = new TypeListTask(information, params);
        executeTask(task);
    }

    /**
     * 价格区间
     */
    public void pricerangeList() {
        DtywHttpInformation information = DtywHttpInformation.PRICERANGE_LIST;
        HashMap<String, String> params = new HashMap<String, String>();
        DtywNetTask task = new pricerangeListTask(information, params);
        executeTask(task);
    }

    /**
     * 一二级地址列表
     */
    public void addList() {
        DtywHttpInformation information = DtywHttpInformation.ADD_LIST;
        HashMap<String, String> params = new HashMap<String, String>();
        DtywNetTask task = new AddListTask(information, params);
        executeTask(task);
    }

    /**
     * 购物车列表
     */
    public void cartList(String token) {
        DtywHttpInformation information = DtywHttpInformation.CART_LIST;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        DtywNetTask task = new CarListTask(information, params);
        executeTask(task);
    }

    /**
     * 购物车操作
     */
    public void cartSaveoperate(String token, String keytype, String cartid, String num) {
        DtywHttpInformation information = DtywHttpInformation.CART_SAVEOPERATE;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("keytype", keytype);
        params.put("cartid", cartid);
        params.put("num", num);
        DtywNetTask task = new CartSaveoperateTask(information, params);
        executeTask(task);
    }

    /**
     * 默认地址
     */
    public void getDefaultadd(String token) {
        DtywHttpInformation information = DtywHttpInformation.GET_DEFAULTADD;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        DtywNetTask task = new GetDefaultaddTask(information, params);
        executeTask(task);
    }

    /**
     * 收货地址列表
     */
    public void shipaddressList(String token) {
        DtywHttpInformation information = DtywHttpInformation.SHIPADDRESS_LIST;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        DtywNetTask task = new ShipaddressListTask(information, params);
        executeTask(task);
    }

    /**
     * 设置默认收货地址
     */
    public void setDefaultadd(String token, String id) {
        DtywHttpInformation information = DtywHttpInformation.SETDEFAULTADD;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("id", id);
        DtywNetTask task = new SetDefaultaddTask(information, params);
        executeTask(task);
    }

    /**
     * 删除收货地址
     */
    public void shipaddressRemove(String token, String id) {
        DtywHttpInformation information = DtywHttpInformation.SHIPADDRESS_REMOVE;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("id", id);
        DtywNetTask task = new ShipaddressRemoveTask(information, params);
        executeTask(task);
    }

    /**
     * 保存收货地址
     */
    public void shipaddressSave(String token, String id, String name, String tel, String city, String location, String defaultadd) {
        DtywHttpInformation information = DtywHttpInformation.SHIPADDRESS_SAVE;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("id", id);
        params.put("name", name);
        params.put("tel", tel);
        params.put("city", city);
        params.put("location", location);
        params.put("defaultadd", defaultadd);
        DtywNetTask task = new ShipaddressSaveTask(information, params);
        executeTask(task);

    }

    /**
     * 新增收货地址
     */
    public void shipaddressAdd(String token, String name, String tel, String city, String location, String defaultadd) {
        DtywHttpInformation information = DtywHttpInformation.SHIPADDRESS_ADD;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);

        params.put("name", name);
        params.put("tel", tel);
        params.put("city", city);
        params.put("location", location);
        params.put("defaultadd", defaultadd);
        DtywNetTask task = new ShipaddressAddTask(information, params);
        executeTask(task);
    }

    /**
     * 添加订单
     *
     * @param token
     * @param cartid
     * @param //invoicetype
     * @param addressid
     * @param invoiceheader
     * @param invoiceitem
     * @param company
     * @param companyaddress
     * @param conmpanytel
     * @param bank
     * @param banknum
     * @param taxnum
     * @param invoicedemo
     */
    public void orderAdd(String token, String cartid, String invoice, String addressid, String invoiceheader,
                         String invoiceitem, String company, String companyaddress, String conmpanytel, String bank,
                         String banknum, String taxnum, String invoicedemo) {
        DtywHttpInformation information = DtywHttpInformation.ORDER_ADD;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("cartid", cartid);
        params.put("invoice", invoice);
        params.put("addressid", addressid);
        params.put("invoiceheader", invoiceheader);
        params.put("invoiceitem", invoiceitem);
        params.put("company", company);
        params.put("companyaddress", companyaddress);
        params.put("conmpanytel", conmpanytel);
        params.put("bank", bank);
        params.put("banknum", banknum);
        params.put("taxnum", taxnum);
        params.put("invoicedemo", invoicedemo);
        DtywNetTask task = new OrderAddTask(information, params);
        executeTask(task);
    }

    /**
     * 获取余额
     */
    public void getFeeaccount(String token) {
        DtywHttpInformation information = DtywHttpInformation.GET_FEEACCOUNT;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        DtywNetTask task = new GetFeeaccountTask(information, params);
        executeTask(task);
    }

    /**
     * 余额支付
     */
    public void feeaccountRemove(String token,String money, String keyid, String paypassword) {
        DtywHttpInformation information = DtywHttpInformation.FEEACCOUNT_REMOVE;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("keyid", keyid);
        params.put("money", money);
        params.put("paypassword", paypassword);
        DtywNetTask task = new FeeaccountRemoveTask(information, params);
        executeTask(task);
    }

    /**
     * @param token
     * @param paytype
     * @param keytype
     * @param keyid
     * @param total_fee
     * @param //extra_param
     * @方法名称: alipayGet
     * @功能描述: TODO支付宝支付
     * @返回值: void
     */
    public void alipayGet(String token, String paytype, String keytype,
                          String keyid, String total_fee) {
        DtywHttpInformation information = DtywHttpInformation.ALIPAY;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("paytype", paytype);
        params.put("keytype", keytype);
        params.put("keyid", keyid);
        params.put("total_fee", total_fee);

        DtywNetTask task = new AlipayTradeTask(information, params);
        executeTask(task);
    }

    /**
     * @param token
     * @param paytype
     * @param keytype
     * @param keyid
     * @param total_fee
     * @方法名称: unionpay
     * @功能描述: TODO银联支付
     * @返回值: void
     */
    public void unionpay(String token, String paytype, String keytype,
                         String keyid, String total_fee) {
        DtywHttpInformation information = DtywHttpInformation.UNIONPAY;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("paytype", paytype);
        params.put("keytype", keytype);
        params.put("keyid", keyid);
        params.put("total_fee", total_fee);
        DtywNetTask task = new UnionTradeTask(information, params);
        executeTask(task);
    }

    /**
     * @param token
     * @param paytype
     * @param keytype
     * @param keyid
     * @param total_fee
     * @方法名称: wxpayment
     * @功能描述: TODO微信支付
     * @返回值: void
     */
    public void wxpayment(String token, String paytype, String keytype,
                          String keyid, String total_fee) {
        DtywHttpInformation information = DtywHttpInformation.WXPAYMENT;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("paytype", paytype);
        params.put("keytype", keytype);
        params.put("keyid", keyid);
        params.put("total_fee", total_fee);
        DtywNetTask task = new WxPaymentTask(information, params);
        executeTask(task);
    }

    /**
     * 退出登录
     */
    public void clientLoginout(String token) {
        DtywHttpInformation information = DtywHttpInformation.CLIENT_LOGINOUT;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        DtywNetTask task = new ClientLoginoutTask(information, params);
        executeTask(task);
    }

    /**
     * 推送设置
     */
    public void getnotice(String token, String keytype) {
        DtywHttpInformation information = DtywHttpInformation.GETNOTICE;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("keytype", keytype);
        DtywNetTask task = new ClientLoginoutTask(information, params);
        executeTask(task);
    }

    /**
     * 意见反馈
     *
     * @param token
     * @param content
     */
    public void adviceAdd(String token, String content) {
        DtywHttpInformation information = DtywHttpInformation.ADVICE_ADD;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("content", content);
        DtywNetTask task = new ClientLoginoutTask(information, params);
        executeTask(task);
    }

    /**
     * 推送状态
     *
     * @param token
     */
    public void notice(String token) {
        DtywHttpInformation information = DtywHttpInformation.NOTICE;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        DtywNetTask task = new NoticeTask(information, params);
        executeTask(task);
    }

    /**
     * 修改密码
     *
     * @param token
     * @param keytype
     * @param old_password
     * @param new_password
     */
    public void passwordSave(String token, String keytype, String old_password, String new_password) {
        DtywHttpInformation information = DtywHttpInformation.PASSWORD_SAVE;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("keytype", keytype);
        params.put("old_password", old_password);
        params.put("new_password", new_password);

        DtywNetTask task = new ClientLoginoutTask(information, params);
        executeTask(task);
    }

    /**
     * 用户保存
     *
     * @param token
     * @param nickname
     * @param location
     */
    public void clientSave(String token, String nickname, String location) {
        DtywHttpInformation information = DtywHttpInformation.CLIENT_SAVE;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("nickname", nickname);
        params.put("location", location);
        DtywNetTask task = new ClientLoginoutTask(information, params);
        executeTask(task);
    }

    /**
     * 我的收藏
     *
     * @param token
     */
    public void collectList(String token) {
        DtywHttpInformation information = DtywHttpInformation.COLLECT_LIST;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        DtywNetTask task = new CollectListTask(information, params);
        executeTask(task);
    }

    /**
     * 交易记录
     *
     * @param token
     * @param page
     */
    public void payList(String token, String page) {
        DtywHttpInformation information = DtywHttpInformation.PAY_LIST;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("page", page);
        DtywNetTask task = new PayListTask(information, params);
        executeTask(task);
    }

    /**
     * 直接购买
     * @param token
     * @param goodsid
     * @param buycount
     * @param installid
     * @param properyid
     * @param addressid
     * @param invoice
     * @param invoiceheader
     * @param invoiceitem
     * @param company
     * @param companyaddress
     * @param conmpanytel
     * @param bank
     * @param banknum
     * @param taxnum
     * @param invoicedemo
     */
    public void goodBuy(String token, String goodsid, String buycount, String installid, String properyid, String addressid
            , String invoice, String invoiceheader, String invoiceitem, String company, String companyaddress, String conmpanytel,
                        String bank, String banknum, String taxnum, String invoicedemo) {
        DtywHttpInformation information = DtywHttpInformation.GOODS_BUY;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("goodsid", goodsid);
        params.put("buycount", buycount);
        params.put("installid", installid);
        params.put("properyid", properyid);
        params.put("addressid", addressid);
        params.put("invoice", invoice);
        params.put("invoiceheader", invoiceheader);
        params.put("invoiceitem", invoiceitem);
        params.put("company", company);
        params.put("companyaddress", companyaddress);
        params.put("conmpanytel", conmpanytel);
        params.put("bank", bank);
        params.put("banknum", banknum);
        params.put("taxnum", taxnum);
        params.put("invoicedemo", invoicedemo);
        DtywNetTask task = new GoodsBuyTask(information,params);
        executeTask(task);

    }

    /**
     * 订单列表
     * @param token
     * @param keytype
     * @param page
     */
    public void orderList(String token,String keytype,String page)
    {
        DtywHttpInformation information = DtywHttpInformation.ORDER_LIST;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("keytype", keytype);
        params.put("page", page);
        DtywNetTask task = new OrderListTask(information,params);
        executeTask(task);
    }

    /**
     * 订单操作
     * @param token
     * @param keytype
     * @param orderid
     */
    public void orderSaveoperate(String token,String keytype,String orderid)
    {
        DtywHttpInformation information = DtywHttpInformation.ORDER_SAVEOPERATE;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("keytype", keytype);
        params.put("orderid", orderid);
        DtywNetTask task = new ClientLoginoutTask(information, params);
        executeTask(task);
    }

    /**
     * 确认收货
     * @param token
     * @param orderid
     */
    public void orderConfirm(String token,String orderid)
    {
        DtywHttpInformation information = DtywHttpInformation.ORDER_CONFIRM;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("orderid", orderid);
        DtywNetTask task = new ClientLoginoutTask(information, params);
        executeTask(task);
    }

    /**
     * 添加评价
     * @param token
     * @param keytype
     * @param keyid
     * @param goodsid
     * @param orderid
     * @param content
     * @param replytype
     * @param parentid
     */
    public void replyAdd(String token,String keytype,String keyid,String goodsid,String orderid,String content,String replytype,
                         String parentid)
    {
        DtywHttpInformation information = DtywHttpInformation.REPLY_ADD;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("keytype", keytype);
        params.put("keyid", keyid);
        params.put("goodsid", goodsid);
        params.put("content", content);
        params.put("orderid", orderid);
        params.put("replytype", replytype);
        params.put("parentid", parentid);
        DtywNetTask task = new ReplyAddTask(information, params);
        executeTask(task);
    }

    /**
     * 订单详情
     * @param token
     * @param orderid
     */
    public void orderGet(String token,String orderid)
    {
        DtywHttpInformation information = DtywHttpInformation.ORDER_GET;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("orderid", orderid);
        DtywNetTask task = new OrderGetTask(information,params);
        executeTask(task);
    }

    /**
     * 退款列表
     * @param token
     * @param page
     */
    public void orderReturn(String token,String page)
    {
        DtywHttpInformation information = DtywHttpInformation.ORDER_RETURN;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("page", page);
        DtywNetTask task = new orderReturnTask(information,params);
        executeTask(task);
    }

    /**
     * 退款详情
     * @param token
     * @param orderid
     * @param cartid
     */
    public void refundDetails(String token,String orderid,String cartid,String goodsid)
    {

        DtywHttpInformation information = DtywHttpInformation.REFUND_DETAILS;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("orderid", orderid);
        params.put("cartid", cartid);
        params.put("goodsid", goodsid);
        DtywNetTask task = new RefundDetailsTask(information,params);
        executeTask(task);
    }

    /**
     * 保存快递信息
     * @param token
     * @param orderid
     * @param cartid
     * @param deliveryname
     * @param deliverynum
     */
    public void deliverySave(String token,String orderid,String cartid,String deliveryname,String deliverynum)
    {
        DtywHttpInformation information = DtywHttpInformation.DELIVERY_SAVE;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("orderid", orderid);
        params.put("cartid", cartid);
        params.put("deliveryname", deliveryname);
        params.put("deliverynum", deliverynum);
        DtywNetTask task = new CodeGetTask(information,params);
        executeTask(task);
    }

    /**
     * 退款申请
     * @param token
     * @param orderid
     * @param cartid
     * @param returndemo
     */
    public void accountReturn(String token,String orderid,String cartid,String returndemo)
    {
        DtywHttpInformation information = DtywHttpInformation.ACCOUNT_RETURN;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("orderid", orderid);
        params.put("cartid", cartid);
        params.put("returndemo", returndemo);
        DtywNetTask task = new CodeGetTask(information,params);
        executeTask(task);
    }

    /**
     * 银行卡信息保存
     * @param token
     * @param bankuser
     * @param bankname
     * @param bankcard
     */
    public void bankSave(String token,String bankuser,String bankname,String bankcard,String bank)
    {
        DtywHttpInformation information = DtywHttpInformation.BANK_SAVE;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("bankuser", bankuser);
        params.put("bankname", bankname);
        params.put("bankcard", bankcard);
        params.put("bank", bank);
        DtywNetTask task = new CodeGetTask(information,params);
        executeTask(task);
    }

    /**
     * 支付宝信息保存
     */
    public void alipaySave(String token,String alipayname)
    {
        DtywHttpInformation information = DtywHttpInformation.ALIPAY_SAVE;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("alipayname", alipayname);
        DtywNetTask task = new CodeGetTask(information,params);
        executeTask(task);
    }

    /**
     * 银行列表
     * @param page
     */
    public void bankList(String page)
    {
        DtywHttpInformation information = DtywHttpInformation.BANK_LIST;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("page", page);
        DtywNetTask task = new BankListTask(information,params);
        executeTask(task);
    }

    /**
     * 申请提现
     * @param token
     * @param keytype
     * @param applyfee
     * @param paypassword
     */
    public void cashAdd(String token,String keytype,String applyfee,String paypassword)
    {
        DtywHttpInformation information = DtywHttpInformation.CASH_ADD;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("keytype", keytype);
        params.put("applyfee", applyfee);
        params.put("paypassword", paypassword);
        DtywNetTask task = new CodeGetTask(information,params);
        executeTask(task);
    }

    /**
     * 获取个人数据
     * @param token
     * @param id
     */
    public void clientGet(String token,String id)
    {
        DtywHttpInformation information = DtywHttpInformation.CLIENT_GET;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("id", id);
        DtywNetTask task = new ClientLoginTask(information,params);
        executeTask(task);
    }
    /**
     *
     * @方法名称: deviceSave
     * @功能描述: TODO硬件保存
     * @param token
     * @param deviceid
     * @param devicetype
     * @param channelid
     * @返回值: void
     */
    public void deviceSave(String token, String deviceid, String devicetype,
                           String channelid) {
        DtywHttpInformation information = DtywHttpInformation.DEVICE_SAVE;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("deviceid", deviceid);
        params.put("devicetype", devicetype);
        params.put("channelid", channelid);

        DtywNetTask task = new CodeGetTask(information, params);
        executeTask(task);
    }

    /**
     * 获取订单中各个状态的数量
     * @param token
     */
    public void orderNum(String token)
    {
        DtywHttpInformation information = DtywHttpInformation.ORDER_NUM;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);

        DtywNetTask task = new OrderNumTask(information, params);
        executeTask(task);
    }
    /**
     * 需求发布
     * @param token
     */
    public void requirementAdd(String token,String name,String brand,String property,String memo,String phone,String address)
    {
        DtywHttpInformation information = DtywHttpInformation.REQUIREMENT_ADD;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("name", name);
        params.put("brand", brand);
        params.put("property", property);
        params.put("memo", memo);
        params.put("phone", phone);
        params.put("address", address);
        DtywNetTask task = new OrderAddTask(information, params);
        executeTask(task);
    }
    /**
     * 需求列表requirement_list
     * @param
     *
     */
    public void requirementList(String keytype,String page)
    {
        DtywHttpInformation information = DtywHttpInformation.REQUIRMENT_LIST;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("keytype", keytype);
        params.put("page", page);
        DtywNetTask task = new RequirementListTask(information, params);
        executeTask(task);
    }
    /**
     * 需求详情requirement_list
     * @param
     *
     */
    public void requirementGet(String id)
    {
        DtywHttpInformation information = DtywHttpInformation.REQUIREMENT_GET;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        DtywNetTask task = new RequirementGetTask(information, params);
        executeTask(task);
    }
    /**
     * 需求详情requirement_list
     * @param
     *
     */
    public void thirdUrl()
    {
        DtywHttpInformation information = DtywHttpInformation.THIRD_URL;
        HashMap<String, String> params = new HashMap<String, String>();
        DtywNetTask task = new ThirdUrlTask(information, params);
        executeTask(task);
    }
}
