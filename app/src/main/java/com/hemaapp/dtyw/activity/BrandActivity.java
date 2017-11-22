package com.hemaapp.dtyw.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hemaapp.dtyw.DtywActivity;
import com.hemaapp.dtyw.DtywApplication;
import com.hemaapp.dtyw.DtywHttpInformation;
import com.hemaapp.dtyw.adapter.Brand1ClassAdpater;
import com.hemaapp.dtyw.adapter.Brand2TypeAdapter;
import com.hemaapp.dtyw.adapter.Brand3AddressAdapter;
import com.hemaapp.dtyw.adapter.Brand4PriceAdapter;
import com.hemaapp.dtyw.adapter.BrandAdapter;
import com.hemaapp.dtyw.model.AddressOne;
import com.hemaapp.dtyw.model.BrandsTw;
import com.hemaapp.dtyw.model.Goods;
import com.hemaapp.dtyw.model.Pricerange;
import com.hemaapp.dtyw.model.Type;
import com.hemaapp.dtyw.myapplication.R;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.result.HemaPageArrayResult;
import com.hemaapp.hm_FrameWork.view.RefreshLoadmoreLayout;

import java.util.ArrayList;

import xtom.frame.util.XtomToastUtil;
import xtom.frame.view.XtomListView;
import xtom.frame.view.XtomRefreshLoadmoreLayout;

/**
 * Created by lenovo on 2016/9/18.
 * 品牌分类  所有产品，二手配件
 * pop中的adapter ShaiXuanMoneyAdapter ShaiXuanExpandAdapter
 */
public class BrandActivity extends DtywActivity {
    private ImageButton back_button;
    private TextView title_text;
    private ImageButton next_button;
    private RefreshLoadmoreLayout refreshLoadmoreLayout;
    private XtomListView listview;
    private ProgressBar progressbar;
    private TextView text1;//综合
    private TextView text2;//销量
    private TextView text3;//价格
    private TextView text4;//筛选
    private String brandId;
    private String brandName;
    private Integer page = 0;
    private BrandAdapter adapter;
    private ArrayList<Goods> goodses = new ArrayList<Goods>();
    private String type = "1";//1 综合2销量3价格增4价格减 5 筛选
    private ViewHolder holder;
    private ArrayList<BrandsTw> brandsTws = new ArrayList<BrandsTw>();
    private Brand1ClassAdpater adpater;
    private ArrayList<Type> types = new ArrayList<Type>();
    private View view;
    private Brand2TypeAdapter typeadapter;
    private String title;

    private ArrayList<Pricerange> priceranges = new ArrayList<Pricerange>();
    private ArrayList<AddressOne> addressOnes = new ArrayList<AddressOne>();
    private Brand4PriceAdapter priceAdapter;
    private Brand3AddressAdapter addressAdapter;
    private String typekey = ""; //3品牌分类商品 4:二手配件 5:所有产品
    private String pingPid = "";//筛选中品牌的id
    private String typeid = "";//筛选中类型组合id
    private String place = "";//筛选中产地组合名称
    private String priceid = "";//筛选中价格的id
    PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_brand);
        super.onCreate(savedInstanceState);
        inIt();

    }

    private void inIt() {
        //  page = 0;
        if (!isNull(brandId)) {
            getNetWorker().goodsList(typekey, brandId, "", type, "", "", "", String.valueOf(page));
        } else {
            getNetWorker().goodsList(typekey, "", typeid, type, "", "", "", String.valueOf(page));

        }
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case GOODS_LIST:
                showProgressDialog("获取商品信息");
                break;
            case BRANDS_TW:
                showProgressDialog("获取品牌信息");
                break;
            case ALLTYPE_LIST:
                showProgressDialog("获取产品信息");
                break;
            case ADD_LIST:
                showProgressDialog("获取地区信息");
                break;
            case PRICERANGE_LIST:
                showProgressDialog("获取价格信息");
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case GOODS_LIST:
                cancelProgressDialog();
                progressbar.setVisibility(View.GONE);
                refreshLoadmoreLayout.setVisibility(View.VISIBLE);
                break;
            case BRANDS_TW:
            case ALLTYPE_LIST:
            case ADD_LIST:
            case PRICERANGE_LIST:
                cancelProgressDialog();
                holder.progressbar.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case GOODS_LIST:
                HemaPageArrayResult<Goods> result3 = (HemaPageArrayResult<Goods>) hemaBaseResult;
                ArrayList<Goods> goodses = result3.getObjects();
                String page2 = hemaNetTask.getParams().get("page");
                if ("0".equals(page2)) {// 刷新
                    refreshLoadmoreLayout.refreshSuccess();
                    this.goodses.clear();
                    this.goodses.addAll(goodses);

                    DtywApplication application = DtywApplication.getInstance();
                    int sysPagesize = application.getSysInitInfo()
                            .getSys_pagesize();
                    if (goodses.size() < sysPagesize) {
                        refreshLoadmoreLayout.setLoadmoreable(false);
                        // leftRE = false;
                    } else {
                        refreshLoadmoreLayout.setLoadmoreable(true);
                        // leftRE = true;
                    }
                } else {// 更多
                    refreshLoadmoreLayout.loadmoreSuccess();
                    if (goodses.size() > 0)
                        this.goodses.addAll(goodses);
                    else {
                        refreshLoadmoreLayout.setLoadmoreable(false);
                        // leftRE = false;
                        XtomToastUtil.showShortToast(this, "已经到最后啦");
                    }
                }
                freshData();
                break;
            case BRANDS_TW:
                HemaArrayResult<BrandsTw> result = (HemaArrayResult<BrandsTw>) hemaBaseResult;
                brandsTws = result.getObjects();
                if (brandsTws != null) {
                    for (int i = 0; i < brandsTws.size(); i++) {
                        for (int j = 0; j < brandsTws.get(i).getBrands().size(); j++) {
                            if (brandId.equals(brandsTws.get(i).getBrands().get(j).getId())) {
                                brandsTws.get(i).getBrands().get(j).setCheck(true);
                            } else
                                brandsTws.get(i).getBrands().get(j).setCheck(false);
                        }
                    }
                }
                freshData1Class();
                break;
            case ALLTYPE_LIST:
                HemaArrayResult<Type> result2 = (HemaArrayResult<Type>) hemaBaseResult;
                types = result2.getObjects();
                if (types != null) {
                    for (int i = 0; i < types.size(); i++) {
                        for (int j = 0; j < types.get(i).getChildren().size(); j++) {
                            if (typeid.equals(types.get(i).getChildren().get(j).getId())) {
                                types.get(i).getChildren().get(j).setCheck(true);
                                types.get(i).setCheck(true);
                            } else
                                types.get(i).getChildren().get(j).setCheck(false);
                        }
                    }
                }
                freshDataType();
                break;
            case ADD_LIST:
                HemaArrayResult<AddressOne> result1 = (HemaArrayResult<AddressOne>) hemaBaseResult;
                addressOnes = result1.getObjects();
                freshDataAdd();

                break;
            case PRICERANGE_LIST:
                HemaArrayResult<Pricerange> result5 = (HemaArrayResult<Pricerange>) hemaBaseResult;
                priceranges = result5.getObjects();
                freshData4Price();
                break;
        }
    }

    /**
     * 地址列表adapter
     *
     * @param
     */
    private void freshDataAdd() {


        addressAdapter = new Brand3AddressAdapter(mContext, BrandActivity.this, addressOnes, title);
        //   addressAdapter.setAddressOnes(addressOnes);
        title = "1";
        addressAdapter.setTit(title);
        addressAdapter.setEmptyString("暂无数据");
        holder.listview.setAdapter(addressAdapter);
    }

    //改变地址状态二
    public void changeAddressOneName(int a, int b) {
        for (int i = 0; i < addressOnes.size(); i++) {
            for (int j = 0; j < addressOnes.get(i).getChildren().size(); j++) {
                if (a == i) {
                    addressOnes.get(i).setCheck(true);
                    if (j == b) {
                        addressOnes.get(i).getChildren().get(j).setCheck(true);
                    } else
                        addressOnes.get(i).getChildren().get(j).setCheck(false);
                } else {
                    addressOnes.get(i).setCheck(false);
                    addressOnes.get(i).getChildren().get(j).setCheck(false);
                }
            }
        }
        title = "1";
        addressAdapter.setTit(title);
        addressAdapter.setAddressOnes(addressOnes);
        addressAdapter.notifyDataSetChanged();
    }

    //改变地址主题状态
    public void changeAddressName(int a) {
        for (int i = 0; i < addressOnes.size(); i++) {
            if (a == i) {
                addressOnes.get(i).setCheck(true);
            } else
                addressOnes.get(i).setCheck(false);
        }
        title = "1";
        addressAdapter.setTit(title);
        addressAdapter.setAddressOnes(addressOnes);
        addressAdapter.notifyDataSetChanged();
    }


    //选择价格
    public void changePrice(int a) {
        for (int i = 0; i < priceranges.size(); i++) {
            if (i == a)
                priceranges.get(i).setCheck(true);
            else
                priceranges.get(i).setCheck(false);
        }
        priceAdapter.setPriceranges(priceranges);
        priceAdapter.notifyDataSetChanged();
    }

    /**
     * 选择价格的adapter
     */
    private void freshData4Price() {
        priceAdapter = new Brand4PriceAdapter(mContext, priceranges, BrandActivity.this);
        priceAdapter.setEmptyString("暂无数据");
        holder.listview.setAdapter(priceAdapter);

    }

    //重新选择品牌
    public void changePP(int a, int b) {
        for (int i = 0; i < brandsTws.size(); i++) {
            for (int j = 0; j < brandsTws.get(i).getBrands().size(); j++) {
                if (a == i && b == j) {
                    brandsTws.get(i).getBrands().get(j).setCheck(true);
                } else
                    brandsTws.get(i).getBrands().get(j).setCheck(false);
            }
        }
        //   freshData1Class();
    }

    private void freshData1Class() {
//        if (adpater == null) {
        adpater = new Brand1ClassAdpater(mContext, brandsTws, BrandActivity.this);


        adpater.setEmptyString("暂无数据");
        holder.listview.setAdapter(adpater);
//        } else {
//            adpater.setBrandses(brandsTws);
//            adpater.setEmptyString("暂无数据");
//            adpater.notifyDataSetChanged();
//
//        }
    }

    //改变类型主题状态
    public void changeTypeName(int a) {
        for (int i = 0; i < types.size(); i++) {
            if (a == i) {
                types.get(i).setCheck(true);
            } else
                types.get(i).setCheck(false);
        }
        title = "1";
        typeadapter.setTit(title);
        typeadapter.setTypes(types);
        typeadapter.notifyDataSetChanged();
    }

    //改变类型状态二
    public void changeTypeOneName(int a, int b) {
        for (int i = 0; i < types.size(); i++) {
            for (int j = 0; j < types.get(i).getChildren().size(); j++) {
                if (a == i) {
                    types.get(i).setCheck(true);
                    if (j == b) {
                        types.get(i).getChildren().get(j).setCheck(true);
                    } else
                        types.get(i).getChildren().get(j).setCheck(false);
                } else {
                    types.get(i).setCheck(false);
                    types.get(i).getChildren().get(j).setCheck(false);
                }
            }
        }
        title = "1";
        typeadapter.setTypes(types);
        typeadapter.notifyDataSetChanged();
    }

    private void freshDataType() {
//        if (typeadapter == null) {
        typeadapter = new Brand2TypeAdapter(mContext, types, this, title);
        title = "1";
        typeadapter.setEmptyString("暂无数据");
        typeadapter.setTit(title);
        holder.listview.setAdapter(typeadapter);
//        } else {
//
//            typeadapter.setEmptyString("暂无数据");
//            typeadapter.notifyDataSetChanged();
//
//        }
    }

    private void freshData() {
        if (adapter == null) {
            adapter = new BrandAdapter(mContext, goodses);

            adapter.setEmptyString("暂无数据");

            listview.setAdapter(adapter);
        } else {

            adapter.setEmptyString("暂无数据");
            adapter.notifyDataSetChanged();

        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case GOODS_LIST:
            case BRANDS_TW:
            case ADD_LIST:
                showTextDialog(hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case GOODS_LIST:
                String page1 = hemaNetTask.getParams().get("page");
                if ("0".equals(page1)) {
                    refreshLoadmoreLayout.refreshFailed();
                } else {
                    refreshLoadmoreLayout.loadmoreFailed();
                }
                showTextDialog("获取商品信息失败，请稍后重试");
                break;
            case BRANDS_TW:
                showTextDialog("获取品牌失败，请稍后重试");
                break;
            case ADD_LIST:
                showTextDialog("获取地区失败，请稍后重试");
                break;
        }
    }

    @Override
    protected void findView() {

        refreshLoadmoreLayout = (RefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        listview = (XtomListView) findViewById(R.id.listview);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        text1 = (TextView) findViewById(R.id.text1);
        text2 = (TextView) findViewById(R.id.text2);
        text3 = (TextView) findViewById(R.id.text3);
        text4 = (TextView) findViewById(R.id.text4);
        back_button = (ImageButton) findViewById(R.id.back_button);
        title_text = (TextView) findViewById(R.id.title_text);
        next_button = (ImageButton) findViewById(R.id.next_button);
    }

    @Override
    protected void getExras() {
        brandName = mIntent.getStringExtra("brandName");
        brandId = mIntent.getStringExtra("brandId");
        if (isNull(brandId))
            brandId = "";
        typekey = mIntent.getStringExtra("typekey");
        typeid = mIntent.getStringExtra("typeid");
        if (isNull(typeid))
            typeid = "";
    }

    @Override
    protected void setListener() {
        back_button.setImageResource(R.mipmap.back_img);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        next_button.setImageResource(R.mipmap.search_more_img);
//        if (!isNull(typeid))
//            next_button.setVisibility(View.INVISIBLE);
       // 搜索
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BrandActivity.this, SearchMoreActivity.class);
                intent.putExtra("keytype", typekey);
                if (isNull(brandId))
                    intent.putExtra("brandId", typeid);
                else
                    intent.putExtra("brandId", brandId);
                startActivity(intent);
            }
        });
        if (!isNull(brandName))
            title_text.setText(brandName + "商品");
        refreshLoadmoreLayout.setOnStartListener(new XtomRefreshLoadmoreLayout.OnStartListener() {
            @Override
            public void onStartRefresh(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                page = 0;
                frelshData();
            }

            @Override
            public void onStartLoadmore(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                page++;
                frelshData();
            }
        });
        //选择各种筛选
        //综合
        text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "1";
                text1.setTextColor(getResources().getColor(R.color.title_backgroid));
                text2.setTextColor(getResources().getColor(R.color.shaixuan));
                text3.setTextColor(getResources().getColor(R.color.shaixuan));
                text3.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.jiage_no_img), null);
                text4.setTextColor(getResources().getColor(R.color.shaixuan));
                text4.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.shaixuan_no_img), null);
                if (popupWindow != null)
                    popupWindow.dismiss();
                setData();
            }
        });
        //销量
        text2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "2";
                text2.setTextColor(getResources().getColor(R.color.title_backgroid));
                text1.setTextColor(getResources().getColor(R.color.shaixuan));
                text3.setTextColor(getResources().getColor(R.color.shaixuan));
                text3.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.jiage_no_img), null);
                text4.setTextColor(getResources().getColor(R.color.shaixuan));
                text4.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.shaixuan_no_img), null);
                if (popupWindow != null)
                    popupWindow.dismiss();
                setData();
            }
        });
        //价格
        text3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text3.setTextColor(getResources().getColor(R.color.title_backgroid));
                text1.setTextColor(getResources().getColor(R.color.shaixuan));
                text2.setTextColor(getResources().getColor(R.color.shaixuan));
                text4.setTextColor(getResources().getColor(R.color.shaixuan));
                text4.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.shaixuan_no_img), null);
                if (type.equals("4")) {
                    text3.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.jiage_max_img), null);
                    type = "3";
                } else {
                    text3.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.jiage_min_img), null);
                    type = "4";
                }
                if (popupWindow != null)
                    popupWindow.dismiss();
                setData();
            }
        });
        //筛选
        text4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text4.setTextColor(getResources().getColor(R.color.title_backgroid));
                text1.setTextColor(getResources().getColor(R.color.shaixuan));
                text2.setTextColor(getResources().getColor(R.color.shaixuan));
                text3.setTextColor(getResources().getColor(R.color.shaixuan));
                text4.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.shaixuan_yes_img), null);
                text3.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.jiage_no_img), null);
                if (popupWindow != null)
                    popupWindow.dismiss();
                showSelect();
            }
        });

    }

    private void setData() {
        page = 0;
        if (!type.equals("5")) {
//            if (!isNull(brandId)) {
//                getNetWorker().goodsList("3", brandId, "", type, "", "", "", String.valueOf(page));
//            }
            frelshData();
        }
    }

    //四个选项
    private class ViewHolder {
        RadioGroup radiogroup_select;
        LinearLayout layout_yes;
        TextView chongzhi;
        TextView yes_text;
        XtomListView listview;
        ProgressBar progressbar;
        RadioButton check1;
        RadioButton check2;
        RadioButton check3;
        RadioButton check4;
        LinearLayout layout_show;
    }

    private void findHolder(ViewHolder holder, View v) {
        holder.radiogroup_select = (RadioGroup) v.findViewById(R.id.radiogroup_select);
        holder.layout_yes = (LinearLayout) v.findViewById(R.id.layout_yes);
        holder.chongzhi = (TextView) v.findViewById(R.id.chongzhi);
        holder.yes_text = (TextView) v.findViewById(R.id.yes_text);
        holder.listview = (XtomListView) v.findViewById(R.id.listview);
        holder.progressbar = (ProgressBar) v.findViewById(R.id.progressbar);
    }

    private void showSelect() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.pop_shaixuan_view, null);

        holder = new ViewHolder();
        holder.radiogroup_select = (RadioGroup) view.findViewById(R.id.radiogroup_select);
        holder.layout_yes = (LinearLayout) view.findViewById(R.id.layout_yes);
        holder.chongzhi = (TextView) view.findViewById(R.id.chongzhi);
        holder.yes_text = (TextView) view.findViewById(R.id.yes_text);
        holder.listview = (XtomListView) view.findViewById(R.id.listview);
        holder.progressbar = (ProgressBar) view.findViewById(R.id.progressbar);
        holder.check1 = (RadioButton) view.findViewById(R.id.check1);
        holder.check2 = (RadioButton) view.findViewById(R.id.check2);
        holder.check3 = (RadioButton) view.findViewById(R.id.check3);
        holder.check4 = (RadioButton) view.findViewById(R.id.check4);
        holder.layout_show = (LinearLayout) view.findViewById(R.id.layout_show);
        holder.layout_yes.setVisibility(View.GONE);
        holder.layout_show.setVisibility(View.GONE);
        holder.radiogroup_select.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.check1://品牌筛选
                        if (brandsTws == null || brandsTws.size() == 0)
                            getNetWorker().brandsTw();
                        else {
                            adpater = new Brand1ClassAdpater(mContext, brandsTws, BrandActivity.this);
                            adpater.setEmptyString("暂无数据");
                            holder.listview.setAdapter(adpater);
                        }
                        holder.layout_yes.setVisibility(View.VISIBLE);
                        holder.layout_show.setVisibility(View.VISIBLE);
                        break;
                    case R.id.check2://产品类型
                        if (types == null || types.size() == 0)
                            getNetWorker().typeList();
                        else {
                            typeadapter = new Brand2TypeAdapter(mContext, types, BrandActivity.this, title);
                            title = "2";
                            typeadapter.setTit(title);
                            typeadapter.setEmptyString("暂无数据");
                            holder.listview.setAdapter(typeadapter);
                        }
                        holder.layout_yes.setVisibility(View.VISIBLE);
                        holder.layout_show.setVisibility(View.VISIBLE);
                        break;
                    case R.id.check3://产地筛选
                        if (addressOnes == null || addressOnes.size() == 0)
                            getNetWorker().addList();
                        else {
                            title = "2";
                            addressAdapter.setTit(title);
                            addressAdapter.setAddressOnes(addressOnes);
                            addressAdapter.setEmptyString("暂无数据");
                            holder.listview.setAdapter(addressAdapter);
                        }
                        holder.layout_yes.setVisibility(View.VISIBLE);
                        holder.layout_show.setVisibility(View.VISIBLE);
                        break;
                    case R.id.check4://价位筛选
                        if (priceranges == null || priceranges.size() == 0)
                            getNetWorker().pricerangeList();
                        else {
                            priceAdapter.setPriceranges(priceranges);
                            priceAdapter.setEmptyString("暂无数据");
                            holder.listview.setAdapter(priceAdapter);
                        }
                        holder.layout_yes.setVisibility(View.VISIBLE);
                        holder.layout_show.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
        //重置选项
        holder.chongzhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //重置各个选项
                if (holder.check1.isChecked()) {
                    for (int i = 0; i < brandsTws.size(); i++) {
                        for (int j = 0; j < brandsTws.get(i).getBrands().size(); j++)
                            brandsTws.get(i).getBrands().get(j).setCheck(false);
                    }
                    adpater = new Brand1ClassAdpater(mContext, brandsTws, BrandActivity.this);
                    adpater.setEmptyString("暂无数据");
                    holder.listview.setAdapter(adpater);
                } else if (holder.check2.isChecked()) {
                    for (int i = 0; i < types.size(); i++) {
                        types.get(i).setCheck(false);
                        for (int j = 0; j < types.get(i).getChildren().size(); j++) {
                            types.get(i).getChildren().get(j).setCheck(false);
                        }
                    }
                    typeadapter = new Brand2TypeAdapter(mContext, types, BrandActivity.this, title);
                    title = "2";
                    typeadapter.setTit(title);
                    typeadapter.setEmptyString("暂无数据");
                    holder.listview.setAdapter(typeadapter);
                } else if (holder.check3.isChecked()) {
                    for (int i = 0; i < addressOnes.size(); i++) {
                        addressOnes.get(i).setCheck(false);
                        for (int j = 0; j < addressOnes.get(i).getChildren().size(); j++) {
                            addressOnes.get(i).getChildren().get(j).setCheck(false);
                        }
                    }
                    title = "2";
                    addressAdapter.setTit(title);
                    addressAdapter.setAddressOnes(addressOnes);
                    addressAdapter.setEmptyString("暂无数据");
                    holder.listview.setAdapter(addressAdapter);
                } else {
                    for (int i = 0; i < priceranges.size(); i++) {
                        priceranges.get(i).setCheck(false);
                    }
                    priceAdapter.setPriceranges(priceranges);
                    priceAdapter.setEmptyString("暂无数据");
                    holder.listview.setAdapter(priceAdapter);
                }
            }
        });
        //确定
        holder.yes_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String pingPName = "";
                String typeName = "";
                place = "";
                priceid = "";
                String priceName = "";
                page = 0;
                if (brandsTws == null || brandsTws.size() == 0) {
                } else {
                    pingPid = "";
                    //获取品牌id
                    for (int i = 0; i < brandsTws.size(); i++) {
                        for (int j = 0; j < brandsTws.get(i).getBrands().size(); j++)
                            if (brandsTws.get(i).getBrands().get(j).isCheck()) {
                                pingPid = brandsTws.get(i).getBrands().get(j).getId();
                                pingPName = brandsTws.get(i).getBrands().get(j).getName();
                            }
                    }
                }
                //获取类型id
                //  String tid=null;
                if (types == null || types.size() == 0) {

                } else {
                    typeid = "";
                    for (int i = 0; i < types.size(); i++) {
                        for (int j = 0; j < types.get(i).getChildren().size(); j++) {
                            if (types.get(i).getChildren().get(j).isCheck()) {
                                // typeid = types.get(i).getId() + "," + types.get(i).getChildren().get(j).getId();
                                typeid = types.get(i).getChildren().get(j).getId();
                                //  typeName = types.get(i).getId() + "," + types.get(i).getChildren().get(j).getName();
                                typeName = types.get(i).getChildren().get(j).getName();
                            }
                        }
                    }
                }

                //获取地点名称组合
                for (int i = 0; i < addressOnes.size(); i++) {
                    for (int j = 0; j < addressOnes.get(i).getChildren().size(); j++) {
                        if (addressOnes.get(i).getChildren().get(j).isCheck())
                            place = addressOnes.get(i).getName() + "," + addressOnes.get(i).getChildren().get(j).getName();
                    }
                }
                //获取价格区间
                for (int i = 0; i < priceranges.size(); i++) {
                    if (priceranges.get(i).isCheck()) {
                        priceid = priceranges.get(i).getId();
                        priceName = priceranges.get(i).getDescription();
                    }
                }
                type = "1";
                text1.setTextColor(getResources().getColor(R.color.title_backgroid));
                text2.setTextColor(getResources().getColor(R.color.shaixuan));
                text3.setTextColor(getResources().getColor(R.color.shaixuan));
                text3.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.jiage_no_img), null);
                text4.setTextColor(getResources().getColor(R.color.shaixuan));
                text4.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.shaixuan_no_img), null);
                if (popupWindow != null)
                    popupWindow.dismiss();
                title_text.setText(brandName + "商品");
                if (!isNull(pingPName))
                    title_text.setText(pingPName + "商品");
                if (!isNull(typeName))
                    title_text.setText(typeName + "商品");
                if (!isNull(place))
                    title_text.setText(place + "商品");
                if (!isNull(priceName))
                    title_text.setText(priceName + "商品");
                frelshData();
            }
        });

        popupWindow = new PopupWindow(view,
                RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(false);
        popupWindow.setWidth(RadioGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(RadioGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.showAsDropDown(findViewById(R.id.pop_layout_bottom));
        popupWindow.setAnimationStyle(R.style.PopupAnimation);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    //更改各种数据
    private void frelshData() {
        //筛选
        if (isNull(pingPid) && isNull(typeid) && isNull(place) && isNull(priceid)) {
            title_text.setText(brandName + "商品");
            inIt();
        } else if (isNull(pingPid)) {
            //    page = 0;
            getNetWorker().goodsList(typekey, brandId, typeid, type, place, priceid, "", String.valueOf(page));
        } else {
            //    page = 0;
            getNetWorker().goodsList(typekey, pingPid, typeid, type, place, priceid, "", String.valueOf(page));
        }

    }
}
