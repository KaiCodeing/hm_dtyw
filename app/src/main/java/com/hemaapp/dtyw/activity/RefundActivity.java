package com.hemaapp.dtyw.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hemaapp.dtyw.DtywActivity;
import com.hemaapp.dtyw.DtywApplication;
import com.hemaapp.dtyw.DtywHttpInformation;
import com.hemaapp.dtyw.adapter.AddimgRefundAdapter;
import com.hemaapp.dtyw.config.DtywConfig;
import com.hemaapp.dtyw.model.GoodsItems;
import com.hemaapp.dtyw.model.OrderGet;
import com.hemaapp.dtyw.myapplication.R;
import com.hemaapp.dtyw.view.DtywGridView;
import com.hemaapp.dtyw.view.DtywImageWay;
import com.hemaapp.dtyw.view.FlowLayout;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import xtom.frame.util.XtomFileUtil;
import xtom.frame.util.XtomImageUtil;

/**
 * Created by lenovo on 2016/10/8.
 * 退款申请
 */
public class RefundActivity extends DtywActivity {
    private ImageButton back_button;
    private TextView title_text;
    private Button next_button;
    private ImageView commod_img;
    private TextView commod_name;
    private TextView money_text;
    private FlowLayout flowlayout;
    private LinearLayout shangp_layout;
    private LinearLayout anzhuang_layoutl;
    private TextView fuwu_name;
    private TextView address_text;
    private TextView phone_text;
    private TextView money_text_an;
    private TextView commod_number;
    private TextView commod_money;
    private TextView tuikuan_money;
    private EditText input_yijian;
    private DtywGridView gridview_img;
    private String orderid;//订单id
    private String cartid;//购物车id
    private GoodsItems items;
    private OrderGet orderGet;
    private TextView number_text;
    private LinearLayout anzhuang_layout;
    private AddimgRefundAdapter adapter;
    private DtywImageWay imageWay;
    private ArrayList<String> images = new ArrayList<String>();
    private int posterOrImg = 0;
    private Integer orderby = 0;
    private String imagePathCamera;
    private TextView add_view_guige;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_refund);
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            imageWay = new DtywImageWay(mContext, 10, 11) {
                @Override
                public void album() {
                    // 注意：若不重写该方法则使用系统相册选取(对应的onActivityResult中的处理方法也应不同)
                    Intent it = new Intent(mContext, AlbumActivity.class);
                    it.putExtra("limit", 4 - images.size());// 图片选择张数限制
                    startActivityForResult(it, albumRequestCode);
                }
            };
        } else {
            imagePathCamera = savedInstanceState.getString("imagePathCamera");
            imageWay = new DtywImageWay(mContext, 10, 11) {
                @Override
                public void album() {
                    // 注意：若不重写该方法则使用系统相册选取(对应的onActivityResult中的处理方法也应不同)
                    Intent it = new Intent(mContext, AlbumActivity.class);
                    it.putExtra("limit", 4 - images.size());// 图片选择张数限制
                    startActivityForResult(it, albumRequestCode);
                }
            };
        }
        adapter = new AddimgRefundAdapter(RefundActivity.this, images,
                back_button);
        gridview_img.setAdapter(adapter);
        log_i("++++++++++++++++++++++++oncrete" + images);
    }

    public void showImageWay(int type) {
        // posterOrImg = type;
        imageWay.show();
    }

    private void camera() {
        //String imagepath = imageWay.getCameraImage();

        String path = imageWay.getCameraImage();
        if (!isNull(path)) {
            imagePathCamera = path;

        }
        new CompressPicTask().execute(imagePathCamera);
//		else {
//			new CompressPicTask().execute(imagePathCamera);
//		}
        log_i("imagePathCamera=" + imagePathCamera);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (imageWay != null)
            outState.putString("imagePathCamera", imageWay.getCameraImage());
        super.onSaveInstanceState(outState);
    }

    private void album(Intent data) {
        if (data == null)
            return;
        ArrayList<String> imgList = data.getStringArrayListExtra("images");
        if (imgList == null)
            return;
        for (String img : imgList) {
            log_i(img);
            new CompressPicTask().execute(img);
        }
    }

    private class CompressPicTask extends AsyncTask<String, Void, Integer> {
        String compressPath;

        @Override
        protected Integer doInBackground(String... params) {
            try {
                String path = params[0];
                String savedir = XtomFileUtil.getTempFileDir(mContext);
                // if (compressPath==null) {
                // return 1;
                // }
                compressPath = XtomImageUtil.compressPictureWithSaveDir(path,

                        DtywConfig.IMAGE_HEIGHT, DtywConfig.IMAGE_WIDTH,
                        DtywConfig.IMAGE_QUALITY, savedir, mContext);
                return 0;
            } catch (IOException e) {
                return 1;
            }
        }

        @Override
        protected void onPreExecute() {
            showProgressDialog("正在压缩图片");
        }

        @Override
        protected void onPostExecute(Integer result) {
            cancelProgressDialog();
            switch (result) {
                case 0:
                    images.add(compressPath);
                    adapter.notifyDataSetChanged();
                    break;
                case 1:
                    showTextDialog("图片压缩失败");
                    break;
            }
        }
    }

    private void deleteCompressPics() {
        for (String string : images) {
            File file = new File(string);
            file.delete();
        }
    }

    private void imgUpload() {

        String imagePath = images.get(0);
        String token = DtywApplication.getInstance().getUser().getToken();
        getNetWorker().fileUpload(token, "3", cartid, orderid, "0", String.valueOf(orderby), "无", imagePath);
        orderby++;
        images.remove(imagePath);
    }

    private void addSuccess() {
        showTextDialog("退款发送成功");
        Intent intent = new Intent();
        intent.setAction("hemaapp.dtyw.buy.dd.more");
        sendBroadcast(intent);
        next_button.postDelayed(new Runnable() {

            @Override
            public void run() {
                finish();
            }
        }, 2000);
    }

    @Override
    protected void onDestroy() {
        //   deleteCompressPics();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        // super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;
        switch (requestCode) {
            case 10:// 相册
                album(data);
                break;
            case 11:
                camera();
                break;
            default:
                break;
        }

    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ACCOUNT_RETURN:
                showProgressDialog("提交退款申请");
                break;
            case FILE_UPLOAD:
                showProgressDialog("提交退款图片");
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ACCOUNT_RETURN:
            case FILE_UPLOAD:
                cancelProgressDialog();
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ACCOUNT_RETURN:
                if (images.size() > 0) {

                    imgUpload();
                } else
                    addSuccess();
                break;
            case FILE_UPLOAD:
                if (images.size() > 0)
                    imgUpload();
                else
                    addSuccess();
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ACCOUNT_RETURN:
            case FILE_UPLOAD:
                showTextDialog(hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case ACCOUNT_RETURN:
                showTextDialog("提交退款申请失败，请稍后重试");
                break;
            case FILE_UPLOAD:
                showTextDialog("提交退款图片失败，请稍后重试");
                break;
        }
    }

    @Override
    protected void findView() {
        back_button = (ImageButton) findViewById(R.id.back_button);
        title_text = (TextView) findViewById(R.id.title_text);
        next_button = (Button) findViewById(R.id.next_button);

        commod_img = (ImageView) findViewById(R.id.commod_img);
        commod_name = (TextView) findViewById(R.id.commod_name);
        money_text = (TextView) findViewById(R.id.money_text);
        flowlayout = (FlowLayout) findViewById(R.id.flowlayout);
        shangp_layout = (LinearLayout) findViewById(R.id.shangp_layout);
        anzhuang_layoutl = (LinearLayout) findViewById(R.id.anzhuang_layout);
        fuwu_name = (TextView) findViewById(R.id.fuwu_name);
        address_text = (TextView) findViewById(R.id.address_text);
        phone_text = (TextView) findViewById(R.id.phone_text);
        money_text_an = (TextView) findViewById(R.id.money_text_an);

        commod_number = (TextView) findViewById(R.id.commod_number);
        commod_money = (TextView) findViewById(R.id.commod_money);
        tuikuan_money = (TextView) findViewById(R.id.tuikuan_money);
        input_yijian = (EditText) findViewById(R.id.input_yijian);
        gridview_img = (DtywGridView) findViewById(R.id.gridview_img);
        number_text = (TextView) findViewById(R.id.number_text);
        anzhuang_layout = (LinearLayout) findViewById(R.id.anzhuang_layout);
        add_view_guige = (TextView) findViewById(R.id.add_view_guige);
    }

    @Override
    protected void getExras() {
        orderid = mIntent.getStringExtra("orderid");
        cartid = mIntent.getStringExtra("cartid");
        items = (GoodsItems) mIntent.getSerializableExtra("goodsitems");
        orderGet = (OrderGet) mIntent.getSerializableExtra("orderget");
    }

    //填充数据
    private void setData() {
        //商品图标
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.pinpai_def_img)
                .showImageForEmptyUri(R.mipmap.pinpai_def_img)
                .showImageOnFail(R.mipmap.pinpai_def_img).cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoader.getInstance().displayImage(items.getGoodsimgurl(), commod_img, options);
        //商品名称
        commod_name.setText(items.getGoodsname());
        //商品价格
        money_text.setText(items.getGoodsprice());
        //商品数量
        number_text.setText(items.getGoodsnum());
        //商品规格
        String contect = "";
        add_view_guige.setVisibility(View.VISIBLE);
        if (!isNull(items.getGoodspropertyname()))
            contect = items.getGoodspropertyname();
        add_view_guige.setText(contect);
        //判断是否有安装服务
        if ("0".equals(items.getInstall()))
            anzhuang_layout.setVisibility(View.GONE);
        else {
            anzhuang_layout.setVisibility(View.VISIBLE);
            fuwu_name.setText(items.getInstallname());
            address_text.setText(items.getInstalladdress());
            phone_text.setText(items.getInstallphone());
            money_text_an.setText(items.getInstallprice());
        }
        //商品数量
        commod_number.setText("共" + items.getGoodsnum() + "件商品");
        int count = 0;
        for (int i = 0; i < orderGet.getGoodsItem().size(); i++) {
            if ("0".equals(orderGet.getGoodsItem().get(i).getReply()))
                count++;
        }
        Double price = Double.valueOf(items.getGoodsprice());
        Double yunfei = 0.0;
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");

        if (isNull(items.getGoodsshipment())) {
        } else
            yunfei = Double.valueOf(items.getGoodsshipment());
        if (count == 1) {
            commod_money.setText(String.valueOf(df.format(price + yunfei)));
            tuikuan_money.setText(String.valueOf(df.format(price + yunfei)));
            if (0==price && 0==yunfei)
            { commod_money.setText("0.00");
                tuikuan_money.setText("0.00");
            }
        } else {
            commod_money.setText(String.valueOf(df.format(price)));
            tuikuan_money.setText(String.valueOf(df.format(price)));
            if (0==price && 0==yunfei)
            { commod_money.setText("0.00");
                tuikuan_money.setText("0.00");
            }
        }

        //总价
    //    commod_money.setText(items.getGoodsprice());
        //退款总额
      //  tuikuan_money.setText(items.getGoodsprice());

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
        title_text.setText("退款申请");
        next_button.setText("提交");
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = input_yijian.getText().toString().trim();
                if (isNull(content)) {
                    showTextDialog("请填写退款原因");
                    return;
                }
                String token = DtywApplication.getInstance().getUser().getToken();
                getNetWorker().accountReturn(token, orderid, cartid, content);
            }
        });
        setData();
    }

}
