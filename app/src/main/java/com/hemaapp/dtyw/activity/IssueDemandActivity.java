package com.hemaapp.dtyw.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hemaapp.dtyw.DtywActivity;
import com.hemaapp.dtyw.DtywApplication;
import com.hemaapp.dtyw.DtywHttpInformation;
import com.hemaapp.dtyw.adapter.RequsetAdapter;
import com.hemaapp.dtyw.config.DtywConfig;
import com.hemaapp.dtyw.model.CityChildren;
import com.hemaapp.dtyw.model.CitySan;
import com.hemaapp.dtyw.myapplication.R;
import com.hemaapp.dtyw.view.AreaDialog;
import com.hemaapp.dtyw.view.DtywGridView;
import com.hemaapp.dtyw.view.DtywImageWay;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import xtom.frame.util.XtomFileUtil;
import xtom.frame.util.XtomImageUtil;
import xtom.frame.util.XtomSharedPreferencesUtil;

/**
 * Created by lenovo on 2017/2/28.
 * 需求发布
 */
public class IssueDemandActivity extends DtywActivity {
    private ImageButton back_button;
    private TextView title_text;
    private Button next_button;
    private EditText input_title;//标题
    private EditText input_type;//产品型号
    private LinearLayout nickname_layout;//所需产品品牌
    private TextView username_text;//产品品牌
    private EditText input_tel;//输入手机号
    private LinearLayout address_layout;//所在地区
    private TextView address_text;//地区展示
    private EditText input_yijian;//需求说明
    private TextView length_text;//数量展示
    private DtywGridView gridview_img;//照片展示
    private TextView yes_button;//发布
    private Integer orderby = 0;
    private String imagePathCamera;
    private DtywImageWay imageWay;
    private ArrayList<String> images = new ArrayList<String>();
    private RequsetAdapter adapter;
    private String requsetId="0";
    private AreaDialog areaDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_issue_demand);
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            imageWay = new DtywImageWay(mContext, 10, 11) {
                @Override
                public void album() {
                    // 注意：若不重写该方法则使用系统相册选取(对应的onActivityResult中的处理方法也应不同)
                    Intent it = new Intent(mContext, AlbumActivity.class);
                    it.putExtra("limit", 3 - images.size());// 图片选择张数限制
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
                    it.putExtra("limit", 3 - images.size());// 图片选择张数限制
                    startActivityForResult(it, albumRequestCode);
                }
            };
        }
        adapter = new RequsetAdapter(IssueDemandActivity.this, images,
                back_button);
        gridview_img.setAdapter(adapter);
        log_i("++++++++++++++++++++++++oncrete" + images);
        /**
         * 获取全部地址列表
         */
        if (DtywApplication.getInstance().getCityInfo() == null) {
            getNetWorker().addListAllList();
        }
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
            case 1:
                String brandName = data.getStringExtra("brandName");
                username_text.setText(brandName);
                break;
            default:
                break;
        }

    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case REQUIREMENT_ADD:
                showProgressDialog("提交发布需求...");
                break;
            case ADDLISTALL_LIST:
                showProgressDialog("获取地区信息");
                break;
            case FILE_UPLOAD:
                showProgressDialog("上传需求图片");
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case REQUIREMENT_ADD:
            case ADDLISTALL_LIST:
            case FILE_UPLOAD:
                cancelProgressDialog();
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case REQUIREMENT_ADD:
                HemaArrayResult<String> result = (HemaArrayResult<String>) hemaBaseResult;
                requsetId = result.getObjects().get(0);
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
            case ADDLISTALL_LIST:
                HemaArrayResult<CityChildren> result1 = (HemaArrayResult<CityChildren>) hemaBaseResult;
//                CitySan citySan = result1.getObjects().get(0);
//                ArrayList<CitySan> citySanArrayList = ;
                CitySan citySan = new CitySan();
                //     ArrayList<CityChildren> cityChildrens= result1.getObjects();
                citySan.setChildren(result1.getObjects());
                getApplicationContext().setCityInfo(citySan);
                break;
        }
    }
    private void imgUpload() {

        String imagePath = images.get(0);
        String token = DtywApplication.getInstance().getUser().getToken();
        getNetWorker().fileUpload(token, "4", requsetId, "", "0", String.valueOf(orderby), "无", imagePath);
        orderby++;
        images.remove(imagePath);
    }
    private void addSuccess() {
        showTextDialog("需求发送成功");
        next_button.postDelayed(new Runnable() {

            @Override
            public void run() {
                finish();
            }
        }, 1000);
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case REQUIREMENT_ADD:
            case FILE_UPLOAD:
            case ADDLISTALL_LIST:
                showTextDialog(hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information)
        {
            case REQUIREMENT_ADD:
                showTextDialog("提交需求失败，请稍后重试");
                break;
            case FILE_UPLOAD:
                showTextDialog("提交需求图片失败，请稍后重试");
                next_button.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                },1000);
                break;
            case ADDLISTALL_LIST:
                showTextDialog("获取地区失败，请稍后重试");
                break;
        }
    }

    @Override
    protected void findView() {
        back_button = (ImageButton) findViewById(R.id.back_button);
        title_text = (TextView) findViewById(R.id.title_text);
        next_button = (Button) findViewById(R.id.next_button);
        input_title = (EditText) findViewById(R.id.input_title);
        input_type = (EditText) findViewById(R.id.input_type);
        nickname_layout = (LinearLayout) findViewById(R.id.nickname_layout);
        username_text = (TextView) findViewById(R.id.username_text);
        input_tel = (EditText) findViewById(R.id.input_tel);
        address_layout = (LinearLayout) findViewById(R.id.address_layout);
        address_text = (TextView) findViewById(R.id.address_text);
        input_yijian = (EditText) findViewById(R.id.input_yijian);
        length_text = (TextView) findViewById(R.id.length_text);
        gridview_img = (DtywGridView) findViewById(R.id.gridview_img);
        yes_button = (TextView) findViewById(R.id.yes_button);

    }

    @Override
    protected void getExras() {

    }

    @Override
    protected void setListener() {
        next_button.setVisibility(View.INVISIBLE);
        back_button.setImageResource(R.mipmap.back_img);
        title_text.setText("需求发布");
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //发布
        yes_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = input_title.getText().toString().trim();
                String brandName = username_text.getText().toString().trim();
                String type = input_type.getText().toString().trim();
                String tel = input_tel.getText().toString().trim();
                String address = address_text.getText().toString().trim();
                String content = input_yijian.getText().toString().trim();
                if (isNull(title))
                {
                    showTextDialog("请输入标题");
                    return;
                }
                if (isNull(brandName))
                {
                    showTextDialog("请选择产品品牌");
                    return;
                }
                if (isNull(type))
                {
                    showTextDialog("请输入产品型号");
                    return;
                }
                if (isNull(tel))
                {
                    showTextDialog("请输入手机号");
                    return;
                }
                if (isNull(address))
                {
                    showTextDialog("请选择地区");
                    return;
                }
                if (isNull(content))
                {
                    showTextDialog("请输入需求说明");
                    return;
                }
                if (isNull(XtomSharedPreferencesUtil.get(IssueDemandActivity.this,"username")))
                {
                    showTextDialog("请登录");
                    return;
                }
                String token = DtywApplication.getInstance().getUser().getToken();
                getNetWorker().requirementAdd(token,title,brandName,type,content,tel,address);
            }
        });
        //字数控制
        input_yijian.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String content = input_yijian.getText().toString();

                length_text.setText(String.valueOf(content.length())+"/200");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //选择品牌
        nickname_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IssueDemandActivity.this, ProductClassActivity.class);
                intent.putExtra("type","1");
                startActivityForResult(intent,1);
            }
        });
        //选择地区
        address_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCity();
            }
        });

    }
    private void showCity() {
        if (areaDialog == null) {
            areaDialog = new AreaDialog(mContext);
//            areaDialog.closeSan();
            areaDialog.setButtonListener(new onbutton());
            return;
        }
        areaDialog.show();
    }

    private class onbutton implements com.hemaapp.dtyw.view.AreaDialog.OnButtonListener {

        @Override
        public void onLeftButtonClick(AreaDialog dialog) {
            // TODO Auto-generated method stub

            areaDialog.cancel();
        }


        @Override
        public void onRightButtonClick(AreaDialog dialog) {
            // TODO Auto-generated method stub
            address_text.setText(areaDialog.getCityName());
//            homecity = home_text.getText().toString();
//            home_text.setTag(areaDialog.getId());
//            String[] cityid = areaDialog.getId().split(",");
//            homecity = cityid[1];
//            homecounty = cityid[2];
            areaDialog.cancel();
        }

    }
}
