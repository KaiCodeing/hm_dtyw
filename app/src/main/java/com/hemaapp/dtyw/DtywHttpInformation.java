package com.hemaapp.dtyw;

import com.hemaapp.HemaConfig;
import com.hemaapp.dtyw.config.DtywConfig;
import com.hemaapp.dtyw.model.SysInitInfo;
import com.hemaapp.hm_FrameWork.HemaHttpInfomation;

/**
 * Created by lenovo on 2016/9/6.
 */
public  enum DtywHttpInformation implements HemaHttpInfomation{
    /**
     * 登录
     */
    CLIENT_LOGIN(HemaConfig.ID_LOGIN, "client_login", "登录", false),
    // 注意登录接口id必须为HemaConfig.ID_LOGIN

    /**
     * 后台服务接口根路径
     */
    SYS_ROOT(0, DtywConfig.SYS_ROOT, "后台服务接口根路径", true),
    /**
     * 初始化
     */
    INIT(1, "index.php/webservice/index/init", "初始化", false),
    /**
     * code_get发送验证码
     */
    CODE_GET(2, "code_get", "发送验证码", false),
    /**
     * code_verify验证验证码
     */
    CODE_VERIFY(3, "code_verify", "验证验证码", false),
    /**
     * client_verify验证用户是否注册
     */
    CLIENT_VERIFY(4, "client_verify", "验证用户是否注册", false),
    /**
     * 密码重设password_reset
     */
    PASSWORD_RESET(5, "password_reset", "密码重设", false),
    /**
     * 地区列表address_list
     */
    ADDRESS_LIST(6,"address_list","地区列表",false),
    /**
     * addlistall_list全部地区
     */
    ADDLISTALL_LIST(7,"addlistall_list","全部地区",false),
    /**
     * file_upload上传文件
     */
    FILE_UPLOAD(8,"file_upload","上传文件",false),
    /**
     * 用户注册client_add
     */
    CLIENT_ADD(9,"client_add","用户注册",false),
    /**
     * ad_list广告页
     */
    AD_LIST(10,"ad_list","广告页",false),
    /**
     * bulletin_list公告
     */
    BULLETIN_LIST(11,"bulletin_list","公告",false),
    /**
     * brands_list 品牌列表
     */
    BRANDS_LIST(12,"brands_list","品牌列表",false),
    /**
     * goods_list商品列表
     */
    GOODS_LIST(13,"goods_list","商品列表",false),
    /**
     * notice_list消息列表
     */
    NOTICE_LIST(14,"notice_list","消息列表",false),
    /**
     * notice_saveoperate消息操作
     */
    NOTICE_SAVEOPERATE(15,"notice_saveoperate","消息操作",false),
    /**
     * notice_unread未读通知数
     */
    NOREAD_UNREAD(16,"notice_unread","未读通知数",false),
    /**
     * goods_gets商品详情
     */
    GOODS_GET(17,"goods_get","商品详情",false),
    /**
     * installservice_list 安装服务列表
     */
    INSTALLSERVICE_LIST(18,"installservice_list","安装服务列表",false),
    /**
     * reply_list评论
     */
    REPLY_LIST(19,"reply_list","评论列表",false),
    /**
     * goods_operate收藏操作
     */
    GOODS_OPERATE(20,"goods_operate","收藏操作",false),
    /**
     * cart_add加入购物车
     */
    CART_ADD(21,"cart_add","加入购物车",false),
    /**
     * get_cartgoodsnum 购物车中商品数量
     */
    GET_CARTGOODSNUM(22,"get_cartgoodsnum","购物车商品数量",false),
    /**
     * brands_tw全部品牌
     */
    BRANDS_TW(23,"brands_tw","全部品牌",false),
    /**
     * alltype_list类型列表
     */
    ALLTYPE_LIST(24,"alltype_list","alltype_list",false),
    /**
     * pricerange_list 价格区间
     */
    PRICERANGE_LIST(25,"pricerange_list","价格区间",false),
    /**
     * add_list一二级地址列表
     */
    ADD_LIST(26,"add_list","一二级地址列表",false),
    /**
     * cart_list购物车列表
     */
    CART_LIST(27,"cart_list","购物车列表",false),
    /**
     * cart_saveoperate购物车操作
     */
    CART_SAVEOPERATE(28,"cart_saveoperate","购物车操作",false),
    /**
     * get_defaultadd默认收货地址
     */
    GET_DEFAULTADD(29,"get_defaultadd","默认收货地址",false),
    /**
     *   shipaddress_list收货地址列表
     */
    SHIPADDRESS_LIST(30,"shipaddress_list","收货地址列表",false),
    /**
     * setdefaultadd设置默认收货地址
     */
    SETDEFAULTADD(31,"setdefaultadd","设置默认收货地址",false),
    /**
     * shipaddress_remove删除收货地址
     */
    SHIPADDRESS_REMOVE(32,"shipaddress_remove","删除收货地址",false),
    /**
     * shipaddress_save保存收货地址
     */
    SHIPADDRESS_SAVE(33,"shipaddress_save","保存收货地址",false),
    /**
     * shipaddress_add新增收货地址
     */
    SHIPADDRESS_ADD(34,"shipaddress_add","新增收货地址",false),
    /**
     *     order_add 添加订单
     */
    ORDER_ADD(35,"order_add","添加订单",false),
    /**
     * get_feeaccount 获取余额钱数
     */
    GET_FEEACCOUNT(36,"get_feeaccount","获取余额数量",false),
    /**
     * feeaccount_remove余额购买
     */
    FEEACCOUNT_REMOVE(37,"feeaccount_remove","余额购买",false),
    /**
     * 支付宝alipay
     */
    ALIPAY(38,"OnlinePay/Alipay/alipaysign_get.php","支付宝获取串口号",false),
    /**
     * 银联支付unionpay
     */
    UNIONPAY(39,"OnlinePay/Unionpay/unionpay_get.php","银联获取串口号",false),
    /**
     * 微信支付wxpayment
     */
    WXPAYMENT(40,"OnlinePay/Weixinpay/weixinpay_get.php","微信获取串口号",false),
    /**
     * client_loginout退出登录
     */
    CLIENT_LOGINOUT(41,"client_loginout","退出登录",false),
    /**
     * getnotice 推送设置
     */
    GETNOTICE(42,"getnotice","推送设置",false),
    /**
     * advice_add 意见反馈
     */
    ADVICE_ADD(43,"advice_add","意见反馈",false),
    /**
     * notice是否接受推送
     */
    NOTICE(44,"notice","是否接收推送",false),
    /**
     * password_save修改密码
     */
    PASSWORD_SAVE(45,"password_save","修改密码",false),
    /**
     * client_save 用户保存
     */
    CLIENT_SAVE(46,"client_save","用户保存",false),
    /**
     * collect_list我的收藏
     */
    COLLECT_LIST(47,"collect_list","我的收藏",false),
    /**
     * 交易记录pay_list
     */
    PAY_LIST(48,"pay_list","交易记录",false),
    /**
     * goods_buy 购买
     */
    GOODS_BUY(49,"goods_buy","购买",false),
    /**
     * order_list订单列表
     */
    ORDER_LIST(50,"order_list","订单列表",false),
    /**
     * order_saveoperate 订单操作
     */
    ORDER_SAVEOPERATE(51,"order_saveoperate","订单操作",false),
    /**
     * order_confirm确认收货
     */
    ORDER_CONFIRM(52,"order_confirm","确认收货",false),
    /**
     * reply_add商品评价
     */
    REPLY_ADD(53,"reply_add","商品评价",false),
    /**
     * 订单详情order_get
     */
    ORDER_GET(54,"order_get","订单详情",false),
    /**
     * order_return  退款订单
     */
    ORDER_RETURN(55,"order_return","退款订单",false),
    /**
     * refund_details 退款详情
     */
    REFUND_DETAILS(56,"refund_details","退款详情",false),
    /**
     * delivery_save保存快递信息
     */
    DELIVERY_SAVE(57,"delivery_save","保存快递信息",false),
    /**
     * account_return 申请退款
     */
    ACCOUNT_RETURN(58,"account_return","申请退款",false),
    /**bank_save
     * 保存银行卡信息
     */
    BANK_SAVE(59,"bank_save","银行卡信息保存",false),
    /**
     * alipay_save 支付宝信息保存
     */
    ALIPAY_SAVE(60,"alipay_save","支付宝信息保存",false),
    /**
     * 银行列表bank_list
     */
    BANK_LIST(61,"bank_list","银行卡列表",false),
    /**
     * cash_add申请提现
     */
    CASH_ADD(62,"cash_add","申请提现",false),
    /**
     * 请求个人数据client_get
     */
    CLIENT_GET(63,"client_get","请求个人数据",false),
    /**
     * device_save硬件保存
     */
    DEVICE_SAVE(64,"device_save","硬件保存",false),
    /**
     * order_num 订单数量
     */
    ORDER_NUM(65,"order_num","订单数量",false),
    /**
     * requirement_add 发布需求
     */
    REQUIREMENT_ADD(66,"requirement_add","发布需求",false),
    /**
     * requirement_list
     */
    REQUIRMENT_LIST(67,"requirement_list","需求列表",false),
    /**
     * requirement_get 需求详情
     */
    REQUIREMENT_GET(68,"requirement_get","需求详情",false),
    /**
     * third_url获取第三方网址
     */
    THIRD_URL(69,"third_url","获取第三方网址",false);
    private int id;// 对应NetTask的id
    private String urlPath;// 请求地址
    private String description;// 请求描述
    private boolean isRootPath;// 是否是根路径

    private DtywHttpInformation(int id, String urlPath, String description,
                                boolean isRootPath) {
        this.id = id;
        this.urlPath = urlPath;
        this.description = description;
        this.isRootPath = isRootPath;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getUrlPath() {
        if (isRootPath)
            return urlPath;

        String path = SYS_ROOT.urlPath + urlPath;

        if (this.equals(INIT))
            return path;

        DtywApplication application = DtywApplication.getInstance();
        SysInitInfo info = application.getSysInitInfo();
        path = info.getSys_web_service() + urlPath;
         if (this.equals(ALIPAY))
         path = info.getSys_plugins() + urlPath;
         if (this.equals(UNIONPAY))
         path = info.getSys_plugins() + urlPath;
        if (this.equals(WXPAYMENT))
            path = info.getSys_plugins() + urlPath;
        return path;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public boolean isRootPath() {
        return isRootPath;
    }

}
