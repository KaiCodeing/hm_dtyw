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
import com.hemaapp.dtyw.adapter.AddImgAdapter;
import com.hemaapp.dtyw.config.DtywConfig;
import com.hemaapp.dtyw.model.GoodsItems;
import com.hemaapp.dtyw.myapplication.R;
import com.hemaapp.dtyw.view.DtywGridView;
import com.hemaapp.dtyw.view.DtywImageWay;
import com.hemaapp.dtyw.view.FlowLayout;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import xtom.frame.util.XtomFileUtil;
import xtom.frame.util.XtomImageUtil;

/**
 * Created by lenovo on 2016/10/9.
 * 商品评价
 */
public class CommodityEvaluateActivity extends DtywActivity {
    private ImageButton back_button;
    private TextView title_text;
    private Button next_button;
    private LinearLayout shangp_layout;
    private ImageView commod_img;
    private TextView commod_name;
    private FlowLayout flowlayout;
    private TextView money_text;
    private TextView number_text;
    private ImageView star1;
    private ImageView star2;
    private ImageView star3;
    private ImageView star4;
    private ImageView star5;
    private EditText input_yijian;
    private DtywGridView gridview;
    private GoodsItems order;
    private AddImgAdapter adapter;
    private DtywImageWay imageWay;
    private ArrayList<String> images = new ArrayList<String>();
    private int posterOrImg = 0;
    private Integer orderby = 0;
    private String id;
    private String start_num = "5";
    private String replyid;
    private String imagePathCamera;
    private TextView add_conent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_commodity_evaluate);
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
            imagePathCamera = savedInstanceState.getString("imagePathDtywCamera");
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
        adapter = new AddImgAdapter(CommodityEvaluateActivity.this, images,
                back_button);
        gridview.setAdapter(adapter);
    }

    public void showImageWay(int type) {
        // posterOrImg = type;
        imageWay.show();
    }

    private void camera() {
        //String imagepath = imageWay.getCameraImage();

        String path = imageWay.getCameraImage();
        log_i("+++++++++__________"+ path);
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
                log_i("+++++++++++++++++++++"+path);
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

    @Override
    protected void onDestroy() {
        deleteCompressPics();
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
    private void imgUpload() {

        String imagePath = images.get(0);
        String token = DtywApplication.getInstance().getUser().getToken();
        getNetWorker().fileUpload(token, "2", replyid, id, "0", String.valueOf(orderby), "无", imagePath);
        orderby++;
        images.remove(imagePath);
    }

    private void addSuccess() {
        showTextDialog("评价发布成功");
        Intent intent = new Intent();
        intent.setAction("hemaapp.dtyw.buy.dd.more");
        sendBroadcast(intent);
        next_button.postDelayed(new Runnable() {

            @Override
            public void run() {
                mIntent.putExtra("key", "key");
                setResult(RESULT_OK, mIntent);
                finish();
            }
        }, 1000);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case REPLY_ADD:
                showProgressDialog("添加评论...");
                break;
            case FILE_UPLOAD:
                showProgressDialog("发表图片");
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case REPLY_ADD:
            case FILE_UPLOAD:
                cancelProgressDialog();
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case REPLY_ADD:
                HemaArrayResult<String> result = (HemaArrayResult<String>) hemaBaseResult;
                replyid = result.getObjects().get(0);
                if (images.size() > 0)
                    imgUpload();
                else
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
            case REPLY_ADD:
            case FILE_UPLOAD:
                showTextDialog(hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case REPLY_ADD:
                showTextDialog("发表评论文字失败，请稍后重试");
                break;
            case FILE_UPLOAD:
                showTextDialog("发表评论图片失败，请稍后重试");
                break;
        }
    }

    @Override
    protected void findView() {
        back_button = (ImageButton) findViewById(R.id.back_button);
        title_text = (TextView) findViewById(R.id.title_text);
        next_button = (Button) findViewById(R.id.next_button);
        shangp_layout = (LinearLayout) findViewById(R.id.shangp_layout);
        commod_img = (ImageView) findViewById(R.id.commod_img);
        commod_name = (TextView) findViewById(R.id.commod_name);
        flowlayout = (FlowLayout) findViewById(R.id.flowlayout);
        money_text = (TextView) findViewById(R.id.money_text);
        number_text = (TextView) findViewById(R.id.number_text);
        star1 = (ImageView) findViewById(R.id.star1);
        star2 = (ImageView) findViewById(R.id.star2);
        star3 = (ImageView) findViewById(R.id.star3);
        star4 = (ImageView) findViewById(R.id.star4);
        star5 = (ImageView) findViewById(R.id.star5);
        input_yijian = (EditText) findViewById(R.id.input_yijian);
        gridview = (DtywGridView) findViewById(R.id.gridview);
        add_conent = (TextView) findViewById(R.id.add_conent);
    }

    @Override
    protected void getExras() {
        order = (GoodsItems) mIntent.getSerializableExtra("order");
        id = mIntent.getStringExtra("id");
    }

    @Override
    protected void setListener() {
        next_button.setText("提交");
        back_button.setImageResource(R.mipmap.back_img);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title_text.setText("商品评价");
        //图标commod_img
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.pinpai_def_img)
                .showImageForEmptyUri(R.mipmap.pinpai_def_img)
                .showImageOnFail(R.mipmap.pinpai_def_img).cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoader.getInstance().displayImage(order.getGoodsimgurl(), commod_img, options);
        //名称
        commod_name.setText(order.getGoodsname());
        //个数
        number_text.setText(order.getGoodsnum());
        //价格
        money_text.setText(order.getGoodsprice());
        //规格
        String content = "无";
        add_conent.setVisibility(View.VISIBLE);
        if (isNull(order.getGoodspropertyname()))
        {}
        else
        content = order.getGoodspropertyname();
        add_conent.setText("商品规格:"+content);
        //星星
        star1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star1.setImageResource(R.mipmap.star_liang_img);
                star2.setImageResource(R.mipmap.star_an_img);
                star3.setImageResource(R.mipmap.star_an_img);
                star4.setImageResource(R.mipmap.star_an_img);
                star5.setImageResource(R.mipmap.star_an_img);
                start_num = "1";
            }
        });
        star2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star1.setImageResource(R.mipmap.star_liang_img);
                star2.setImageResource(R.mipmap.star_liang_img);
                star3.setImageResource(R.mipmap.star_an_img);
                star4.setImageResource(R.mipmap.star_an_img);
                star5.setImageResource(R.mipmap.star_an_img);
                start_num = "2";
            }
        });
        star3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star1.setImageResource(R.mipmap.star_liang_img);
                star2.setImageResource(R.mipmap.star_liang_img);
                star3.setImageResource(R.mipmap.star_liang_img);
                star4.setImageResource(R.mipmap.star_an_img);
                star5.setImageResource(R.mipmap.star_an_img);
                start_num = "3";
            }
        });
        star4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star1.setImageResource(R.mipmap.star_liang_img);
                star2.setImageResource(R.mipmap.star_liang_img);
                star3.setImageResource(R.mipmap.star_liang_img);
                star4.setImageResource(R.mipmap.star_liang_img);
                star5.setImageResource(R.mipmap.star_an_img);
                start_num = "4";
            }
        });
        star5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star1.setImageResource(R.mipmap.star_liang_img);
                star2.setImageResource(R.mipmap.star_liang_img);
                star3.setImageResource(R.mipmap.star_liang_img);
                star4.setImageResource(R.mipmap.star_liang_img);
                star5.setImageResource(R.mipmap.star_liang_img);
                start_num = "5";
            }
        });
        //发布
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = input_yijian.getText().toString().trim();
                if (isNull(content)) {
                    showTextDialog("请填写评论");
                    return;
                }
                String token = DtywApplication.getInstance().getUser().getToken();
                getNetWorker().replyAdd(token, "1", order.getCartid(), order.getGoodsid(), id, content, start_num, "0");
            }
        });

    }
}
